package com.blue_gamerwolf.sculkvoid.modded.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Collections;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModBlocks;

public class HellGateFrameBlock extends Block {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty EYE = BooleanProperty.create("eye");

	public HellGateFrameBlock() {
		super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.GLASS).strength(-1, 3600000).noOcclusion().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true)
				.isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EYE, false));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case NORTH -> box(0, 0, -0.1, 16, 13.1, 16);
			case SOUTH -> box(0, 0, 0, 16, 13.1, 16.1);
			case EAST -> box(0, 0, 0, 16, 13.1, 16);
			case WEST -> box(-0.1, 0, 0, 16, 13.1, 16);
			default -> Shapes.block();
		};
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getShape(state, world, pos, context);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, EYE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(EYE, false);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (!state.getValue(EYE) && stack.getItem() == SculkVoidModItems.NULL_EYE.get()) {
			if (!level.isClientSide) {
				level.setBlock(pos, state.setValue(EYE, true), 3);
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
				tryActivatePortal(level, pos);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	private void tryActivatePortal(Level level, BlockPos pos) {
		for (int centerOffsetX = -2; centerOffsetX <= 2; centerOffsetX++) {
			for (int centerOffsetZ = -2; centerOffsetZ <= 2; centerOffsetZ++) {
				BlockPos center = pos.offset(centerOffsetX, 0, centerOffsetZ);
				if (isValidPortalFrame(level, center)) {
					activatePortal(level, center);
					return;
				}
			}
		}
	}

	private boolean isValidPortalFrame(Level level, BlockPos center) {
		BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				check.set(center.getX() + x, center.getY(), center.getZ() + z);
				if ((Math.abs(x) == 2 || Math.abs(z) == 2) && !(Math.abs(x) == 2 && Math.abs(z) == 2)) {
					BlockState state = level.getBlockState(check);
					if (!(state.getBlock() instanceof HellGateFrameBlock) || !state.getValue(EYE) || state.getValue(FACING) != getRequiredFacing(x, z)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private Direction getRequiredFacing(int x, int z) {
		if (z == -2) {
			return Direction.SOUTH;
		}
		if (z == 2) {
			return Direction.NORTH;
		}
		if (x == -2) {
			return Direction.EAST;
		}
		return Direction.WEST;
	}

	private void activatePortal(Level level, BlockPos center) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockPos portalPos = center.offset(x, 0, z);
				BlockState portalState = SculkVoidModBlocks.NULL_PORTAL.get().defaultBlockState().setValue(NullPortalBlock.U, x + 1).setValue(NullPortalBlock.V, z + 1);
				level.setBlock(portalPos, portalState, 3);
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}
}
