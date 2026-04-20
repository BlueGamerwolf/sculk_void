package com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.voidwalker;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class VoidWalker implements SubRace {

    @Override
    public String id() {
        return "voidwalker";
    }

    @Override
    public String description() {
        return "Masters of dimensional void traversal.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.NIGHT_VISION, 220, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.SLOW_FALLING, 120, 0);
                if (player.isCrouching()) {
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.INVISIBILITY, 40, 0);
                }
            }
        });
    }
}
