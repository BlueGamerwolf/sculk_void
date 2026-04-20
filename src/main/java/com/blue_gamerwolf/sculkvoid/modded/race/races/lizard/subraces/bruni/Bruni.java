package com.blue_gamerwolf.sculkvoid.modded.race.races.lizard.subraces.bruni;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Bruni implements SubRace {

    @Override
    public String id() {
        return "bruni";
    }

    @Override
    public String description() {
        return "Lizardfolk adapted to volcanic environments and heat resistance.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.FIRE_RESISTANCE, 220, 0);
            }

            @Override
            public void onHit(Player player, LivingEntity target) {
                target.setSecondsOnFire(4);
            }
        });
    }
}
