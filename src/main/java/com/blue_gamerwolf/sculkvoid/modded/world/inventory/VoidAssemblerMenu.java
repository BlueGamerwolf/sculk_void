package com.blue_gamerwolf.sculkvoid.modded.world.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.blue_gamerwolf.sculkvoid.modded.block.entity.VoidAssemblerBlockEntity;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModCustomContent;

public class VoidAssemblerMenu extends AbstractContainerMenu {
	private final VoidAssemblerBlockEntity blockEntity;
	private final Level level;
	private final ContainerData data;

	public VoidAssemblerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
		this(id, inventory, getBlockEntity(inventory, extraData), new SimpleContainerData(2));
	}

	public VoidAssemblerMenu(int id, Inventory inventory, VoidAssemblerBlockEntity blockEntity, ContainerData data) {
		super(SculkVoidModCustomContent.VOID_ASSEMBLER_MENU.get(), id);
		this.blockEntity = blockEntity;
		this.level = inventory.player.level();
		this.data = data;

		checkContainerSize(blockEntity, 3);
		blockEntity.startOpen(inventory.player);

		this.addSlot(new Slot(blockEntity, 0, 56, 17));
		this.addSlot(new Slot(blockEntity, 1, 56, 53));
		this.addSlot(new Slot(blockEntity, 2, 116, 35) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
		});

		addPlayerInventory(inventory);
		addPlayerHotbar(inventory);
		addDataSlots(data);
	}

	private static VoidAssemblerBlockEntity getBlockEntity(Inventory inventory, FriendlyByteBuf extraData) {
		BlockPos pos = extraData.readBlockPos();
		BlockEntity blockEntity = inventory.player.level().getBlockEntity(pos);
		if (blockEntity instanceof VoidAssemblerBlockEntity assembler) {
			return assembler;
		}
		throw new IllegalStateException("Missing void assembler block entity at " + pos);
	}

	public boolean isCrafting() {
		return this.data.get(0) > 0;
	}

	public int getScaledProgress() {
		int progress = this.data.get(0);
		int maxProgress = this.data.get(1);
		int arrowWidth = 24;
		return maxProgress > 0 && progress > 0 ? progress * arrowWidth / maxProgress : 0;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack original = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem();
		original = stack.copy();

		if (index < 3) {
			if (!this.moveItemStackTo(stack, 3, this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else if (!this.moveItemStackTo(stack, 0, 2, false)) {
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		slot.onTake(player, stack);
		return original;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()), player, SculkVoidModCustomContent.VOID_ASSEMBLER.get());
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.blockEntity.stopOpen(player);
	}

	private void addPlayerInventory(Inventory inventory) {
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				this.addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
			}
		}
	}

	private void addPlayerHotbar(Inventory inventory) {
		for (int slot = 0; slot < 9; slot++) {
			this.addSlot(new Slot(inventory, slot, 8 + slot * 18, 142));
		}
	}
}
