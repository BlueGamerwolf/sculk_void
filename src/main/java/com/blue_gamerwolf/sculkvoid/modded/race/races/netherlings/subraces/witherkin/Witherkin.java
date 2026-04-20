package com.blue_gamerwolf.sculkvoid.modded.race.races.netherlings.subraces.witherkin;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Witherkin implements SubRace {

    @Override
    public String id() {
        return "witherkin";
    }

    @Override
    public String description() {
        return "Corrupted beings infused with wither energy and decay resistance.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                player.removeEffect(MobEffects.WITHER);
            }

            @Override
            public void onHit(Player player, LivingEntity target) {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 0));
            }
        });
    }
}
