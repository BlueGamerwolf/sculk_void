package com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.subraces.ashborn;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Ashborn implements SubRace {

    @Override
    public String id() {
        return "ashborn";
    }

    @Override
    public String description() {
        return "Born from volcanic ash, resistant to extreme heat and fire.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.FIRE_RESISTANCE, 220, 0);
                if (player.getRemainingFireTicks() > 0 && player.tickCount % 20 == 0) {
                    player.heal(0.5F);
                }
            }
        });
    }
}
