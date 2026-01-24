
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.blue_gamerwolf.sculkvoid.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import com.blue_gamerwolf.sculkvoid.block.SculkoreBlock;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

public class SculkVoidModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SculkVoidMod.MODID);
	public static final RegistryObject<Block> SCULKORE = REGISTRY.register("sculkore", () -> new SculkoreBlock());
}
