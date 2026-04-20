
package com.blue_gamerwolf.sculkvoid.modded.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import com.blue_gamerwolf.sculkvoid.modded.item.meterials.others.WardenHeartItem;
import com.blue_gamerwolf.sculkvoid.modded.item.utils.VoidPrisonItem;
import com.blue_gamerwolf.sculkvoid.modded.item.weapons.VoidCrossbowItem;
import com.blue_gamerwolf.sculkvoid.modded.item.weapons.SkyVampSytheItem;
import com.blue_gamerwolf.sculkvoid.modded.item.meterials.crafting.SculkIngotItem;
import com.blue_gamerwolf.sculkvoid.modded.item.meterials.crafting.RawSculkOreItem;
import com.blue_gamerwolf.sculkvoid.modded.item.meterials.others.NullEyeItem;
import com.blue_gamerwolf.sculkvoid.modded.item.ammunition.CrystalArrowItem;
import com.blue_gamerwolf.sculkvoid.modded.item.weapons.CrimsonDaggerItem;
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
	public static final RegistryObject<Item> CRYSTAL_ARROW = REGISTRY.register("crystal_arrow", () -> new CrystalArrowItem());
	public static final RegistryObject<Item> VOID_PRISON = REGISTRY.register("void_prison", () -> new VoidPrisonItem());
	public static final RegistryObject<Item> HELL_GATE_FRAME = block(SculkVoidModBlocks.HELL_GATE_FRAME);
	public static final RegistryObject<Item> NULL_EYE = REGISTRY.register("null_eye", () -> new NullEyeItem());
	public static final RegistryObject<Item> NULL_PORTAL = block(SculkVoidModBlocks.NULL_PORTAL);
	public static final RegistryObject<Item> VOID_CROSSBOW = REGISTRY.register("void_crossbow", () -> new VoidCrossbowItem());

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
