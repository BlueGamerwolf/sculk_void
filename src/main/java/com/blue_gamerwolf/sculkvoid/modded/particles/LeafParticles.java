package com.blue_gamerwolf.sculkvoid.modded.particles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class LeafParticles {

    public static void spawnStealthPulse(Player player) {

        if (!(player.level() instanceof ServerLevel level)) return;

        Vec3 pos = player.position();

        for (int i = 0; i < 12; i++) {

            double offsetX = (Math.random() - 0.5) * 1.2;
            double offsetY = Math.random() * 1.0;
            double offsetZ = (Math.random() - 0.5) * 1.2;

            level.sendParticles(
                    ParticleTypes.CHERRY_LEAVES,
                    pos.x + offsetX,
                    pos.y + offsetY,
                    pos.z + offsetZ,
                    1,
                    0, 0, 0,
                    0.01
            );
        }
    }

    public static void spawnStealthFade(Player player) {

        if (!(player.level() instanceof ServerLevel level)) return;

        Vec3 pos = player.position();

        for (int i = 0; i < 6; i++) {

            double offsetX = (Math.random() - 0.5) * 0.8;
            double offsetY = Math.random() * 0.6;
            double offsetZ = (Math.random() - 0.5) * 0.8;

            level.sendParticles(
                    ParticleTypes.SPORE_BLOSSOM_AIR,
                    pos.x + offsetX,
                    pos.y + offsetY,
                    pos.z + offsetZ,
                    1,
                    0, 0, 0,
                    0.01
            );
        }
    }
}