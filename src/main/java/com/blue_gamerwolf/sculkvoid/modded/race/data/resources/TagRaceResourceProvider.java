package com.blue_gamerwolf.sculkvoid.modded.race.data.resources;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class TagRaceResourceProvider implements RaceResourceProvider {

    private final String root;
    private final String valueKey;
    private final String maxKey;
    private final int defaultMax;

    public TagRaceResourceProvider(String root, String valueKey, String maxKey, int defaultMax) {
        this.root = root;
        this.valueKey = valueKey;
        this.maxKey = maxKey;
        this.defaultMax = defaultMax;
    }

    private CompoundTag getData(Player player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains("ForgeData")) {
            persistent.put("ForgeData", new CompoundTag());
        }

        CompoundTag forgeData = persistent.getCompound("ForgeData");
        if (!forgeData.contains(root)) {
            forgeData.put(root, new CompoundTag());
        }

        return forgeData.getCompound(root);
    }

    @Override
    public int get(Player player) {
        return getData(player).getInt(valueKey);
    }

    @Override
    public int getMax(Player player) {
        CompoundTag data = getData(player);
        if (!data.contains(maxKey)) {
            return defaultMax;
        }
        return Math.max(1, data.getInt(maxKey));
    }

    @Override
    public void set(Player player, int value) {
        int clamped = Math.max(0, Math.min(value, getMax(player)));
        getData(player).putInt(valueKey, clamped);
    }

    @Override
    public void add(Player player, int amount) {
        set(player, get(player) + amount);
    }

    @Override
    public void consume(Player player, int amount) {
        set(player, get(player) - amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return get(player) >= amount;
    }

    public void setMax(Player player, int value) {
        getData(player).putInt(maxKey, Math.max(1, value));
        set(player, get(player));
    }
}
