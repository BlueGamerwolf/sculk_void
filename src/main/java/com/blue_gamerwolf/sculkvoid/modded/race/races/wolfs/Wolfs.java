package com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.alpha.Alpha;
import com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.beta.Beta;
import com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.direwolf.DireWolf;
import com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.guardian.Guardian;
import com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.hunter.Hunter;
import com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.scout.Scout;

import java.util.List;
import java.util.Set;

public class Wolfs implements RaceModule {

    @Override
    public String id() {
        return "wolfs";
    }

    @Override
    public RaceCategory category() {
        return RaceCategory.EXTRA;
    }

    @Override
    public Set<String> aliases() {
        return Set.of("blood");
    }

    @Override
    public List<SubRace> getSubRaces() {
        return List.of(
                new Alpha(),
                new Beta(),
                new DireWolf(),
                new Guardian(),
                new Hunter(),
                new Scout()
        );
    }
}
