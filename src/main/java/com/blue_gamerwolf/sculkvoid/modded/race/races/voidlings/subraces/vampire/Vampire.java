package com.blue_gamerwolf.sculkvoid.modded.race.races.voidlings.subraces.vampire;

import com.blue_gamerwolf.sculkvoid.api.race.api.SubRaceAbility;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.uuid.UUIDRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Vampire implements SubRace {

    @Override
    public String id() {
        return "vampire";
    }

    @Override
    public String description() {
        return "Blood predator that gains power from life essence.";
    }

    @Override
    public List<SubRaceAbility> abilities() {
        return Collections.singletonList(new SubRaceAbility() {

            @Override
            public void onHit(Player player, LivingEntity target) {
                player.heal(2.0F);
            }

            @Override
            public void tick(Player player) {
                if (player.isCrouching()) {
                    player.setSpeed(0.15F);
                }
            }
        });
    }

    @Override
    public Set<UUID> requiredPlayerUuids() {
        return Set.of(UUIDRegistry.RESERVED_2);
    }
}
