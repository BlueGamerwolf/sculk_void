package com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.phoenix;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.api.uuid.UUIDRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Phoenix implements SubRace {

    @Override
    public String id() {
        return "phoenix";
    }

    @Override
    public String description() {
        return "Rebirth entity that evolves through death and flame.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return Collections.singletonList(new SubRaceAbility() {

            @Override
            public void onDeath(Player player) {
                // rebirth logic later
            }

            @Override
            public void onKill(Player player, LivingEntity killed) {
                // stage growth logic later
            }

            @Override
            public void tick(Player player) {
                if (player.isInWater()) {
                    player.hurt(player.damageSources().magic(), 1.0F);
                }
            }
        });
    }

    @Override
    public Set<UUID> requiredPlayerUuids() {
        return Set.of(UUIDRegistry.RESERVED_3);
    }
}
