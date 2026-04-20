package com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.abyssal;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class Abyssal implements SubRace {

    @Override
    public String id() {
        return "abyssal";
    }

    @Override
    public String description() {
        return "Deep void dwellers strengthened by darkness.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(net.minecraft.world.entity.player.Player player) {
                if (SubRaceAbilityUtil.isInDarkness(player)) {
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.NIGHT_VISION, 220, 0);
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_BOOST, 120, 0);
                }
            }
        });
    }
}
