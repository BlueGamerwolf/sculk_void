package com.blue_gamerwolf.sculkvoid.modded.client;

public class ClankingClientHandler {

    private static int blackTicks = 0;

    public static void trigger(int ticks) {
        blackTicks = ticks;
    }

    public static void tick() {
        if (blackTicks > 0) {
            blackTicks--;
        }
    }

    public static boolean isActive() {
        return blackTicks > 0;
    }
}
