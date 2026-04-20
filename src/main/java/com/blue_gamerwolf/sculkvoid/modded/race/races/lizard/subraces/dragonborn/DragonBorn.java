package com.blue_gamerwolf.sculkvoid.modded.race.races.lizard.subraces.dragonborn;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class DragonBorn implements SubRace {

    @Override
    public String id() {
        return "dragonborn";
    }

    @Override
    public String description() {
        return "Reptilian descendants infused with dragon essence.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_BOOST, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 120, 0);
            }
        });
    }
}
