package com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.highlander;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Highlander implements SubRace {

    @Override
    public String id() {
        return "highlander";
    }

    @Override
    public String description() {
        return "Mountain adapted humans with high endurance.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.JUMP, 120, 0);
            }
        });
    }
}
