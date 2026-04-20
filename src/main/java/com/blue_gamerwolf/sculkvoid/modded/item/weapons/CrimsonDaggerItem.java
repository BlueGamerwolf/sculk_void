package com.blue_gamerwolf.sculkvoid.modded.item.weapons;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.UUID;
import java.util.List;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModItems;

import com.blue_gamerwolf.sculkvoid.modded.race.data.storage.RaceData;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CrimsonDaggerItem extends SwordItem {

    private static final String TAG_KILLS = "CBKills";
    private static final String TAG_STAGE = "CBStage";
    private static final String TAG_OWNER = "BoundOwner";
    private static final String TAG_COOLDOWN = "AbilityCooldown";
    private static final String TAG_HITS = "ComboHits";

    private static final TagKey<net.minecraft.world.entity.EntityType<?>> HELL =
            TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("sculk_void", "hell"));

    public CrimsonDaggerItem() {
        super(new Tier() {
            public int getUses() { return 6800; }
            public float getSpeed() { return 4f; }
            public float getAttackDamageBonus() { return 12f; }
            public int getLevel() { return 1; }
            public int getEnchantmentValue() { return 30; }
            public Ingredient getRepairIngredient() {
                return Ingredient.of(SculkVoidModItems.SCULK_INGOT.get());
            }
        }, 3, -1.7f, new Item.Properties().fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!(attacker instanceof Player player))
            return super.hurtEnemy(stack, target, attacker);

        CompoundTag tag = stack.getOrCreateTag();

        // 🔒 Bind owner
        if (!tag.hasUUID(TAG_OWNER)) {
            tag.putUUID(TAG_OWNER, player.getUUID());
        }

        UUID owner = tag.getUUID(TAG_OWNER);

        // 🔥 ONLY allow owner
        if (!owner.equals(player.getUUID())) {
            player.sendSystemMessage(Component.literal("§4The dagger rejects you."));
            return false;
        }

        int stage = tag.getInt(TAG_STAGE);

        switch (stage) {

            case 1 -> {
                if (isBehindTarget(player, target)) {
                    target.hurt(player.damageSources().playerAttack(player), 6f);
                    player.heal(2f);
                }
            }

            case 2 -> {
                int hits = tag.getInt(TAG_HITS) + 1;
                tag.putInt(TAG_HITS, hits);

                if (hits >= 3) {
                    target.hurt(player.damageSources().playerAttack(player), 10f);
                    player.heal(3f);
                    tag.putInt(TAG_HITS, 0);
                }
            }

            case 3 -> {
                if (target.getHealth() <= target.getMaxHealth() * 0.35f) {
                    target.hurt(player.damageSources().playerAttack(player), 12f);
                }
            }

            case 4 -> {
                if (target.getHealth() <= target.getMaxHealth() * 0.30f) {
                    target.hurt(player.damageSources().playerAttack(player), 30f);
                }

                if (player.getHealth() <= player.getMaxHealth() * 0.35f) {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 0));
                }
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if (level.isClientSide)
            return InteractionResultHolder.pass(stack);

        if (!RaceData.hasRace(player)) {
            player.sendSystemMessage(Component.literal("§7You have no race power."));
            return InteractionResultHolder.fail(stack);
        }

        int stage = tag.getInt(TAG_STAGE);
        if (stage < 1)
            return InteractionResultHolder.pass(stack);

        RaceResourceProvider resource =
                RaceResourceRegistry.get(RaceData.getRaceId(player));

        if (resource == null)
            return InteractionResultHolder.pass(stack);

        int cost = getCostForStage(stage);

        if (!resource.has(player, cost)) {
            player.sendSystemMessage(Component.literal("§4Not enough power."));
            return InteractionResultHolder.fail(stack);
        }

        long gameTime = level.getGameTime();
        long cooldown = tag.getLong(TAG_COOLDOWN);
        long stageCooldown = getCooldownForStage(stage);

        if (stageCooldown > 0 && gameTime < cooldown)
            return InteractionResultHolder.fail(stack);

        // 🔥 consume resource
        resource.consume(player, cost);

        quickstep(level, player, stage);

        if (stageCooldown > 0)
            tag.putLong(TAG_COOLDOWN, gameTime + stageCooldown);

        return InteractionResultHolder.success(stack);
    }

    private void quickstep(Level level, Player player, int stage) {

        double strength = 2.5 + (stage * 0.4);
        float damage = 6f + (stage * 2f);

        player.setDeltaMovement(player.getLookAngle().scale(strength));
        player.hurtMarked = true;

        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 15, 0));

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(2.5),
                e -> e != player
        );

        for (LivingEntity e : entities) {
            e.hurt(player.damageSources().playerAttack(player), damage);

            double dx = e.getX() - player.getX();
            double dz = e.getZ() - player.getZ();
            e.setDeltaMovement(dx * 0.4, 0.25, dz * 0.4);
        }
    }

    private long getCooldownForStage(int stage) {
        return switch (stage) {
            case 1 -> 120;
            case 2 -> 80;
            case 3 -> 40;
            case 4 -> 0;
            default -> 0;
        };
    }

    private int getCostForStage(int stage) {
        return switch (stage) {
            case 1 -> 10;
            case 2 -> 15;
            case 3 -> 25;
            case 4 -> 40;
            default -> 0;
        };
    }

    private boolean isBehindTarget(Player player, LivingEntity target) {
        double dx = player.getX() - target.getX();
        double dz = player.getZ() - target.getZ();

        float targetYaw = target.getYRot();
        double angle = Math.atan2(dz, dx) * (180 / Math.PI) - 90;
        double difference = Math.abs(targetYaw - angle);

        return difference < 60;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        list.add(Component.literal("§cCrimson Dagger"));
        list.add(Component.literal("§7Kills: §c" + tag.getInt(TAG_KILLS)));
        list.add(Component.literal("§7Stage: §c" + tag.getInt(TAG_STAGE)));
        super.appendHoverText(stack, world, list, flag);
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {

        if (!(event.getSource().getEntity() instanceof Player player))
            return;
        if (player.level().isClientSide)
            return;

        ItemStack main = player.getMainHandItem();
        if (!(main.getItem() instanceof CrimsonDaggerItem))
            return;

        LivingEntity victim = event.getEntity();
        if (victim.getType().is(HELL))
            return;

        CompoundTag tag = main.getOrCreateTag();

        int kills = tag.getInt(TAG_KILLS) + 1;
        tag.putInt(TAG_KILLS, kills);

        int oldStage = tag.getInt(TAG_STAGE);
        int newStage = calculateStage(kills);
        tag.putInt(TAG_STAGE, newStage);

        if (newStage != oldStage) {
            player.sendSystemMessage(Component.literal("§cThe Crimson Dagger evolves... Stage " + newStage));
        }

        if (RaceData.hasRace(player)) {
            RaceResourceProvider resource =
                    RaceResourceRegistry.get(RaceData.getRaceId(player));

            if (resource != null) {
                resource.add(player, 5);
            }
        }
    }

    private static int calculateStage(int kills) {
        if (kills >= 50) return 4;
        if (kills >= 30) return 3;
        if (kills >= 15) return 2;
        if (kills >= 5)  return 1;
        return 0;
    }
}