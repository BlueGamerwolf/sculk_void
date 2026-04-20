package com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.tsukisora;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.api.uuid.UUIDRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.SubRaceAbilityUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Tsukisora implements SubRace {

    @Override
    public String id() {
        return "tsukisora";
    }

    @Override
    public String description() {
        return "Void sovereign growing stronger through domination.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return List.of(new SubRaceAbility() {
            @Override
            public void tick(Player player) {
                if (player.getHealth() <= player.getMaxHealth() * 0.5F) {
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_BOOST, 120, 0);
                    SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 120, 0);
                }
            }

            @Override
            public void onKill(Player player, LivingEntity killed) {
                player.heal(1.0F);
                SubRaceAbilityUtil.refreshEffect(player, MobEffects.DAMAGE_BOOST, 120, 1);
            }
        });
    }

    @Override
    public Set<UUID> requiredPlayerUuids() {
        return Set.of(UUIDRegistry.RESERVED_1);
    }
}
