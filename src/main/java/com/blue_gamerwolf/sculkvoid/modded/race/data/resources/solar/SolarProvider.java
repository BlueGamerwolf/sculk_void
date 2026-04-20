package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.solar;

import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceProvider;
import net.minecraft.world.entity.player.Player;

public class SolarProvider implements RaceResourceProvider {

    @Override
    public int get(Player player) {
        return SolarData.getSP(player);
    }

    @Override
    public int getMax(Player player) {
        return SolarData.getMaxSP(player);
    }

    @Override
    public void set(Player player, int value) {
        SolarData.setSP(player, value);
    }

    @Override
    public void add(Player player, int amount) {
        SolarData.addSP(player, amount);
    }

    @Override
    public void consume(Player player, int amount) {
        SolarData.consumeSP(player, amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return SolarData.hasEnough(player, amount);
    }
}