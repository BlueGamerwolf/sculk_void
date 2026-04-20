package com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.subraces.piglinborn;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Piglinborn implements SubRace {

    @Override
    public String id() {
        return "piglinborn";
    }

    @Override
    public String description() {
        return "Descendants of Piglins, skilled in survival and barter instincts.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DIG_SPEED, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.LUCK, 220, 0);
            }
        });
    }
}
