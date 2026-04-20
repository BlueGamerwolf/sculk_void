package com.blue_gamerwolf.sculkvoid.api.race.races.registry;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

public class RaceRegistry {

    private static final Map<String, RaceModule> RACES = new LinkedHashMap<>();
    private static final Map<String, RaceModule> LOOKUP = new HashMap<>();

    public static void register(RaceModule module) {
        if (RACES.containsKey(module.id())) {
            throw new IllegalStateException("Duplicate race registration for id: " + module.id());
        }
        RACES.put(module.id(), module);
        registerLookup(module.id(), module);
        for (String alias : module.aliases()) {
            registerLookup(alias, module);
        }
        System.out.println("[RaceRegistry] Registered race: " + module.id());
    }

    private static void registerLookup(String key, RaceModule module) {
        if (LOOKUP.containsKey(key)) {
            throw new IllegalStateException("Duplicate race lookup registration for id: " + key);
        }
        LOOKUP.put(key, module);
    }

    public static RaceModule get(String id) {
        return LOOKUP.get(id);
    }

    public static Map<String, RaceModule> getAll() {
        return Map.copyOf(RACES);
    }
}
