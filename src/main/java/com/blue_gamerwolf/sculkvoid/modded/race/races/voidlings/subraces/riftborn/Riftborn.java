package com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.riftborn;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Riftborn implements SubRace {

    @Override
    public String id() {
        return "riftborn";
    }

    @Override
    public String description() {
        return "Born from unstable dimensional rifts.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.SLOW_FALLING, 120, 0);
            }

            @Override
            public void onHit(Player player, LivingEntity target) {
                if (player.getRandom().nextFloat() < 0.2F) {
                    target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0));
                }
            }
        });
    }
}
