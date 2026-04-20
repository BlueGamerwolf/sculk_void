package com.blue_gamerwolf.sculkvoid.modded.race.races.elven.subraces.woodelves;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class WoodElves implements SubRace {

    @Override
    public String id() {
        return "wood_elves";
    }

    @Override
    public String description() {
        return "Nature-attuned elves who excel in forests, movement, and survival.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(
                new SubRaceAbility() {
                    @Override
                    public void tick(net.minecraft.world.entity.player.Player player) {
                        if (SubRaceAbilityUtil.isNaturalFooting(player)) {
                            SubRaceAbilityUtil.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 120, 0);
                            SubRaceAbilityUtil.refreshEffect(player, MobEffects.DIG_SPEED, 120, 0);
                        }
                    }
                }
        );
    }
}
