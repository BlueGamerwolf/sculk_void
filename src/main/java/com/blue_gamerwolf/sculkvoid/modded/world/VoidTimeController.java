package com.blue_gamerwolf.sculkvoid.modded.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID)
public class VoidTimeController {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {

        if (!(event.level instanceof ServerLevel level)) return;

        if (!level.dimension().location().toString().equals("sculk_void:endless_void")) return;

        level.setDayTime(6000);
        level.setWeatherParameters(0, 0, false, false);
    }
}