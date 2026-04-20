package com.blue_gamerwolf.sculkvoid.modded.race.races.humans;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.highlander.Highlander;
import com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.mariner.Mariner;
import com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.nomad.Nomad;
import com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.wardensworm.Wardensworn;
import com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.witch.Witch;

import java.util.List;
import java.util.Set;

public class Humans implements RaceModule {

    @Override
    public String id() {
        return "humans";
    }

    @Override
    public RaceCategory category() {
        return RaceCategory.STORY;
    }

    @Override
    public Set<String> aliases() {
        return Set.of("human");
    }

    @Override
    public List<SubRace> getSubRaces() {
        return List.of(
                new Highlander(),
                new Mariner(),
                new Nomad(),
                new Wardensworn(),
                new Witch()
        );
    }
}
