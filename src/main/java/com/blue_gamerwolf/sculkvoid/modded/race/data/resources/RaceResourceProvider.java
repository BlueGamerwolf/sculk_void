package com.blue_gamerwolf.sculkvoid.modded.race.data.resources;

import net.minecraft.world.entity.player.Player;

public interface RaceResourceProvider {

    int get(Player player);
    int getMax(Player player);

    void set(Player player, int value);

    void add(Player player, int amount);

    void consume(Player player, int amount);

    boolean has(Player player, int amount);
}