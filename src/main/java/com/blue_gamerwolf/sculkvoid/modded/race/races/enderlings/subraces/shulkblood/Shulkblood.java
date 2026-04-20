package com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.shulkblood;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Shulkblood implements SubRace {

    @Override
    public String id() {
        return "shulkblood";
    }

    @Override
    public String description() {
        return "Infused with Shulker essence, granting natural armor and resilience.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.SLOW_FALLING, 120, 0);
            }
        });
    }
}
