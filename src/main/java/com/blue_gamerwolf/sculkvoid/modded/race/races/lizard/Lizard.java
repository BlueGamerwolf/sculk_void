package com.blue_gamerwolf.sculkvoid.modded.race.races.lizard;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.modded.race.races.lizard.subraces.bruni.Bruni;
import com.blue_gamerwolf.sculkvoid.modded.race.races.lizard.subraces.dragonborn.DragonBorn;

import java.util.Set;
import java.util.List;

public class Lizard implements RaceModule {

    @Override
    public String id() {
        return "lizard";
    }

    @Override
    public RaceCategory category() {
        return RaceCategory.EXTRA;
    }

    @Override
    public Set<String> aliases() {
        return Set.of("solar");
    }

    @Override
    public List<SubRace> getSubRaces() {
        return List.of(
                new Bruni(),
                new DragonBorn()
        );
    }
}
