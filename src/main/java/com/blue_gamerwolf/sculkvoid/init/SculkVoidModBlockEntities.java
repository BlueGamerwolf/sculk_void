
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.blue_gamerwolf.sculkvoid.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import com.blue_gamerwolf.sculkvoid.block.entity.SculkInvestedBarrelBlockEntity;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

public class SculkVoidModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SculkVoidMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> SCULK_INVESTED_BARREL = register("sculk_invested_barrel", SculkVoidModBlocks.SCULK_INVESTED_BARREL, SculkInvestedBarrelBlockEntity::new);

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
