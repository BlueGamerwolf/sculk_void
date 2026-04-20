package com.blue_gamerwolf.sculkvoid.modded.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

import com.blue_gamerwolf.sculkvoid.modded.world.inventory.SculkInvestedbarrelGUIMenu;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModBlockEntities;

public class SculkInvestedBarrelBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    // Storage
    private NonNullList<ItemStack> stacks = NonNullList.withSize(54, ItemStack.EMPTY);
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.UP));

    // Energy & Fluid
    private final EnergyStorage energyStorage = new EnergyStorage(1_000_000, 10_000, 20_000, 0) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
            }
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
            }
            return extracted;
        }
    };

    private final FluidTank fluidTank = new FluidTank(24_000, f -> f.getFluid() == net.minecraft.world.level.material.Fluids.WATER || f.getFluid() == net.minecraft.world.level.material.Fluids.LAVA) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
        }
    };

    public SculkInvestedBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(SculkVoidModBlockEntities.SCULK_INVESTED_BARREL.get(), pos, state);
    }

    // ------------------------------
    // NBT Save / Load
    // ------------------------------
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (!this.tryLoadLootTable(tag))
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.stacks);

        if (tag.get("energyStorage") instanceof IntTag intTag)
            energyStorage.deserializeNBT(intTag);
        if (tag.get("fluidTank") instanceof CompoundTag compoundTag)
            fluidTank.readFromNBT(compoundTag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag))
            ContainerHelper.saveAllItems(tag, this.stacks);

        tag.put("energyStorage", energyStorage.serializeNBT());
        tag.put("fluidTank", fluidTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    // ------------------------------
    // Inventory
    // ------------------------------
    @Override
    public int getContainerSize() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : stacks)
            if (!stack.isEmpty()) return false;
        return true;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    // ------------------------------
    // GUI
    // ------------------------------
    @Override
    public Component getDefaultName() {
        return Component.literal("sculk_invested_barrel");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new SculkInvestedbarrelGUIMenu(id, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Sculk Invested Barrel");
    }

    // ------------------------------
    // Capabilities
    // ------------------------------
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!remove && cap == ForgeCapabilities.ITEM_HANDLER)
            return itemHandler.cast();
        if (!remove && cap == ForgeCapabilities.ENERGY)
            return LazyOptional.of(() -> energyStorage).cast();
        if (!remove && cap == ForgeCapabilities.FLUID_HANDLER)
            return LazyOptional.of(() -> fluidTank).cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        itemHandler.invalidate();
    }
}
