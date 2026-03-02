package com.blue_gamerwolf.sculkvoid.item;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import java.util.function.Predicate;
import java.util.List;

import com.blue_gamerwolf.sculkvoid.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.init.SculkVoidModEnchantments;

@Mod.EventBusSubscriber
public class VoidBowItem extends BowItem {
	private static final String VOID_LEVEL = "VoidLevel";
	private static final String VOID_XP = "VoidXP";
	private static final int MAX_VOID_LEVEL = 100;

	public VoidBowItem() {
		super(new Item.Properties().durability(6800).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return stack -> stack.is(SculkVoidModItems.CRYSTAL_ARROW.get());
	}

	private int getVoidLevel(ItemStack stack) {
		return stack.getOrCreateTag().getInt(VOID_LEVEL);
	}

	private int getVoidXP(ItemStack stack) {
		return stack.getOrCreateTag().getInt(VOID_XP);
	}

	private int getRequiredUses(int level) {
		return 100 + (level * 15);
	}

	private void addVoidUse(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		int level = tag.getInt(VOID_LEVEL);
		int xp = tag.getInt(VOID_XP);
		if (level >= MAX_VOID_LEVEL)
			return;
		xp++;
		if (xp >= getRequiredUses(level)) {
			level++;
			xp = 0;
			tag.putInt(VOID_LEVEL, level);
		}
		tag.putInt(VOID_XP, xp);
	}

	// ====================================================
	// STRICT SCULK CONVERSION
	// ====================================================
	private static boolean canSculkConvert(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (state.isAir())
			return false;
		if (!state.getFluidState().isEmpty())
			return false;
		if (level.getBlockEntity(pos) != null)
			return false;
		// Explicit allowed blocks only
		if (state.is(Blocks.STONE))
			return true;
		if (state.is(Blocks.DEEPSLATE))
			return true;
		if (state.is(Blocks.COBBLESTONE))
			return true;
		if (state.is(Blocks.DIRT))
			return true;
		if (state.is(Blocks.GRASS_BLOCK))
			return true;
		if (state.is(Blocks.COARSE_DIRT))
			return true;
		if (state.is(Blocks.ROOTED_DIRT))
			return true;
		if (state.is(Blocks.SAND))
			return true;
		if (state.is(Blocks.RED_SAND))
			return true;
		if (state.is(Blocks.GRAVEL))
			return true;
		if (state.is(Blocks.NETHERRACK))
			return true;
		if (state.is(Blocks.END_STONE))
			return true;
		if (state.is(Blocks.TUFF))
			return true;
		if (state.is(Blocks.CALCITE))
			return true;
		if (state.is(Blocks.ANDESITE))
			return true;
		if (state.is(Blocks.GRANITE))
			return true;
		if (state.is(Blocks.DIORITE))
			return true;
		return false;
	}

	// ====================================================
	// ARROW IMPACT
	// ====================================================
	@SubscribeEvent
	public static void onArrowImpact(ProjectileImpactEvent event) {
		if (!(event.getProjectile() instanceof Arrow arrow))
			return;
		if (!arrow.getPersistentData().getBoolean("VoidArrow"))
			return;
		if (!(event.getRayTraceResult() instanceof BlockHitResult hit))
			return;
		Level level = arrow.level();
		if (level.isClientSide())
			return;
		BlockPos center = hit.getBlockPos();
		int radius = 3;
		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))) {
			BlockState state = level.getBlockState(pos);
			// Bedrock → Barrier
			if (state.is(Blocks.BEDROCK)) {
				level.setBlock(pos, Blocks.BARRIER.defaultBlockState(), 3);
				continue;
			}
			if (canSculkConvert(level, pos)) {
				level.setBlock(pos, Blocks.SCULK.defaultBlockState(), 3);
			}
		}
	}

	// ====================================================
	// SHOOTING LOGIC
	// ====================================================
	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
		if (!(shooter instanceof Player player))
			return;
		ItemStack ammo = player.getProjectile(stack);
		boolean isCreative = player.getAbilities().instabuild;
		boolean hasAmmo = ammo.is(SculkVoidModItems.CRYSTAL_ARROW.get());
		if (!isCreative && !hasAmmo)
			return;
		float power = BowItem.getPowerForTime(this.getUseDuration(stack) - timeLeft);
		if (power < 0.1F)
			return;
		int voidLevel = getVoidLevel(stack);
		int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(SculkVoidModEnchantments.VOID_GOD.get(), stack);
		if (!level.isClientSide()) {
			int arrowCount = enchantLevel >= 1 ? 5 : 1;
			for (int i = 0; i < arrowCount; i++) {
				float spread = 8f;
				float yawOffset = arrowCount > 1 ? (i - (arrowCount - 1) / 2f) * spread : 0f;
				Arrow arrow = new Arrow(level, shooter);
				arrow.setOwner(shooter);
				arrow.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
				arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
				arrow.getPersistentData().putBoolean("VoidArrow", true);
				arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + yawOffset, 0.0F, power * 3.5F, 1.0F);
				double finalDamage = (28.0 * power) + (voidLevel * 0.4) + (enchantLevel * 4.0);
				arrow.setBaseDamage(finalDamage);
				if (enchantLevel > 0) {
					arrow.setSecondsOnFire(100);
					arrow.setKnockback(2 + enchantLevel / 2);
					arrow.setPierceLevel((byte) 4);
					arrow.setCritArrow(true);
				}
				level.addFreshEntity(arrow);
			}
			addVoidUse(stack);
		}
		if (!isCreative && hasAmmo && enchantLevel == 0) {
			ammo.shrink(1);
		}
		player.getCooldowns().addCooldown(this, 5);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		int voidLevel = getVoidLevel(stack);
		int voidXP = getVoidXP(stack);
		int required = getRequiredUses(voidLevel);
		int enchantLevel = stack.getEnchantmentLevel(SculkVoidModEnchantments.VOID_GOD.get());
		tooltip.add(Component.literal("§5Void Level: §d" + voidLevel));
		tooltip.add(Component.literal("§5Void XP: §d" + voidXP + "§7 / " + required));
		if (voidLevel >= MAX_VOID_LEVEL) {
			tooltip.add(Component.literal("§6MAX VOID LEVEL"));
		}
		if (enchantLevel > 0) {
			tooltip.add(Component.literal("§6Void God: §eLevel " + enchantLevel));
			tooltip.add(Component.literal("§7Multishot Scales With Level"));
			tooltip.add(Component.literal("§7Infinity Enabled"));
		}
		tooltip.add(Component.literal("§8Arrows corrupt the land on impact..."));
	}
}
