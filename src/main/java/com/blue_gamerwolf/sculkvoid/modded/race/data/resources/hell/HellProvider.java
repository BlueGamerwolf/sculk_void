package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.hell;

import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceProvider;
import net.minecraft.world.entity.player.Player;

public class HellProvider implements RaceResourceProvider {

    @Override
    public int get(Player player) {
        return HellData.getHell(player);
    }

    @Override
    public int getMax(Player player) {
        return HellData.getMaxHell(player);
    }

    @Override
    public void set(Player player, int value) {
        HellData.setHell(player, value);
    }

    @Override
    public void add(Player player, int amount) {
        HellData.addHell(player, amount);
    }

    @Override
    public void consume(Player player, int amount) {
        HellData.consumeHell(player, amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return HellData.hasEnough(player, amount);
    }
}