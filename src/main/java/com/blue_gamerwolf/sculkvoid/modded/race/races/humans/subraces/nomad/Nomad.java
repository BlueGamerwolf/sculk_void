package com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.nomad;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Nomad implements SubRace {

    @Override
    public String id() {
        return "nomad";
    }

    @Override
    public String description() {
        return "Wandering tribes adapted to all environments.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 120, 0);
                if (player.isSprinting()) {
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 60, 0);
                }
            }
        });
    }
}
