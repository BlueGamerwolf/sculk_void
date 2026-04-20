package com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.direwolf;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class DireWolf implements SubRace {

    @Override
    public String id() {
        return "dire_wolf";
    }

    @Override
    public String description() {
        return "Massive and powerful wolves with overwhelming strength.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_BOOST, 120, 1);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 120, 0);
            }
        });
    }
}
