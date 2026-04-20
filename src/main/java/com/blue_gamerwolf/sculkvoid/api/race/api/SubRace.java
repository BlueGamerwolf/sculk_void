package com.blue_gamerwolf.sculkvoid.api.race.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SubRace {

    String id();
    String description();

    List<SubRaceAbility> abilities();

    default void tick(Player player) {
        for (SubRaceAbility ability : abilities()) {
            if (ability != null) ability.tick(player);
        }
    }

    default void onKill(Player player, LivingEntity killed) {
        for (SubRaceAbility ability : abilities()) {
            if (ability != null) ability.onKill(player, killed);
        }
    }

    default void onHit(Player player, LivingEntity target) {
        for (SubRaceAbility ability : abilities()) {
            if (ability != null) ability.onHit(player, target);
        }
    }

    default void onDeath(Player player) {
        for (SubRaceAbility ability : abilities()) {
            if (ability != null) ability.onDeath(player);
        }
    }

    default String displayName() {
        return RaceModule.formatName(id());
    }

    default Set<UUID> requiredPlayerUuids() {
        return Set.of();
    }

    default boolean isUnlockedFor(Player player) {
        return requiredPlayerUuids().isEmpty() || requiredPlayerUuids().contains(player.getUUID());
    }
}
