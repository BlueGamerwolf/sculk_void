package com.blue_gamerwolf.sculkvoid.modded.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID)
public class VoidInventoryHandler {

    private static final String TAG = "sculk_void_inventory";
    private static final String FLAG = "sculk_void_inside";

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player)) return;
        boolean inVoid = player.level().dimension().location().toString().equals("sculk_void:endless_void");
        CompoundTag data = player.getPersistentData();
        boolean wasInside = data.getBoolean(FLAG);
        if (inVoid && !wasInside) {
            ListTag inv = new ListTag();
            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack stack = player.getInventory().items.get(i);
                CompoundTag tag = new CompoundTag();
                stack.save(tag);
                tag.putInt("Slot", i);
                inv.add(tag);
            }
            data.put(TAG, inv);
            player.getInventory().clearContent();
            data.putBoolean(FLAG, true);
        }
        if (!inVoid && wasInside) {
            if (data.contains(TAG)) {
                ListTag inv = data.getList(TAG, 10);
                for (int i = 0; i < inv.size(); i++) {
                    CompoundTag tag = inv.getCompound(i);
                    int slot = tag.getInt("Slot");
                    player.getInventory().items.set(slot, ItemStack.of(tag));
                }
                data.remove(TAG);
            }
            data.putBoolean(FLAG, false);
        }
    }
}