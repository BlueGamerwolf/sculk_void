package com.blue_gamerwolf.sculkvoid.api.race.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

public interface SubRaceAbility {
    default void tick(Player player) {}
    default void onKill(Player player, LivingEntity killed) {}
    default void onHit(Player player, LivingEntity target) {}
    default void onDeath(Player player) {}
}