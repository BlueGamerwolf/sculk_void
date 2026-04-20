package com.blue_gamerwolf.sculkvoid.modded.race.races.humans.subraces.witch;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Witch implements SubRace {

    @Override
    public String id() {
        return "witch";
    }

    @Override
    public String description() {
        return "Practitioners of alchemy, curses, and forbidden magic.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                player.removeEffect(MobEffects.POISON);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.LUCK, 220, 0);
            }

            @Override
            public void onHit(Player player, LivingEntity target) {
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0));
            }
        });
    }
}
