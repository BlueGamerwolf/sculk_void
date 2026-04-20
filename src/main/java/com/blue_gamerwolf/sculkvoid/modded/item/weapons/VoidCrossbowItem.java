package com.blue_gamerwolf.sculkvoid.modded.item.weapons;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

import com.blue_gamerwolf.sculkvoid.modded.enchantment.VoidEnchantmentHelper;

@Mod.EventBusSubscriber
public class VoidCrossbowItem extends net.minecraft.world.item.CrossbowItem {

    private static final String VOID_LEVEL = "VoidLevel";
    private static final String VOID_XP = "VoidXP";
    private static final int MAX_VOID_LEVEL = 100;

    public VoidCrossbowItem() {
        super(new Properties().durability(6800).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> false;
    }

    private int getVoidLevel(ItemStack stack) {
        return stack.getOrCreateTag().getInt(VOID_LEVEL);
    }

    private int getVoidXP(ItemStack stack) {
        return stack.getOrCreateTag().getInt(VOID_XP);
    }

    private int getRequiredUses(int level) {
        if (level < 10) return 15 + level * 3;
        if (level < 25) return 40 + level * 4;
        if (level < 50) return 90 + level * 5;
        if (level < 75) return 150 + level * 6;
        return 220 + level * 7;
    }

    private void addVoidXP(ItemStack stack, int amount) {
        CompoundTag tag = stack.getOrCreateTag();
        int level = tag.getInt(VOID_LEVEL);
        int xp = tag.getInt(VOID_XP);

        if (level >= MAX_VOID_LEVEL) return;

        xp += amount;

        while (xp >= getRequiredUses(level)) {
            xp -= getRequiredUses(level);
            level++;

            if (level >= MAX_VOID_LEVEL) {
                level = MAX_VOID_LEVEL;
                xp = 0;
                break;
            }
        }

        tag.putInt(VOID_LEVEL, level);
        tag.putInt(VOID_XP, xp);
    }

    private Vec3 findBeamEnd(Level level, Vec3 start, Vec3 direction, double range) {
        Vec3 pos = start;

        for (double d = 0; d < range; d += 0.5) {
            pos = start.add(direction.scale(d));
            BlockPos blockPos = BlockPos.containing(pos);
            BlockState state = level.getBlockState(blockPos);

            if (!state.getFluidState().isEmpty()) continue;
            if (state.blocksMotion()) return pos;
        }

        return start.add(direction.scale(range));
    }

    private void spawnVoidBeam(Level level, Vec3 start, Vec3 end) {
        if (!(level instanceof ServerLevel server)) return;

        Vec3 dir = end.subtract(start).normalize();
        double distance = start.distanceTo(end);

        for (double d = 0; d < distance; d += 1.2) {
            Vec3 center = start.add(dir.scale(d));

            server.sendParticles(ParticleTypes.SCULK_SOUL, center.x, center.y, center.z, 4, 0.05, 0.05, 0.05, 0);
            server.sendParticles(ParticleTypes.REVERSE_PORTAL, center.x, center.y, center.z, 2, 0, 0, 0, 0);
        }
    }

    private void spawnCompactVoidBeam(Level level, Vec3 start, Vec3 end) {
        if (!(level instanceof ServerLevel server)) return;

        Vec3 dir = end.subtract(start).normalize();
        double distance = start.distanceTo(end);

        for (double d = 0; d < distance; d += 0.6) {
            Vec3 pos = start.add(dir.scale(d));

            server.sendParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 6, 0.03, 0.03, 0.03, 0);
            server.sendParticles(ParticleTypes.REVERSE_PORTAL, pos.x, pos.y, pos.z, 3, 0, 0, 0, 0);
        }
    }

    // 💥 FINAL DAMAGE SYSTEM
    private void damageAlongBeam(ServerLevel level, Vec3 start, Vec3 end, LivingEntity shooter,
                                 double radius, float damage, int punch, int flame) {

        Vec3 dir = end.subtract(start).normalize();
        double distance = start.distanceTo(end);

        for (double d = 0; d < distance; d += 1.0) {
            Vec3 point = start.add(dir.scale(d));

            var entities = level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(
                            point.x - radius, point.y - radius, point.z - radius,
                            point.x + radius, point.y + radius, point.z + radius
                    ),
                    e -> e != shooter
            );

            for (LivingEntity target : entities) {

                target.hurt(target.damageSources().magic(), damage);

                if (flame > 0) {
                    target.setSecondsOnFire(4 * flame);
                }

                double kb = Math.min(0.1, punch * 0.05);

                target.push(
                        dir.x * kb,
                        0.03,
                        dir.z * kb
                );
            }
        }
    }

    private void fireVoidBeam(Level level, LivingEntity shooter, ItemStack stack) {
        if (!(shooter instanceof Player player)) return;
        if (level.isClientSide()) return;

        ServerLevel server = (ServerLevel) level;

        int voidLevel = getVoidLevel(stack);

        int god = VoidEnchantmentHelper.getVoidGod(stack);
        int power = VoidEnchantmentHelper.getPower(stack);
        int punch = VoidEnchantmentHelper.getPunch(stack);
        int flame = VoidEnchantmentHelper.getFlame(stack);
        int multishot = VoidEnchantmentHelper.getMultishot(stack);

        level.playSound(null, shooter.blockPosition(),
                SoundEvents.WARDEN_SONIC_BOOM,
                SoundSource.PLAYERS, 2f, 0.8f);

        Vec3 start = shooter.getEyePosition();
        Vec3 direction = shooter.getViewVector(1.0F).normalize();

        double range = 40 + (voidLevel * 0.35) + (god * 2);

        float damage = 12.0F + (power * 5.0F);
        double radius = 2 + (multishot * 1.5);

        int beams = 1 + (multishot / 2);

        for (int i = 0; i < beams; i++) {

            Vec3 spreadDir = direction.add(
                    (level.random.nextDouble() - 0.5) * 0.1,
                    (level.random.nextDouble() - 0.5) * 0.1,
                    (level.random.nextDouble() - 0.5) * 0.1
            ).normalize();

            Vec3 end = findBeamEnd(level, start, spreadDir, range);

            damageAlongBeam(server, start, end, shooter,
                    radius, damage, punch, flame);

            if (god == 10) {
                spawnCompactVoidBeam(level, start, end);
            } else {
                spawnVoidBeam(level, start, end);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (net.minecraft.world.item.CrossbowItem.isCharged(stack)) {
            fireVoidBeam(level, player, stack);
            net.minecraft.world.item.CrossbowItem.setCharged(stack, false);
            return InteractionResultHolder.consume(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        int voidLevel = getVoidLevel(stack);
        int voidXP = getVoidXP(stack);
        int required = getRequiredUses(voidLevel);
        int god = VoidEnchantmentHelper.getVoidGod(stack);

        tooltip.add(Component.literal("§5Void Level: §d" + voidLevel));
        tooltip.add(Component.literal("§5Void XP: §d" + voidXP + "§7 / " + required));

        if (voidLevel >= MAX_VOID_LEVEL)
            tooltip.add(Component.literal("§6MAX VOID LEVEL"));

        tooltip.add(Component.literal("§5Void God: §eLevel " + god));

        if (god == 10)
            tooltip.add(Component.literal("§c§lVOID OBLITERATION MODE"));
    }
}