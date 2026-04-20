package com.blue_gamerwolf.sculkvoid.api.race.api;

import java.util.List;
import java.util.Set;

public interface RaceModule {
    String id();
    RaceCategory category();
    List<SubRace> getSubRaces();

    default String displayName() {
        return formatName(id());
    }

    default String startingDimension() {
        return "minecraft:overworld";
    }

    default Set<String> aliases() {
        return Set.of();
    }

    static String formatName(String value) {
        String normalized = value.replace('_', ' ').trim();
        String[] parts = normalized.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                continue;
            }
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(parts[i].charAt(0)));
            if (parts[i].length() > 1) {
                builder.append(parts[i].substring(1).toLowerCase());
            }
        }
        return builder.toString();
    }
}
