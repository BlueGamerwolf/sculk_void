package com.blue_gamerwolf.sculkvoid.modded.events;

import org.joml.Vector3f;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.DustParticleOptions;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModItems;

@Mod.EventBusSubscriber
public class VoidPrisonDamageHandler {
	private static final ResourceKey<Level> ENDLESS_VOID = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("sculk_void:endless_void"));

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player))
			return;
		ItemStack offhand = player.getOffhandItem();
		if (offhand.getItem() != SculkVoidModItems.VOID_PRISON.get())
			return;
		LivingEntity target = event.getEntity();
		if (player.level().isClientSide())
			return;
		float damage = event.getAmount();
		if (target.getHealth() - damage <= 0) {
			event.setCanceled(true);
			ServerLevel level = (ServerLevel) target.level();
			double cx = target.getX();
			double cy = target.getY() + 0.1;
			double cz = target.getZ();
			for (int ring = 0; ring < 3; ring++) {
				double radius = 1.3 + ring * 0.35;
				for (int i = 0; i < 64; i++) {
					double angle = i * (Math.PI * 2 / 64) + (ring * 0.6);
					double x = cx + Math.cos(angle) * radius;
					double z = cz + Math.sin(angle) * radius;
					level.sendParticles(new DustParticleOptions(new Vector3f(0.5f, 0.0f, 0.8f), 1.8f), x, cy, z, 1, 0.02, 0, 0.02, 0);
				}
			}
			for (int i = 0; i < 120; i++) {
				double angle = i * 0.18;
				double radius = 0.2 + i * 0.015;
				double x = cx + Math.cos(angle) * radius;
				double y = cy + i * 0.045;
				double z = cz + Math.sin(angle) * radius;
				level.sendParticles(new DustParticleOptions(new Vector3f(0.06f, 0.0f, 0.1f), 2.0f), x, y, z, 1, 0, 0.02, 0, 0);
			}
			level.sendParticles(new DustParticleOptions(new Vector3f(0.65f, 0.0f, 1f), 2.6f), cx, cy + 1, cz, 140, 0.9, 0.9, 0.9, 0.04);
			level.playSound(null, target.blockPosition(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 2.2F, 0.75F);
			offhand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
			if (target instanceof ServerPlayer serverPlayer) {
				ServerLevel voidLevel = level.getServer().getLevel(ENDLESS_VOID);
				if (voidLevel != null) {
					serverPlayer.teleportTo(voidLevel, 0.5, 100, 0.5, serverPlayer.getYRot(), serverPlayer.getXRot());
				}
			}
		}
	}
}
