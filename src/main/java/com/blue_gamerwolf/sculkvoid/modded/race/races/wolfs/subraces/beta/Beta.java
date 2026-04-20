package com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.beta;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Beta implements SubRace {

    @Override
    public String id() {
        return "beta";
    }

    @Override
    public String description() {
        return "Balanced fighters that support the pack structure.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 120, 0);
            }
        });
    }
}
