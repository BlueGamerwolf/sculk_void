package com.blue_gamerwolf.sculkvoid.modded.race.data.resources;

import net.minecraft.world.entity.player.Player;

public class VanillaExperienceProvider implements RaceResourceProvider {

    @Override
    public int get(Player player) {
        return totalExperience(player.experienceLevel, player.experienceProgress);
    }

    @Override
    public int getMax(Player player) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void set(Player player, int value) {
        setTotalExperience(player, Math.max(0, value));
    }

    @Override
    public void add(Player player, int amount) {
        if (amount <= 0) {
            return;
        }
        player.giveExperiencePoints(amount);
    }

    @Override
    public void consume(Player player, int amount) {
        if (amount <= 0) {
            return;
        }
        set(player, get(player) - amount);
    }

    @Override
    public boolean has(Player player, int amount) {
        return get(player) >= amount;
    }

    private static void setTotalExperience(Player player, int totalXp) {
        int level = levelForExperience(totalXp);
        int xpAtLevel = experienceForLevel(level);
        int xpIntoLevel = totalXp - xpAtLevel;
        int xpNeeded = xpNeededForNextLevel(level);

        player.totalExperience = totalXp;
        player.experienceLevel = level;
        player.experienceProgress = xpNeeded <= 0 ? 0.0F : (float) xpIntoLevel / (float) xpNeeded;
    }

    private static int totalExperience(int level, float progress) {
        return experienceForLevel(level) + Math.round(progress * xpNeededForNextLevel(level));
    }

    private static int levelForExperience(int totalXp) {
        int level = 0;
        while (experienceForLevel(level + 1) <= totalXp) {
            level++;
        }
        return level;
    }

    private static int experienceForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        }
        if (level <= 31) {
            return (int) (2.5D * level * level - 40.5D * level + 360);
        }
        return (int) (4.5D * level * level - 162.5D * level + 2220);
    }

    private static int xpNeededForNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        }
        if (level >= 15) {
            return 37 + (level - 15) * 5;
        }
        return 7 + level * 2;
    }
}
