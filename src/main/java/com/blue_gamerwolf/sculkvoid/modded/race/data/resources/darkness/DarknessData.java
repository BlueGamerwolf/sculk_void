package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.darkness;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class DarknessData {

    private static final String ROOT = "sculkvoid_darkness";
    private static final String DP = "darkness_points";
    private static final String MAX_DP = "max_darkness_points";

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

    public static int getDarkness(Player player) {
        return getData(player).getInt(DP);
    }

    public static int getMaxDarkness(Player player) {
        CompoundTag data = getData(player);
        if (!data.contains(MAX_DP)) return 100; // default max
        return data.getInt(MAX_DP);
    }

    public static void setDarkness(Player player, int value) {
        int max = getMaxDarkness(player);
        int clamped = Math.max(0, Math.min(value, max));
        getData(player).putInt(DP, clamped);
    }

    public static void setMaxDarkness(Player player, int value) {
        getData(player).putInt(MAX_DP, Math.max(1, value));
    }

    public static void addDarkness(Player player, int amount) {
        setDarkness(player, getDarkness(player) + amount);
    }

    public static void consumeDarkness(Player player, int amount) {
        setDarkness(player, getDarkness(player) - amount);
    }

    public static boolean hasEnough(Player player, int amount) {
        return getDarkness(player) >= amount;
    }
}