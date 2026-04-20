package com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.mariner;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Mariner implements SubRace {

    @Override
    public String id() {
        return "mariner";
    }

    @Override
    public String description() {
        return "Sea-faring humans adapted to ocean life.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.WATER_BREATHING, 220, 0);
                if (player.isInWaterOrBubble()) {
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.DOLPHINS_GRACE, 120, 0);
                }
            }
        });
    }
}
