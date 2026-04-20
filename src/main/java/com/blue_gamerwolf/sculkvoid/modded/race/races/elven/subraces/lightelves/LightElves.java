package com.blue_gamerwolf.sculkvoid.modded.race.races.elven.subraces.lightelves;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class LightElves implements SubRace {

    @Override
    public String id() {
        return "light_elf";
    }

    @Override
    public String description() {
        return "Blessed elves aligned with light energy, excelling in healing and purification.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(
                new SubRaceAbility() {
                    @Override
                    public void tick(net.minecraft.world.entity.player.Player player) {
                        if (player.level().isDay() && SubRaceAbilityUtil.canSeeSky(player)) {
                            SubRaceAbilityUtil.refreshEffect(player, MobEffects.REGENERATION, 120, 0);
                        }
                        player.removeEffect(MobEffects.BLINDNESS);
                        player.removeEffect(MobEffects.DARKNESS);
                    }
                }
        );
    }
}
