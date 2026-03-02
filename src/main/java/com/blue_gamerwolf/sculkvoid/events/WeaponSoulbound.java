package com.blue_gamerwolf.sculkvoid.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

import com.blue_gamerwolf.sculkvoid.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID)
public class WeaponSoulbound {
	// Temporary storage for soulbound items
	private static final Map<UUID, List<ItemStack>> SOULBOUND_STORAGE = new HashMap<>();

	// STEP 1: Remove from drops and store
	@SubscribeEvent
	public static void onPlayerDrops(LivingDropsEvent event) {
		if (!(event.getEntity() instanceof Player player))
			return;
		if (player.level().isClientSide())
			return;
		List<ItemStack> savedItems = new ArrayList<>();
		Iterator<ItemEntity> iterator = event.getDrops().iterator();
		while (iterator.hasNext()) {
			ItemEntity itemEntity = iterator.next();
			ItemStack stack = itemEntity.getItem();
			if (stack.is(SculkVoidModItems.SKY_VAMP_SYTHE.get()) || stack.is(SculkVoidModItems.CRYSTAL_ARROW.get()) || stack.is(SculkVoidModItems.CRIMSON_DAGGER.get()) || stack.is(SculkVoidModItems.VOID_BOW.get())) {
				savedItems.add(stack.copy());
				iterator.remove(); // Prevent drop
			}
		}
		if (!savedItems.isEmpty()) {
			SOULBOUND_STORAGE.put(player.getUUID(), savedItems);
		}
	}

	// STEP 2: Give back after respawn
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event) {
		if (!event.isWasDeath())
			return;
		Player newPlayer = event.getEntity();
		UUID uuid = newPlayer.getUUID();
		if (!SOULBOUND_STORAGE.containsKey(uuid))
			return;
		List<ItemStack> savedItems = SOULBOUND_STORAGE.get(uuid);
		for (ItemStack stack : savedItems) {
			newPlayer.getInventory().add(stack);
		}
		SOULBOUND_STORAGE.remove(uuid); // Clean up
	}
}
