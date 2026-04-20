package com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.dragonkin;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Dragonkin implements SubRace {

    @Override
    public String id() {
        return "dragonkinn";
    }

    @Override
    public String description() {
        return "Descendants of the Ender Dragon with immense power.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_BOOST, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.FIRE_RESISTANCE, 220, 0);
            }
        });
    }
}
