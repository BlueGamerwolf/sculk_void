package com.blue_gamerwolf.sculkvoid.modded.race;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.races.registry.RaceRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.data.storage.RaceData;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class RaceManager {

    public static RaceModule getRace(Player player) {
        return RaceData.hasRace(player) ? RaceRegistry.get(RaceData.getRaceId(player)) : null;
    }

    public static List<SubRace> getSubRaces(Player player) {
        RaceModule module = getRace(player);
        return module == null ? List.of() : module.getSubRaces();
    }

    public static SubRace getSubRace(Player player) {
        String subId = RaceData.getSubRaceId(player);
        for (SubRace sub : getSubRaces(player)) {
            if (sub.id().equals(subId)) {
                return sub;
            }
        }
        return null;
    }

    public static List<RaceModule> getRacesByCategory(RaceCategory category) {
        return RaceRegistry.getAll().values().stream()
                .filter(module -> module.category() == category)
                .toList();
    }
}
