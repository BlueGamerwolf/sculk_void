package com.blue_gamerwolf.sculkvoid.modded.race.data.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class RaceData {

    private static final String ROOT = "sculkvoid_race";
    private static final String RACE = "race";
    private static final String SUBRACE = "subrace";
    private static final String LEGACY_MAIN_RACE = "main_race";
    private static final String LEGACY_MAIN_SUBRACE = "main_subrace";

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
    public static String getRaceId(Player player) {
        CompoundTag data = getData(player);
        if (data.contains(RACE)) {
            return data.getString(RACE);
        }
        if (data.contains(LEGACY_MAIN_RACE)) {
            return data.getString(LEGACY_MAIN_RACE);
        }
        return "";
    }
    public static void setRace(Player player, String raceId) {
        CompoundTag data = getData(player);
        data.putString(RACE, raceId);
        data.remove(SUBRACE);
        data.remove(LEGACY_MAIN_RACE);
        data.remove(LEGACY_MAIN_SUBRACE);
    }

    public static boolean hasRace(Player player) {
        CompoundTag data = getData(player);
        return data.contains(RACE) || data.contains(LEGACY_MAIN_RACE);
    }

    public static String getSubRaceId(Player player) {
        CompoundTag data = getData(player);
        if (data.contains(SUBRACE)) {
            return data.getString(SUBRACE);
        }
        if (data.contains(LEGACY_MAIN_SUBRACE)) {
            return data.getString(LEGACY_MAIN_SUBRACE);
        }
        return "";
    }

    public static void setSubRace(Player player, String subRaceId) {
        CompoundTag data = getData(player);
        data.putString(SUBRACE, subRaceId);
        data.remove(LEGACY_MAIN_SUBRACE);
    }

    public static boolean hasSubRace(Player player) {
        CompoundTag data = getData(player);
        return data.contains(SUBRACE) || data.contains(LEGACY_MAIN_SUBRACE);
    }

    public static boolean isRace(Player player, String raceId) {
        return raceId.equals(getRaceId(player));
    }

    public static boolean isSubRace(Player player, String subRaceId) {
        return subRaceId.equals(getSubRaceId(player));
    }

    public static void clear(Player player) {
        CompoundTag data = getData(player);
        data.remove(RACE);
        data.remove(SUBRACE);
        data.remove(LEGACY_MAIN_RACE);
        data.remove(LEGACY_MAIN_SUBRACE);
    }
}
