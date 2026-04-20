package com.blue_gamerwolf.sculkvoid.modded.race.races.wolfs.subraces.hunter;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Hunter implements SubRace {

    @Override
    public String id() {
        return "hunter";
    }

    @Override
    public String description() {
        return "Experts in tracking and dealing high targeted damage.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 120, 0);
            }

            @Override
            public void onHit(Player player, LivingEntity target) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
                target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0));
            }
        });
    }
}
