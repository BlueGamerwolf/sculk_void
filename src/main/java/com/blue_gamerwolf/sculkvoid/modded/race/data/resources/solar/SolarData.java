package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.solar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class SolarData {

    private static final String ROOT = "sculkvoid_solar";
    private static final String SP = "solar_points";
    private static final String MAX_SP = "max_solar_points";

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

    public static int getSP(Player player) {
        return getData(player).getInt(SP);
    }

    public static int getMaxSP(Player player) {
        CompoundTag data = getData(player);
        if (!data.contains(MAX_SP)) return 100;
        return data.getInt(MAX_SP);
    }

    public static void setSP(Player player, int value) {
        int max = getMaxSP(player);
        int clamped = Math.max(0, Math.min(value, max));
        getData(player).putInt(SP, clamped);
    }

    public static void setMaxSP(Player player, int value) {
        getData(player).putInt(MAX_SP, Math.max(1, value));
    }

    public static void addSP(Player player, int amount) {
        setSP(player, getSP(player) + amount);
    }

    public static void consumeSP(Player player, int amount) {
        setSP(player, getSP(player) - amount);
    }

    public static boolean hasEnough(Player player, int amount) {
        return getSP(player) >= amount;
    }
}