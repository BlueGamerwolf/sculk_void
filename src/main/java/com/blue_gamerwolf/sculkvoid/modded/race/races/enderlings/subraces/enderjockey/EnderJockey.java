package com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.enderjockey;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class EnderJockey implements SubRace {

    @Override
    public String id() {
        return "enderjockey";
    }

    @Override
    public String description() {
        return "Agile riders bonded with End creatures, highly unpredictable in combat.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.MOVEMENT_SPEED, 120, 0);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.JUMP, 120, 0);
            }

            @Override
            public void onKill(Player player, LivingEntity killed) {
                player.heal(1.0F);
            }
        });
    }
}
