package com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceCategory;
import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.chorusborn.Chorusborn;
import com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.dragonkin.Dragonkin;
import com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.enderjockey.EnderJockey;
import com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.shulkblood.Shulkblood;
import com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.voidwalker.VoidWalker;

import java.util.List;

public class Enderlings implements RaceModule {

    @Override
    public String id() {
        return "enderlings";
    }

    @Override
    public RaceCategory category() {
        return RaceCategory.STORY;
    }

    @Override
    public String startingDimension() {
        return "minecraft:the_end";
    }

    @Override
    public List<SubRace> getSubRaces() {
        return List.of(
                new Chorusborn(),
                new Dragonkin(),
                new EnderJockey(),
                new Shulkblood(),
                new VoidWalker()
        );
    }
}
