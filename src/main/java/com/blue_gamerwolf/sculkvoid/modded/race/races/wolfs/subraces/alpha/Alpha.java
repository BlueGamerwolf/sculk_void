package com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.alpha;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Alpha implements SubRace {

    @Override
    public String id() {
        return "alpha";
    }

    @Override
    public String description() {
        return "Pack leaders with dominance over allies and enemies.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_BOOST, 120, 0);
            }
        });
    }
}
