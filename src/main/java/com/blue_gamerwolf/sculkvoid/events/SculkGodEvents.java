package com.blue_gamerwolf.sculkvoid.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import com.blue_gamerwolf.sculkvoid.init.SculkVoidModEnchantments;

@Mod.EventBusSubscriber
public class SculkGodEvents {
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player))
			return;
		ItemStack stack = player.getMainHandItem();
		int level = EnchantmentHelper.getItemEnchantmentLevel(SculkVoidModEnchantments.SCULK_GOD.get(), stack);
		if (level <= 0)
			return;
		LivingEntity target = event.getEntity();
		float damage = event.getAmount();
		damage += 1.25F * level;
		if (target.getMobType() == MobType.UNDEAD) {
			damage += 2.5F * level;
		}
		if (target.getMobType() == MobType.ARTHROPOD) {
			damage += 2.5F * level;
			target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * level, Math.min(4, level - 1)));
		}
		damage += 1.0F * level;
		float multiplier = 2.0F * (2.50F + level);
		damage *= multiplier;
		event.setAmount(damage);
		target.setSecondsOnFire(4 * level);
		double strength = 0.4D + (0.3D * level);
		double dx = -Math.sin(Math.toRadians(player.getYRot())) * strength;
		double dz = Math.cos(Math.toRadians(player.getYRot())) * strength;
		target.push(dx, 0.1D, dz);
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player))
			return;
		ItemStack stack = player.getMainHandItem();
		int level = EnchantmentHelper.getItemEnchantmentLevel(SculkVoidModEnchantments.SCULK_GOD.get(), stack);
		if (level <= 0)
			return;
		event.getEntity().spawnAtLocation(Items.EXPERIENCE_BOTTLE, level * 2);
	}
}
