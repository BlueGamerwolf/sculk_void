
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.blue_gamerwolf.sculkvoid.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import com.blue_gamerwolf.sculkvoid.item.WardenHeartItem;
import com.blue_gamerwolf.sculkvoid.item.VoidBowItem;
import com.blue_gamerwolf.sculkvoid.item.SkyVampSytheItem;
import com.blue_gamerwolf.sculkvoid.item.SculkIngotItem;
import com.blue_gamerwolf.sculkvoid.item.RawSculkOreItem;
import com.blue_gamerwolf.sculkvoid.item.CrystalArrowItem;
import com.blue_gamerwolf.sculkvoid.item.CrimsonDaggerItem;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

public class SculkVoidModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SculkVoidMod.MODID);
	public static final RegistryObject<Item> RAW_SCULK_ORE = REGISTRY.register("raw_sculk_ore", () -> new RawSculkOreItem());
	public static final RegistryObject<Item> WARDEN_HEART = REGISTRY.register("warden_heart", () -> new WardenHeartItem());
	public static final RegistryObject<Item> SCULK_INGOT = REGISTRY.register("sculk_ingot", () -> new SculkIngotItem());
	public static final RegistryObject<Item> SCULKORE = block(SculkVoidModBlocks.SCULKORE);
	public static final RegistryObject<Item> VOIDSCULK = block(SculkVoidModBlocks.VOIDSCULK);
	public static final RegistryObject<Item> SCULK_INVESTED_BARREL = block(SculkVoidModBlocks.SCULK_INVESTED_BARREL);
	public static final RegistryObject<Item> SCULK_NUKE = block(SculkVoidModBlocks.SCULK_NUKE);
	public static final RegistryObject<Item> SKY_VAMP_SYTHE = REGISTRY.register("sky_vamp_sythe", () -> new SkyVampSytheItem());
	public static final RegistryObject<Item> CRIMSON_DAGGER = REGISTRY.register("crimson_dagger", () -> new CrimsonDaggerItem());
	public static final RegistryObject<Item> VOID_BOW = REGISTRY.register("void_bow", () -> new VoidBowItem());
	public static final RegistryObject<Item> CRYSTAL_ARROW = REGISTRY.register("crystal_arrow", () -> new CrystalArrowItem());

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
