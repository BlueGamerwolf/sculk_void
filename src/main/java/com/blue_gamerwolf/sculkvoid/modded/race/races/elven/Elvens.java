package com.blue_gamerwolf.sculkvoid.modded.race.races.elven;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.modded.race.races.elven.subraces.darkelves.DarkElves;
import com.blue_gamerwolf.sculkvoid.modded.race.races.elven.subraces.highelves.HighElves;
import com.blue_gamerwolf.sculkvoid.modded.race.races.elven.subraces.lightelves.LightElves;
import com.blue_gamerwolf.sculkvoid.modded.race.races.elven.subraces.woodelves.WoodElves;
import java.util.List;
import java.util.Set;

public class Elvens implements RaceModule {

    @Override
    public String id() {
        return "elven";
    }

    @Override
    public RaceCategory category() {
        return RaceCategory.EXTRA;
    }

    @Override
    public Set<String> aliases() {
        return Set.of("elves");
    }

    @Override
    public List<SubRace> getSubRaces() {
        return List.of(
                new DarkElves(),
                new HighElves(),
                new WoodElves(),
                new LightElves()
        );
    }
}
