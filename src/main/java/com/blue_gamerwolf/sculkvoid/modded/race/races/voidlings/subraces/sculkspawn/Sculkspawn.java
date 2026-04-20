package com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.sculkspawn;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Sculkspawn implements SubRace {

    @Override
    public String id() {
        return "sculkspawn";
    }

    @Override
    public String description() {
        return "Connected to sculk vibrations and energy feeding.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.NIGHT_VISION, 220, 0);
            }

            @Override
            public void onKill(Player player, LivingEntity killed) {
                player.heal(2.0F);
            }
        });
    }
}
