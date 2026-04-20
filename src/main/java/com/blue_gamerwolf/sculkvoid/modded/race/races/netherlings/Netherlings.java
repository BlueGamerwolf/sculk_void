package com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.subraces.ashborn.Ashborn;
import com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.subraces.blazeblood.Blazeblood;
import com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.subraces.piglinborn.Piglinborn;
import com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.subraces.witherkin.Witherkin;

import java.util.List;

public class Netherlings implements RaceModule {

    @Override
    public String id() {
        return "netherlings";
    }

    @Override
    public RaceCategory category() {
        return RaceCategory.STORY;
    }

    @Override
    public String startingDimension() {
        return "minecraft:the_nether";
    }

    @Override
    public java.util.Set<String> aliases() {
        return java.util.Set.of("hell");
    }

    @Override
    public List<SubRace> getSubRaces() {
        return List.of(
                new Ashborn(),
                new Blazeblood(),
                new Piglinborn(),
                new Witherkin()
        );
    }
}
