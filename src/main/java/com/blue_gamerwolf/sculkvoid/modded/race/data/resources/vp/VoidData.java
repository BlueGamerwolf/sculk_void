package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.vp;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class VoidData {

    private static final String ROOT = "sculkvoid_void";
    private static final String VP = "void_points";
    private static final String MAX_VP = "max_void_points";

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

    public static int getVP(Player player) {
        return getData(player).getInt(VP);
    }

    public static int getMaxVP(Player player) {
        CompoundTag data = getData(player);
        if (!data.contains(MAX_VP)) return 100;
        return data.getInt(MAX_VP);
    }

    public static void setVP(Player player, int value) {
        getData(player).putInt(VP, Math.max(0, value));
    }

    public static void addVP(Player player, int amount) {
        int current = getVP(player);
        int max = getMaxVP(player);
        setVP(player, Math.min(current + amount, max));
    }

    public static void consumeVP(Player player, int amount) {
        setVP(player, getVP(player) - amount);
    }
}