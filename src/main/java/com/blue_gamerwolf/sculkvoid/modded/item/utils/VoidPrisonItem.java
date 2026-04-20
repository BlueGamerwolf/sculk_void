package com.blue_gamerwolf.sculkvoid.modded.item.utils;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;

import com.blue_gamerwolf.sculkvoid.modded.util.VoidDamageSources;

public class VoidPrisonItem extends Item {
	public VoidPrisonItem() {
		super(new Item.Properties().stacksTo(1).durability(3).fireResistant().rarity(Rarity.COMMON));
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!(attacker instanceof Player killer))
			return super.hurtEnemy(stack, target, attacker);
		if (!target.isAlive()) {
			if (!killer.level().isClientSide()) {
				ServerLevel currentLevel = (ServerLevel) target.level();
				currentLevel.broadcastEntityEvent(target, (byte) 35);
				currentLevel.sendParticles(ParticleTypes.SCULK_CHARGE_POP, target.getX(), target.getY() + 1, target.getZ(), 40, 1, 1, 1, 0.1);
				currentLevel.sendParticles(ParticleTypes.SOUL, target.getX(), target.getY() + 1, target.getZ(), 25, 1, 1, 1, 0.05);
				currentLevel.playSound(null, target.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 2.0F, 1.0F);
				target.hurt(VoidDamageSources.voidPrison(target.level().registryAccess()), Float.MAX_VALUE);
				MinecraftServer server = killer.getServer();
				if (server != null) {
					ResourceKey<Level> voidDimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("sculk_void:endless_void"));
					ServerLevel targetLevel = server.getLevel(voidDimension);
					if (targetLevel != null) {
						target.changeDimension(targetLevel);
					}
				}
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
}
