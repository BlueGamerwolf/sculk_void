package com.blue_gamerwolf.sculkvoid.modded.network;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID)
public class VoidNoFallDamage {
    private static final String VOID_DIM = "sculk_void:endless_void";
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Player player)) return;
        if (!player.level().dimension().location().toString().equals(VOID_DIM)) return;
        event.setCanceled(true);
        event.setDamageMultiplier(0.0F);
        event.setDistance(0.0F);
    }
}