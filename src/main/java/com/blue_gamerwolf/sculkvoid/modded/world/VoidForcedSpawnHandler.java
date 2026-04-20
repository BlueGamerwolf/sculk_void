package com.blue_gamerwolf.sculkvoid.modded.world;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID)
public class VoidForcedSpawnHandler {
	private static final ResourceKey<Level> ENDLESS_VOID = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("sculk_void:endless_void"));
	private static final BlockPos RESPAWN_POS = new BlockPos(0, 100, 0);

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player))
			return;
		if (!player.level().dimension().equals(ENDLESS_VOID))
			return;
		player.setRespawnPosition(ENDLESS_VOID, RESPAWN_POS, 0.0F, true, false);
	}
}
