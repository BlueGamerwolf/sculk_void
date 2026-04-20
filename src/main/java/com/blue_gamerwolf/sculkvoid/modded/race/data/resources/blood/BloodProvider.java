package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.blood;

import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceProvider;
import net.minecraft.world.entity.player.Player;

public class BloodProvider implements RaceResourceProvider {

    @Override
    public int get(Player player) {
        return BloodData.getBP(player);
    }

    @Override
    public int getMax(Player player) {
        return BloodData.getMaxBP(player);
    }

    @Override
    public void set(Player player, int value) {
        BloodData.setBP(player, value);
    }

    @Override
    public void add(Player player, int amount) {
        BloodData.addBP(player, amount);
    }

    @Override
    public void consume(Player player, int amount) {
        BloodData.consumeBP(player, amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return BloodData.getBP(player) >= amount;
    }
}