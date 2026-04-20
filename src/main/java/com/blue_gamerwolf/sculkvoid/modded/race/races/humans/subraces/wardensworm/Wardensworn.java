package com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.wardensworm;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Wardensworn implements SubRace {

    @Override
    public String id() {
        return "wardensworn";
    }

    @Override
    public String description() {
        return "Humans bound to ancient warden-like forces.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                if (SubRaceAbilityUtil.isInDarkness(player)) {
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.NIGHT_VISION, 220, 0);
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 120, 0);
                }
            }

            @Override
            public void onHit(Player player, LivingEntity target) {
                target.addEffect(new net.minecraft.world.effect.MobEffectInstance(MobEffects.DARKNESS, 60, 0));
            }
        });
    }
}
