package com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.abyssal.Abyssal;
import com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.phoenix.Phoenix;
import com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.riftborn.Riftborn;
import com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.sculkspawn.Sculkspawn;
import com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.tsukisora.Tsukisora;
import com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.vampire.Vampire;

import java.util.List;
import java.util.Set;

public class Voidlings implements RaceModule {

    @Override
    public String id() {
        return "voidlings";
    }

    @Override
    public RaceCategory category() {
        return RaceCategory.STORY;
    }

    @Override
    public Set<String> aliases() {
        return Set.of("sculk", "sculklings");
    }

    @Override
    public List<SubRace> getSubRaces() {
        return List.of(
                new Abyssal(),
                new Phoenix(),
                new Riftborn(),
                new Sculkspawn(),
                new Tsukisora(),
                new Vampire()
        );
    }
}
