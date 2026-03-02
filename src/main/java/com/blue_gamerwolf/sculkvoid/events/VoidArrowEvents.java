package com.blue_gamerwolf.sculkvoid.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.core.BlockPos;

@Mod.EventBusSubscriber
public class VoidArrowEvents {
	@SubscribeEvent
	public static void onArrowImpact(ProjectileImpactEvent event) {
		if (!(event.getProjectile() instanceof AbstractArrow arrow))
			return;
		if (!arrow.getPersistentData().getBoolean("VoidArrow"))
			return;
		if (!(event.getRayTraceResult() instanceof BlockHitResult hit))
			return;
		Level level = arrow.level();
		if (level.isClientSide())
			return;
		BlockPos center = hit.getBlockPos();
		int radius = 5;
		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
			BlockState state = level.getBlockState(pos);
			// Replace bedrock with barrier
			if (state.is(Blocks.BEDROCK)) {
				level.setBlock(pos, Blocks.BARRIER.defaultBlockState(), 3);
				continue;
			}
			// Strict allowed terrain list
			if (isConvertibleTerrain(state)) {
				level.setBlock(pos, Blocks.SCULK.defaultBlockState(), 3);
			}
		}
		arrow.discard();
	}

	private static boolean isConvertibleTerrain(BlockState state) {
		if (state.is(Blocks.STONE))
			return true;
		if (state.is(Blocks.DEEPSLATE))
			return true;
		if (state.is(Blocks.COBBLESTONE))
			return true;
		if (state.is(Blocks.DIRT))
			return true;
		if (state.is(Blocks.GRASS_BLOCK))
			return true;
		if (state.is(Blocks.COARSE_DIRT))
			return true;
		if (state.is(Blocks.ROOTED_DIRT))
			return true;
		if (state.is(Blocks.SAND))
			return true;
		if (state.is(Blocks.RED_SAND))
			return true;
		if (state.is(Blocks.GRAVEL))
			return true;
		if (state.is(Blocks.NETHERRACK))
			return true;
		if (state.is(Blocks.END_STONE))
			return true;
		if (state.is(Blocks.TUFF))
			return true;
		if (state.is(Blocks.CALCITE))
			return true;
		if (state.is(Blocks.ANDESITE))
			return true;
		if (state.is(Blocks.GRANITE))
			return true;
		if (state.is(Blocks.DIORITE))
			return true;
		return false;
	}
}
