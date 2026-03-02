package com.blue_gamerwolf.sculkvoid.procedures;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

public class BindCompassToAlterProcedure {
	public static void execute(Level world, Entity entity, ItemStack stack) {
		if (!(entity instanceof Player player))
			return;
		if (world.isClientSide())
			return;
		// Only affect normal compass
		if (!stack.is(Items.COMPASS))
			return;
		ServerLevel serverLevel = (ServerLevel) world;
		TagKey<Structure> alterTag = TagKey.create(Registries.STRUCTURE, new ResourceLocation("sculk_void", "alter"));
		BlockPos nearest = serverLevel.findNearestMapStructure(alterTag, player.blockPosition(), 100, false);
		if (nearest == null) {
			player.displayClientMessage(Component.literal("§8The alter is silent."), true);
			return;
		}
		// Lodestone binding
		stack.getOrCreateTag().putBoolean("LodestoneTracked", true);
		stack.getOrCreateTag().putInt("LodestonePosX", nearest.getX());
		stack.getOrCreateTag().putInt("LodestonePosY", nearest.getY());
		stack.getOrCreateTag().putInt("LodestonePosZ", nearest.getZ());
		stack.getOrCreateTag().putString("LodestoneDimension", serverLevel.dimension().location().toString());
		// Rename item
		stack.setHoverName(Component.literal("§5Alter"));
		// Add lore
		CompoundTag display = stack.getOrCreateTagElement("display");
		ListTag loreList = new ListTag();
		loreList.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("§5the alter's heart..."))));
		display.put("Lore", loreList);
		player.displayClientMessage(Component.literal("§5The alter answers..."), true);
	}
}
