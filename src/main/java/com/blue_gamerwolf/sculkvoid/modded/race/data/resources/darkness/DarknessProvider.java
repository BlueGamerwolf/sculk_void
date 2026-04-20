package com.blue_gamerwolf.sculkvoid.modded.race.data.resources.darkness;

import com.blue_gamerwolf.sculkvoid.modded.race.data.resources.RaceResourceProvider;
import net.minecraft.world.entity.player.Player;

public class DarknessProvider implements RaceResourceProvider {

    @Override
    public int get(Player player) {
        return DarknessData.getDarkness(player);
    }

    @Override
    public int getMax(Player player) {
        return DarknessData.getMaxDarkness(player);
    }

    @Override
    public void set(Player player, int value) {
        DarknessData.setDarkness(player, value);
    }

    @Override
    public void add(Player player, int amount) {
        DarknessData.addDarkness(player, amount);
    }

    @Override
    public void consume(Player player, int amount) {
        DarknessData.consumeDarkness(player, amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return DarknessData.hasEnough(player, amount);
    }
}