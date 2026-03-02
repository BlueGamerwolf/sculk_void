package com.blue_gamerwolf.sculkvoid.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;

import java.util.UUID;

import com.blue_gamerwolf.sculkvoid.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID)
public class WeaponsgiverProcedure {
	// 🔥 REPLACE THESE WITH REAL UUIDS
	private static final UUID TSUKISORA_UUID = UUID.fromString("a1bc8320-401b-43ff-a73f-581ea979e506");
	private static final UUID VAMPIRE_UUID = UUID.fromString("557d2bd3-544c-47bd-9576-dce31f73c378");
	private static final UUID PHEONIX_UUID = UUID.fromString("d1be0b2d-4a64-4eea-976f-2d69de547c08");

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		LivingEntity victim = event.getEntity();
		// Must be villager
		if (!(victim instanceof Villager))
			return;
		// Killer must be player
		if (!(event.getSource().getEntity() instanceof Player player))
			return;
		ItemStack heldItem = player.getMainHandItem();
		// Must be wooden sword
		if (!heldItem.is(Items.WOODEN_SWORD))
			return;
		// Must have custom name
		if (!heldItem.hasCustomHoverName())
			return;
		// Must be named exactly "END"
		if (!heldItem.getHoverName().getString().equals("END"))
			return;
		UUID killerUUID = player.getUUID();
		// Tsukisora → Void Bow
		if (killerUUID.equals(TSUKISORA_UUID)) {
			giveIfMissing(player, new ItemStack(SculkVoidModItems.VOID_BOW.get()));
		}
		// Vampire → Sky Vamp Scythe
		if (killerUUID.equals(VAMPIRE_UUID)) {
			giveIfMissing(player, new ItemStack(SculkVoidModItems.SKY_VAMP_SYTHE.get()));
		}
		// Pheonix → Crimson Dagger
		if (killerUUID.equals(PHEONIX_UUID)) {
			giveIfMissing(player, new ItemStack(SculkVoidModItems.CRIMSON_DAGGER.get()));
		}
	}

	private static void giveIfMissing(Player player, ItemStack stack) {
		boolean alreadyHas = player.getInventory().contains(stack);
		if (!alreadyHas) {
			player.addItem(stack);
			player.displayClientMessage(Component.literal("§5The void has chosen you."), true);
		}
	}
}
