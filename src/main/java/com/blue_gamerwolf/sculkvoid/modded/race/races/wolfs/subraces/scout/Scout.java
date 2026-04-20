package com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.scout;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Scout implements SubRace {

    @Override
    public String id() {
        return "scout";
    }

    @Override
    public String description() {
        return "Fast and agile reconnaissance specialists.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 120, 1);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.JUMP, 120, 0);
            }
        });
    }
}
