package com.blue_gamerwolf.sculkvoid.modded.enchantment;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.EnvyEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.GreedEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.GluttonyEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.LustEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.PrideEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.SlothEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.WrathEnchantment;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModDimansions;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID)
public final class EnchantmentEvents {

    private static final String CUSTOMER_SERVICE_COOLDOWN = "sculkvoid_customer_service_cooldown";
    private static final String ORPHAN_CARROT_COUNT = "sculkvoid_orphan_carrots";
    private static final int ORPHAN_REQUIRED_CARROTS = 100_000;

    private EnchantmentEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;
        if (player.level().isClientSide()) {
            return;
        }

        applyFowlSmell(player);
        applySevenDeadlySins(player);
    }

    @SubscribeEvent
    public static void onPortalTravel(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (!Level.NETHER.location().equals(event.getDimension())) {
            return;
        }
        if (!hasEnchantmentAnywhere(player, SculkVoidModEnchantments.CURSE_OF_CUSTOMER_SERVICE.get())) {
            return;
        }

        long gameTime = player.serverLevel().getGameTime();
        long nextAllowed = player.getPersistentData().getLong(CUSTOMER_SERVICE_COOLDOWN);
        if (gameTime < nextAllowed) {
            event.setCanceled(true);
            return;
        }

        ServerLevel nether = player.server.getLevel(Level.NETHER);
        if (nether == null) {
            return;
        }

        event.setCanceled(true);
        player.getPersistentData().putLong(CUSTOMER_SERVICE_COOLDOWN, gameTime + 200L);
        BlockPos spawn = nether.getSharedSpawnPos();
        player.teleportTo(nether, spawn.getX() + 0.5D, spawn.getY() + 1D, spawn.getZ() + 0.5D, player.getYRot(), player.getXRot());
        player.setPortalCooldown();
    }

    @SubscribeEvent
    public static void onPigFeed(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Pig pig)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.CARROT)) {
            return;
        }

        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        int fed = player.getPersistentData().getInt(ORPHAN_CARROT_COUNT) + 1;
        player.getPersistentData().putInt(ORPHAN_CARROT_COUNT, fed);

        if (fed == ORPHAN_REQUIRED_CARROTS) {
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            net.minecraft.world.item.enchantment.EnchantmentHelper.setEnchantments(
                    java.util.Map.of(SculkVoidModEnchantments.ORPHAN_OBLITERATOR.get(), 1),
                    book
            );
            if (!player.addItem(book)) {
                player.drop(book, false);
            }
        }
    }

    @SubscribeEvent
    public static void onPigKilled(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Pig)) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }

        int level = EnchantmentHelper.getItemEnchantmentLevel(
                SculkVoidModEnchantments.ORPHAN_OBLITERATOR.get(),
                player.getMainHandItem()
        );
        if (level <= 0) {
            return;
        }

        ServerLevel target = player.server.getLevel(SculkVoidModDimansions.ENDLESS_VOID);
        if (target == null) {
            return;
        }

        BlockPos spawn = target.getSharedSpawnPos();
        player.teleportTo(target, spawn.getX() + 0.5D, spawn.getY() + 1D, spawn.getZ() + 0.5D, player.getYRot(), player.getXRot());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) {
            return;
        }

        LivingEntity target = event.getEntity();

        applyWeaponSinEffects(attacker, target, weapon);
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (!right.is(Items.CARROT) || !left.is(Items.IRON_SWORD)) {
            return;
        }
    }

    private static void applyFowlSmell(Player player) {
        int level = highestArmorEnchantmentLevel(player, SculkVoidModEnchantments.FOWL_SMELL.get());
        if (level <= 0) {
            return;
        }

        if (player.tickCount % 40 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 80, Math.max(0, level - 1), true, false, true));
        }

        AABB aura = player.getBoundingBox().inflate(3.0D + level);
        for (LivingEntity nearby : player.level().getEntitiesOfClass(LivingEntity.class, aura, entity -> entity != player)) {
            nearby.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, true, true, true));
        }
    }

    private static void applySevenDeadlySins(Player player) {
        if (hasWeaponEnchantment(player, SculkVoidModEnchantments.GLUTTONY.get())) {
            GluttonyEnchantment.applyHungerCurse(player);
        }
        if (hasWeaponEnchantment(player, SculkVoidModEnchantments.GREED.get())) {
            GreedEnchantment.applyCurse(player);
        }
        if (hasWeaponEnchantment(player, SculkVoidModEnchantments.WRATH.get())) {
            WrathEnchantment.applyCurse(player);
        }
        if (hasWeaponEnchantment(player, SculkVoidModEnchantments.SLOTH.get()) && player.tickCount % 40 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, false, true));
        }
        if (hasWeaponEnchantment(player, SculkVoidModEnchantments.PRIDE.get()) && player.tickCount % 60 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, false, true));
        }
        if (hasWeaponEnchantment(player, SculkVoidModEnchantments.LUST.get()) && player.tickCount % 40 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 0, true, false, true));
        }
        if (hasWeaponEnchantment(player, SculkVoidModEnchantments.ENVY.get()) && player.tickCount % 40 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, true, false, true));
        }
    }

    private static void applyWeaponSinEffects(LivingEntity attacker, LivingEntity target, ItemStack weapon) {
        if (EnchantmentHelper.getItemEnchantmentLevel(SculkVoidModEnchantments.ENVY.get(), weapon) > 0) {
            attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 0, true, false, true));
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(SculkVoidModEnchantments.LUST.get(), weapon) > 0) {
            attacker.heal(1.0F);
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(SculkVoidModEnchantments.PRIDE.get(), weapon) > 0 && attacker.getHealth() == attacker.getMaxHealth()) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, true, true, true));
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(SculkVoidModEnchantments.SLOTH.get(), weapon) > 0) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, true, true, true));
        }
    }

    private static boolean hasEnchantmentAnywhere(Player player, net.minecraft.world.item.enchantment.Enchantment enchantment) {
        return highestArmorEnchantmentLevel(player, enchantment) > 0
                || EnchantmentHelper.getItemEnchantmentLevel(enchantment, player.getMainHandItem()) > 0
                || EnchantmentHelper.getItemEnchantmentLevel(enchantment, player.getOffhandItem()) > 0;
    }

    private static int highestArmorEnchantmentLevel(Player player, net.minecraft.world.item.enchantment.Enchantment enchantment) {
        int highest = 0;
        for (ItemStack armor : player.getArmorSlots()) {
            highest = Math.max(highest, EnchantmentHelper.getItemEnchantmentLevel(enchantment, armor));
        }
        return highest;
    }

    private static boolean hasWeaponEnchantment(Player player, net.minecraft.world.item.enchantment.Enchantment enchantment) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, player.getMainHandItem()) > 0;
    }
}
