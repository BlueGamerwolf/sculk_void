package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.blood;

import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class BloodData implements RaceResourceProvider {

    private static final String ROOT = "sculkvoid_blood";
    private static final String BP = "blood_points";
    private static final String MAX_BP = "max_blood_points";

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

    public static int getBP(Player player) {
        return getData(player).getInt(BP);
    }

    public static int getMaxBP(Player player) {
        CompoundTag data = getData(player);
        if (!data.contains(MAX_BP)) return 100;
        return data.getInt(MAX_BP);
    }

    public static void setBP(Player player, int value) {
        getData(player).putInt(BP, Math.max(0, value));
    }

    public static void addBP(Player player, int amount) {
        int current = getBP(player);
        int max = getMaxBP(player);
        setBP(player, Math.min(current + amount, max));
    }

    public static void consumeBP(Player player, int amount) {
        setBP(player, getBP(player) - amount);
    }

    @Override
    public int get(Player player) {
        return getBP(player);
    }

    @Override
    public int getMax(Player player) {
        return getMaxBP(player);
    }

    @Override
    public void set(Player player, int value) {
        setBP(player, value);
    }

    @Override
    public void add(Player player, int amount) {
        addBP(player, amount);
    }

    @Override
    public void consume(Player player, int amount) {
        consumeBP(player, amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return getBP(player) >= amount;
    }
}
