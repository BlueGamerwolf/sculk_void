
package com.blue_gamerwolf.sculkvoid.modded.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import com.blue_gamerwolf.sculkvoid.modded.block.VoidsculkBlock;
import com.blue_gamerwolf.sculkvoid.modded.block.SculkoreBlock;
import com.blue_gamerwolf.sculkvoid.modded.block.SculkNukeBlock;
import com.blue_gamerwolf.sculkvoid.modded.block.SculkInvestedBarrelBlock;
import com.blue_gamerwolf.sculkvoid.modded.block.NullPortalBlock;
import com.blue_gamerwolf.sculkvoid.modded.block.HellGateFrameBlock;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

public class SculkVoidModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SculkVoidMod.MODID);
	public static final RegistryObject<Block> SCULKORE = REGISTRY.register("sculkore", () -> new SculkoreBlock());
	public static final RegistryObject<Block> VOIDSCULK = REGISTRY.register("voidsculk", () -> new VoidsculkBlock());
	public static final RegistryObject<Block> SCULK_INVESTED_BARREL = REGISTRY.register("sculk_invested_barrel", () -> new SculkInvestedBarrelBlock());
	public static final RegistryObject<Block> SCULK_NUKE = REGISTRY.register("sculk_nuke", () -> new SculkNukeBlock());
	public static final RegistryObject<Block> HELL_GATE_FRAME = REGISTRY.register("hell_gate_frame", () -> new HellGateFrameBlock());
	public static final RegistryObject<Block> NULL_PORTAL = REGISTRY.register("null_portal", () -> new NullPortalBlock());
}
