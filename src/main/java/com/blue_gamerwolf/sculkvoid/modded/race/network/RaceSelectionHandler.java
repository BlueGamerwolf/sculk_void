package com.blue_gamerwolf.sculkvoid.modded.race.network;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.races.registry.RaceRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.data.storage.RaceData;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RaceSelectionHandler {

    private RaceSelectionHandler() {
    }

    public static boolean assignRace(ServerPlayer player, String raceId, String subRaceId) {
        RaceModule module = RaceRegistry.get(raceId);
        if (module == null) {
            return false;
        }

        SubRace selectedSubRace = module.getSubRaces().stream()
                .filter(subRace -> subRace.id().equals(subRaceId))
                .findFirst()
                .orElse(null);

        if (selectedSubRace == null || !selectedSubRace.isUnlockedFor(player)) {
            return false;
        }

        RaceData.setRace(player, module.id());
        RaceData.setSubRace(player, selectedSubRace.id());
        return true;
    }

    public static boolean assignRandomRace(ServerPlayer player) {
        List<RaceChoice> choices = new ArrayList<>();
        for (RaceModule module : RaceRegistry.getAll().values().stream()
                .sorted(Comparator.comparing(RaceModule::id))
                .toList()) {
            for (SubRace subRace : module.getSubRaces()) {
                if (subRace.isUnlockedFor(player)) {
                    choices.add(new RaceChoice(module.id(), subRace.id()));
                }
            }
        }

        if (choices.isEmpty()) {
            return false;
        }

        RaceChoice selected = choices.get(player.getRandom().nextInt(choices.size()));
        return assignRace(player, selected.raceId(), selected.subRaceId());
    }

    private record RaceChoice(String raceId, String subRaceId) {
    }
}
