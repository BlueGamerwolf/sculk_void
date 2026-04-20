package com.blue_gamerwolf.sculkvoid.modded.race.data.resources;

import java.util.HashMap;
import java.util.Map;

public class RaceResourceRegistry {

    private static final Map<String, RaceResourceProvider> RESOURCES = new HashMap<>();

    public static void register(String raceId, RaceResourceProvider provider) {
        if (RESOURCES.containsKey(raceId)) {
            throw new IllegalStateException("Duplicate race resource registration for id: " + raceId);
        }
        RESOURCES.put(raceId, provider);
    }

    public static RaceResourceProvider get(String raceId) {
        return RESOURCES.get(raceId);
    }

    public static Map<String, RaceResourceProvider> getAll() {
        return Map.copyOf(RESOURCES);
    }
}
