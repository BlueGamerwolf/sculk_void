package com.blue_gamerwolf.sculkvoid.modded.race.init;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import com.blue_gamerwolf.sculkvoid.api.race.races.registry.RaceRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.TagRaceResourceProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.VanillaExperienceProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.blood.BloodProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.darkness.DarknessProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.hell.HellProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.solar.SolarProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.vp.VoidProvider;
import com.blue_gamerwolf.sculkvoid.modded.race.races.elven.Elvens;
import com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.Enderlings;
import com.blue_gamerwolf.sculkvoid.modded.race.races.humans.Humans;
import com.blue_gamerwolf.sculkvoid.modded.race.races.lizard.Lizard;
import com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.Netherlings;
import com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.Voidlings;
import com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.Wolfs;

public final class BuiltInRaceBootstrap {

    private static boolean initialized;

    private BuiltInRaceBootstrap() {
    }

    public static void init() {
        if (initialized) {
            return;
        }

        initialized = true;

        RaceRegistry.register(new Humans());
        RaceRegistry.register(new Elvens());
        RaceRegistry.register(new Enderlings());
        RaceRegistry.register(new Lizard());
        RaceRegistry.register(new Netherlings());
        RaceRegistry.register(new Voidlings());
        RaceRegistry.register(new Wolfs());

        RaceResourceRegistry.register("humans", new VanillaExperienceProvider());
        RaceResourceRegistry.register("human", new VanillaExperienceProvider());
        RaceResourceRegistry.register("elven", new TagRaceResourceProvider(
                "sculkvoid_spiritual",
                "spiritual_points",
                "max_spiritual_points",
                100
        ));
        RaceResourceRegistry.register("elves", new TagRaceResourceProvider(
                "sculkvoid_spiritual",
                "spiritual_points",
                "max_spiritual_points",
                100
        ));
        RaceResourceRegistry.register("spiritual", new TagRaceResourceProvider(
                "sculkvoid_spiritual",
                "spiritual_points",
                "max_spiritual_points",
                100
        ));
        RaceResourceRegistry.register("enderlings", new DarknessProvider());
        RaceResourceRegistry.register("lizard", new SolarProvider());
        RaceResourceRegistry.register("lizards", new SolarProvider());
        RaceResourceRegistry.register("solar", new SolarProvider());
        RaceResourceRegistry.register("netherlings", new HellProvider());
        RaceResourceRegistry.register("hell", new HellProvider());
        RaceResourceRegistry.register("voidlings", new VoidProvider());
        RaceResourceRegistry.register("sculklings", new VoidProvider());
        RaceResourceRegistry.register("sculk", new VoidProvider());
        RaceResourceRegistry.register("wolfs", new BloodProvider());
        RaceResourceRegistry.register("blood", new BloodProvider());

        SculkVoidMod.LOGGER.info("Registered {} races and {} race resources.",
                RaceRegistry.getAll().size(),
                RaceResourceRegistry.getAll().size());
    }
}
