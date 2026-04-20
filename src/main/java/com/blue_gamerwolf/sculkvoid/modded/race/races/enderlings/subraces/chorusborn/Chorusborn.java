package com.blue_gamerwolf.sculkvoid.modded.race.races.enderlings.subraces.chorusborn;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Chorusborn implements SubRace {

    @Override
    public String id() {
        return "chorusborn";
    }

    @Override
    public String description() {
        return "Beings infused with chorus energy and instability.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.SLOW_FALLING, 120, 0);
            }

            @Override
            public void onHit(Player player, LivingEntity target) {
                if (player.getRandom().nextFloat() >= 0.25F) {
                    return;
                }
                double x = target.getX() + (player.getRandom().nextDouble() - 0.5D) * 6.0D;
                double y = target.getY();
                double z = target.getZ() + (player.getRandom().nextDouble() - 0.5D) * 6.0D;
                player.randomTeleport(x, y, z, true);
            }
        });
    }
}
