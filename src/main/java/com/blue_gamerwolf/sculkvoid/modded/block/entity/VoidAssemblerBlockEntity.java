package com.blue_gamerwolf.sculkvoid.modded.block.entity;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.SimpleContainer;

import com.blue_gamerwolf.sculkvoid.modded.crafting.voidassembler.VoidAssemblyRecipe;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModCustomContent;
import com.blue_gamerwolf.sculkvoid.modded.world.inventory.VoidAssemblerMenu;

public class VoidAssemblerBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
	private static final int[] SLOTS_FOR_UP = new int[] { 0, 1 };
	private static final int[] SLOTS_FOR_DOWN = new int[] { 2 };
	private static final int[] SLOTS_FOR_SIDES = new int[] { 0, 1 };
	private static final int DEFAULT_CRAFT_TIME = 120;

	private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
	private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	private int progress;
	private int maxProgress = DEFAULT_CRAFT_TIME;

	private final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> VoidAssemblerBlockEntity.this.progress;
				case 1 -> VoidAssemblerBlockEntity.this.maxProgress;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> VoidAssemblerBlockEntity.this.progress = value;
				case 1 -> VoidAssemblerBlockEntity.this.maxProgress = value;
				default -> {
				}
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

	public VoidAssemblerBlockEntity(BlockPos pos, BlockState state) {
		super(SculkVoidModCustomContent.VOID_ASSEMBLER_BLOCK_ENTITY.get(), pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, VoidAssemblerBlockEntity blockEntity) {
		Optional<VoidAssemblyRecipe> recipe = blockEntity.getCurrentRecipe();
		if (recipe.isPresent() && blockEntity.canCraft(recipe.get())) {
			blockEntity.maxProgress = recipe.get().getCraftTime();
			blockEntity.progress++;
			if (blockEntity.progress >= blockEntity.maxProgress) {
				blockEntity.craft(recipe.get());
				blockEntity.progress = 0;
			}
			blockEntity.setChanged();
			return;
		}

		if (blockEntity.progress != 0) {
			blockEntity.progress = 0;
			blockEntity.setChanged();
		}
		blockEntity.maxProgress = DEFAULT_CRAFT_TIME;
	}

	private Optional<VoidAssemblyRecipe> getCurrentRecipe() {
		if (this.level == null) {
			return Optional.empty();
		}

		SimpleContainer input = new SimpleContainer(2);
		input.setItem(0, this.getItem(0));
		input.setItem(1, this.getItem(1));
		return this.level.getRecipeManager().getRecipeFor(VoidAssemblyRecipe.Type.INSTANCE, input, this.level);
	}

	private boolean canCraft(VoidAssemblyRecipe recipe) {
		ItemStack output = this.getItem(2);
		ItemStack result = recipe.getResultItem(this.level.registryAccess());
		if (result.isEmpty()) {
			return false;
		}
		if (output.isEmpty()) {
			return true;
		}
		if (!ItemStack.isSameItemSameTags(output, result)) {
			return false;
		}
		return output.getCount() + result.getCount() <= output.getMaxStackSize();
	}

	private void craft(VoidAssemblyRecipe recipe) {
		ItemStack result = recipe.getResultItem(this.level.registryAccess()).copy();
		ItemStack output = this.getItem(2);

		this.removeItem(0, 1);
		this.removeItem(1, 1);

		if (output.isEmpty()) {
			this.setItem(2, result);
		} else {
			output.grow(result.getCount());
		}
	}

	public ContainerData getDataAccess() {
		return this.dataAccess;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (!this.tryLoadLootTable(tag)) {
			this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, this.items);
		}
		this.progress = tag.getInt("Progress");
		this.maxProgress = tag.contains("MaxProgress") ? tag.getInt("MaxProgress") : DEFAULT_CRAFT_TIME;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.trySaveLootTable(tag)) {
			ContainerHelper.saveAllItems(tag, this.items);
		}
		tag.putInt("Progress", this.progress);
		tag.putInt("MaxProgress", this.maxProgress);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> stacks) {
		this.items = stacks;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return index < 2;
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.DOWN) {
			return SLOTS_FOR_DOWN;
		}
		return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return this.canPlaceItem(index, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return index == 2;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.sculk_void.void_assembler");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new VoidAssemblerMenu(id, inventory, this, this.dataAccess);
	}

	@Override
	public Component getDisplayName() {
		return this.getDefaultName();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
		if (!this.remove && capability == ForgeCapabilities.ITEM_HANDLER) {
			if (side == null) {
				return this.handlers[0].cast();
			}
			return this.handlers[Math.min(side.ordinal(), this.handlers.length - 1)].cast();
		}
		return super.getCapability(capability, side);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
			handler.invalidate();
		}
	}
}
