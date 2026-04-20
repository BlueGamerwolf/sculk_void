package com.blue_gamerwolf.sculkvoid.modded.race.races.elven.subraces.highelves;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class HighElves implements SubRace {

    @Override
    public String id() {
        return "high_elves";
    }

    @Override
    public String description() {
        return "Magical, aristocratic beings who follow the divine order.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(
                new SubRaceAbility() {
                    @Override
                    public void tick(net.minecraft.world.entity.player.Player player) {
                        SubRaceAbilityUtil.refreshEffect(player, MobEffects.LUCK, 220, 0);
                        if (player.level().isDay() && SubRaceAbilityUtil.canSeeSky(player)) {
                            SubRaceAbilityUtil.refreshEffect(player, MobEffects.REGENERATION, 80, 0);
                        }
                    }
                }
        );
    }
}
