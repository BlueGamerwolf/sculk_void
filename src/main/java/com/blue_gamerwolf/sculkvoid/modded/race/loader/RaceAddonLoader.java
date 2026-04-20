package com.blue_gamerwolf.sculkvoid.modded.race.loader;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceAddon;

import java.util.ArrayList;
import java.util.List;

public class RaceAddonLoader {

    private static final List<RaceAddon> ADDONS = new ArrayList<>();

    public static void register(RaceAddon addon) {
        ADDONS.add(addon);
    }

    public static void init() {
        for (RaceAddon addon : ADDONS) {
            addon.register();
        }
    }
}