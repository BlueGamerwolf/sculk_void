package com.blue_gamerwolf.sculkvoid.modded.race;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class SubRaceAbilityUtil {

    private SubRaceAbilityUtil() {
    }

    public static void refreshEffect(Player player, MobEffect effect, int duration, int amplifier) {
        MobEffectInstance current = player.getEffect(effect);
        if (current != null && current.getAmplifier() == amplifier && current.getDuration() > 40) {
            return;
        }
        player.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, true));
    }

    public static boolean isInDarkness(Player player) {
        return player.level().getMaxLocalRawBrightness(player.blockPosition()) <= 7;
    }

    public static boolean canSeeSky(Player player) {
        return player.level().canSeeSky(player.blockPosition());
    }

    public static boolean isNaturalFooting(Player player) {
        BlockPos belowPos = player.blockPosition().below();
        BlockState below = player.level().getBlockState(belowPos);
        return below.is(BlockTags.DIRT)
                || below.is(BlockTags.LEAVES)
                || below.is(Blocks.GRASS_BLOCK)
                || below.is(Blocks.MOSS_BLOCK)
                || below.is(Blocks.SAND)
                || below.is(Blocks.RED_SAND);
    }
}
