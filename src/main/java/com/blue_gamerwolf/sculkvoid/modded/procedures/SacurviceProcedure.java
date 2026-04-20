package com.blue_gamerwolf.sculkvoid.modded.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import java.util.UUID;
import java.util.List;

@Mod.EventBusSubscriber
public class SacurviceProcedure {
	private static final UUID CHOSEN_UUID = UUID.fromString("a1bc8320-401b-43ff-a73f-581ea979e506");
	private static final ResourceLocation VOIDSCULK_BLOCK = new ResourceLocation("sculk_void", "voidsculk");
	private static final ResourceLocation TSUKISORA_ITEM = new ResourceLocation("forlorn_curios", "tsukisora");

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntity();
		Level level = entity.level();
		if (!(level instanceof ServerLevel serverLevel))
			return;
		BlockPos pos = entity.getOnPos();
		Block blockBelow = serverLevel.getBlockState(pos.below()).getBlock();
		ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(blockBelow);
		if (blockId == null || !blockId.equals(VOIDSCULK_BLOCK))
			return;
		if (entity instanceof ServerPlayer player) {
			if (player.getUUID().equals(CHOSEN_UUID)) {
				var item = ForgeRegistries.ITEMS.getValue(TSUKISORA_ITEM);
				if (item == null)
					return;
				ItemStack reward = new ItemStack(item);
				if (!player.getInventory().add(reward)) {
					player.drop(reward, false);
				}
			}
			return;
		}
		if (entity instanceof Villager) {
			var item = ForgeRegistries.ITEMS.getValue(TSUKISORA_ITEM);
			if (item == null)
				return;
			ItemStack reward = new ItemStack(item);
			List<ServerPlayer> playersNearby = serverLevel.getPlayers(p -> p.distanceToSqr(entity) <= 32 * 32);
			if (!playersNearby.isEmpty()) {
				ServerPlayer nearest = playersNearby.get(0);
				if (!nearest.getInventory().add(reward)) {
					nearest.drop(reward, false);
				}
			}
		}
	}
}
