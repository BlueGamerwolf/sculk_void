package com.blue_gamerwolf.sculkvoid.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import java.util.List;

import com.blue_gamerwolf.sculkvoid.procedures.SculkNukeboomboomProcedure;

public class SculkNukeBlock extends Block {

	public SculkNukeBlock() {
		super(BlockBehaviour.Properties.of()
				.ignitedByLava()
				.sound(SoundType.GRASS)
				.strength(1f, 10f));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
		super.neighborChanged(blockstate, world, pos, neighborBlock, fromPos, moving);

		if (!world.isClientSide() && world.getBestNeighborSignal(pos) > 0) {

			// ❌ Remove block FIRST (no drops, no retrigger)
			world.removeBlock(pos, false);

			// 💥 Trigger nuke logic
			SculkNukeboomboomProcedure.execute(
					world,
					pos.getX(),
					pos.getY(),
					pos.getZ()
			);
		}
	}
}
