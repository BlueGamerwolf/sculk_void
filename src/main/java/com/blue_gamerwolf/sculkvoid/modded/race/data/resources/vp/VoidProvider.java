package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.vp;

import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceProvider;

import net.minecraft.world.entity.player.Player;

public class VoidProvider implements RaceResourceProvider {

    @Override
    public int get(Player player) {
        return VoidData.getVP(player);
    }

    @Override
    public int getMax(Player player) {
        return VoidData.getMaxVP(player);
    }

    @Override
    public void set(Player player, int value) {
        VoidData.setVP(player, value);
    }

    @Override
    public void add(Player player, int amount) {
        VoidData.addVP(player, amount);
    }

    @Override
    public void consume(Player player, int amount) {
        VoidData.consumeVP(player, amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return VoidData.getVP(player) >= amount;
    }
}
