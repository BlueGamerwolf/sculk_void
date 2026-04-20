package com.blue_gamerwolf.sculkvoid.modded.item.weapons;

import com.blue_gamerwolf.sculkvoid.modded.network.SkyVampSlashMessage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;

import java.util.Objects;
import java.util.List;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SkyVampSytheItem extends SwordItem {

    private static final String TAG_KILLS = "SkyKills";
    private static final String TAG_STAGE = "SkyStage";

    public SkyVampSytheItem() {
        super(new Tier() {
            public int getUses() { return 6800; }
            public float getSpeed() { return 4f; }
            public float getAttackDamageBonus() { return 7f; }
            public int getLevel() { return 5; }
            public int getEnchantmentValue() { return 18; }

            @Nonnull
            public Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(SculkVoidModItems.SCULK_INGOT.get()));
            }
        }, 4, -1.8f, new Item.Properties().fireResistant());
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world,
                                @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {

        var tag = stack.getOrCreateTag();

        list.add(Component.literal("§cA Gift from the one they call Voidstone..."));
        list.add(Component.literal("§cKills: §7" + tag.getInt(TAG_KILLS)));
        list.add(Component.literal("§cStage: §7" + tag.getInt(TAG_STAGE)));

        super.appendHoverText(stack, world, list, flag);
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            SkyVampSlashMessage msg = new SkyVampSlashMessage();
            SculkVoidMod.PACKET_HANDLER.sendToServer(msg);
            return;
        }
        handleSlash(event.getEntity());
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide) {
            SkyVampSlashMessage msg = new SkyVampSlashMessage();
            SculkVoidMod.PACKET_HANDLER.sendToServer(msg);
            return;
        }
        handleSlash(event.getEntity());
    }

    public static void handleSlash(Player player) {

        Level level = player.level();
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof SkyVampSytheItem item)) return;

        var tag = stack.getOrCreateTag();
        int stage = tag.getInt(TAG_STAGE);

        SculkVoidMod.debug("[SCYTHE] Attempt ability | Player={} Stage={} Health={}",
                player.getName().getString(), stage, player.getHealth());

        if (stage < 1) return;

        if (player.getCooldowns().isOnCooldown(item)) return;

        if (player.getHealth() <= 3.0F) return;

        if (level.isClientSide) return;

        ServerLevel server = (ServerLevel) level;

        var look = player.getLookAngle();
        var damageSrc = player.damageSources().magic();

        player.hurt(damageSrc, 3.0F);
        player.getCooldowns().addCooldown(item, 20);

        int maxTicks;
        int damageAmount;
        double speedPerTick;
        double hitRadius;
        boolean pierceBlocks;

        if (stage == 1) {
            maxTicks = 15 * 20;
            speedPerTick = 0.6;
            hitRadius = 1.75;
            damageAmount = 15;
            pierceBlocks = false;
        } else if (stage == 2) {
            maxTicks = 20 * 20;
            speedPerTick = 0.8;
            hitRadius = 2;
            damageAmount = 20;
            pierceBlocks = true;
        } else if (stage == 3) {
            maxTicks = 25 * 20;
            speedPerTick = 1.0;
            hitRadius = 4;
            damageAmount = 26;
            pierceBlocks = true;
        } else {
            maxTicks = 30 * 20;
            speedPerTick = 1.3;
            hitRadius = 6;
            damageAmount = 30;
            pierceBlocks = true;
        }

        final double[] px = {player.getX()};
        final double[] py = {player.getEyeY()};
        final double[] pz = {player.getZ()};
        final int[] ticksLeft = {maxTicks};
        final boolean[] finished = {false};

        Runnable tickRunner = new Runnable() {
            @Override
            public void run() {

                if (finished[0]) return;

                if (ticksLeft[0]-- <= 0) {
                    finished[0] = true;
                    return;
                }

                px[0] += look.x * speedPerTick;
                py[0] += look.y * speedPerTick;
                pz[0] += look.z * speedPerTick;

                double x = px[0];
                double y = py[0];
                double z = pz[0];

                var lookVec = look.normalize();
                var rightVec = lookVec.cross(new net.minecraft.world.phys.Vec3(0, 1, 0)).normalize();

                if (!pierceBlocks) {
                    BlockPos pos = BlockPos.containing(x, y, z);
                    if (!level.getBlockState(pos).isAir()) {
                        finished[0] = true;
                        return;
                    }
                }

                AABB aabb = new AABB(x - hitRadius, y - hitRadius, z - hitRadius,
                        x + hitRadius, y + hitRadius, z + hitRadius);

                var list = level.getEntitiesOfClass(LivingEntity.class, aabb, e -> e != player);

                for (var living : list) {
                    living.hurt(player.damageSources().playerAttack(player), damageAmount);
                }

                SculkVoidMod.queueServerWork(1, this);
            }
        };

        SculkVoidMod.queueServerWork(1, tickRunner);
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {

        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        ItemStack main = player.getMainHandItem();
        if (!(main.getItem() instanceof SkyVampSytheItem)) return;

        var tag = main.getOrCreateTag();

        int kills = tag.getInt(TAG_KILLS) + 1;
        tag.putInt(TAG_KILLS, kills);

        int newStage = calculateStage(kills);
        tag.putInt(TAG_STAGE, newStage);

        SculkVoidMod.LOGGER.info("[SCYTHE] Kill registered | Player={} Kills={} Stage={}",
                player.getName().getString(), kills, newStage);
    }

    private static int calculateStage(int kills) {
        if (kills >= 50) return 4;
        if (kills >= 30) return 3;
        if (kills >= 15) return 2;
        if (kills >= 5) return 1;
        return 0;
    }

    public static void debug(String message, Object... args) {
        if (SculkVoidMod.DEBUG) {
            SculkVoidMod.LOGGER.info(message, args);
        }
    }
}