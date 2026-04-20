package com.blue_gamerwolf.sculkvoid.modded.block;

import net.minecraftforge.common.util.ITeleporter;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.util.function.Function;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModDimansions;
import com.blue_gamerwolf.sculkvoid.modded.block.entity.NullPortalBlockEntity;

public class NullPortalBlock extends EndPortalBlock implements EntityBlock {
	public static final IntegerProperty U = IntegerProperty.create("u", 0, 2);
	public static final IntegerProperty V = IntegerProperty.create("v", 0, 2);

	public NullPortalBlock() {
		super(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F).noLootTable().noOcclusion().noCollission());
		this.registerDefaultState(this.stateDefinition.any().setValue(U, 1).setValue(V, 1));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
		builder.add(U, V);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (level.isClientSide)
			return;
		if (!(entity instanceof ServerPlayer player))
			return;
		if (player.isPassenger() || player.isVehicle())
			return;
		if (!player.canChangeDimensions())
			return;
		if (level.dimension().equals(SculkVoidModDimansions.ENDLESS_VOID)) {
			ServerLevel overworld = player.server.getLevel(Level.OVERWORLD);
			BlockPos respawn = player.getRespawnPosition();
			if (respawn == null) {
				respawn = overworld.getSharedSpawnPos();
			}
			final BlockPos spawn = respawn;
			player.changeDimension(overworld, new ITeleporter() {
				@Override
				public Entity placeEntity(Entity entity, ServerLevel current, ServerLevel dest, float yaw, Function<Boolean, Entity> reposition) {
					Entity e = reposition.apply(false);
					e.teleportTo(spawn.getX() + 0.5, spawn.getY() + 1, spawn.getZ() + 0.5);
					return e;
				}
			});
		} else {
			ServerLevel target = player.server.getLevel(SculkVoidModDimansions.ENDLESS_VOID);
			if (target != null) {
				player.changeDimension(target, new ITeleporter() {
					@Override
					public Entity placeEntity(Entity entity, ServerLevel current, ServerLevel dest, float yaw, Function<Boolean, Entity> reposition) {
						Entity e = reposition.apply(false);
						e.teleportTo(0.5, 100, 0.5);
						return e;
					}
				});
			}
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NullPortalBlockEntity(pos, state);
	}
}
