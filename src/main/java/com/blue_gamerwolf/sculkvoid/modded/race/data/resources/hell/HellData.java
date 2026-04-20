package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.hell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class HellData {

    private static final String ROOT = "sculkvoid_hell";
    private static final String HP = "hell_points";
    private static final String MAX_HP = "max_hell_points";

    private static CompoundTag getData(Player player) {

        CompoundTag persistent = player.getPersistentData();

        if (!persistent.contains("ForgeData")) {
            persistent.put("ForgeData", new CompoundTag());
        }

        CompoundTag forgeData = persistent.getCompound("ForgeData");

        if (!forgeData.contains(ROOT)) {
            forgeData.put(ROOT, new CompoundTag());
        }

        return forgeData.getCompound(ROOT);
    }

    public static int getHell(Player player) {
        return getData(player).getInt(HP);
    }

    public static int getMaxHell(Player player) {
        CompoundTag data = getData(player);
        if (!data.contains(MAX_HP)) return 100; // default
        return data.getInt(MAX_HP);
    }

    public static void setHell(Player player, int value) {
        int max = getMaxHell(player);
        int clamped = Math.max(0, Math.min(value, max));
        getData(player).putInt(HP, clamped);
    }

    public static void setMaxHell(Player player, int value) {
        getData(player).putInt(MAX_HP, Math.max(1, value));
    }

    public static void addHell(Player player, int amount) {
        setHell(player, getHell(player) + amount);
    }

    public static void consumeHell(Player player, int amount) {
        setHell(player, getHell(player) - amount);
    }

    public static boolean hasEnough(Player player, int amount) {
        return getHell(player) >= amount;
    }
}