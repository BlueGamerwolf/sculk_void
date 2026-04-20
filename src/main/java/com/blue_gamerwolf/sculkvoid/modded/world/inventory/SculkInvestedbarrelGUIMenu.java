package com.blue_gamerwolf.sculkvoid.modded.world.inventory;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModBlocks;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModMenus;

public class SculkInvestedbarrelGUIMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
	public static final HashMap<String, Object> guistate = new HashMap<>();
	public final Level world;
	public final Player entity;
	private final ContainerLevelAccess access;
	private IItemHandler itemHandler = new ItemStackHandler(54); // 6x9 block inventory
	private final Map<Integer, Slot> customSlots = new HashMap<>();

	/* ------------------------------------------------------------
	 * CLIENT CONSTRUCTOR
	 * ------------------------------------------------------------ */
	public SculkInvestedbarrelGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		this(id, inv, getBlockEntity(inv, extraData));
	}

	/* ------------------------------------------------------------
	 * SERVER CONSTRUCTOR
	 * ------------------------------------------------------------ */
	public SculkInvestedbarrelGUIMenu(int id, Inventory inv, BlockEntity blockEntity) {
		super(SculkVoidModMenus.SCULK_INVESTEDBARREL_GUI.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		if (blockEntity != null) {
			this.access = ContainerLevelAccess.create(world, blockEntity.getBlockPos());
			blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> this.itemHandler = handler);
		} else {
			this.access = ContainerLevelAccess.NULL;
		}
		// ===== BLOCK INVENTORY (6x9 = 54 slots) =====
		int index = 0;
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 9; col++) {
				SlotItemHandler slot = new SlotItemHandler(itemHandler, index, 8 + col * 18, 18 + row * 18);
				this.addSlot(slot);
				customSlots.put(index++, slot);
			}
		}
		int guiHeight = 256;
		int playerInvY = guiHeight - 35 - (3 * 18 + 18 + 4) - 5;
		int hotbarY = playerInvY + 3 * 18 + 4;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				Slot slot = new Slot(inv, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18);
				this.addSlot(slot);
				customSlots.put(index++, slot);
			}
		}
		for (int col = 0; col < 9; col++) {
			Slot slot = new Slot(inv, col, 8 + col * 18, hotbarY);
			this.addSlot(slot);
			customSlots.put(index++, slot);
		}
	}

	/* ------------------------------------------------------------
	 * SAFE PACKET READ
	 * ------------------------------------------------------------ */
	private static BlockEntity getBlockEntity(Inventory inv, FriendlyByteBuf buf) {
		if (buf == null)
			return null;
		BlockPos pos = buf.readBlockPos();
		return inv.player.level().getBlockEntity(pos);
	}

	/* ------------------------------------------------------------
	 * VALIDITY CHECK
	 * ------------------------------------------------------------ */
	@Override
	public boolean stillValid(Player player) {
		return access.evaluate((level, pos) -> level.getBlockState(pos).is(SculkVoidModBlocks.SCULK_INVESTED_BARREL.get()), true);
	}

	/* ------------------------------------------------------------
	 * SHIFT-CLICK LOGIC
	 * ------------------------------------------------------------ */
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack originalStack = slot.getItem();
			stack = originalStack.copy();
			int blockInvSize = 6 * 9;
			int playerInvStart = blockInvSize;
			int playerInvEnd = blockInvSize + 3 * 9 + 9; // 3 rows + hotbar
			if (index < blockInvSize) {
				if (!this.moveItemStackTo(originalStack, playerInvStart, playerInvEnd, true))
					return ItemStack.EMPTY;
			} else {
				if (!this.moveItemStackTo(originalStack, 0, blockInvSize, false))
					return ItemStack.EMPTY;
			}
			if (originalStack.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
		}
		return stack;
	}

	/* ------------------------------------------------------------
	 * SUPPLIER OVERRIDE
	 * ------------------------------------------------------------ */
	@Override
	public Map<Integer, Slot> get() {
		return customSlots;
	}
}
