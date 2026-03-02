package com.blue_gamerwolf.sculkvoid.client;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import com.blue_gamerwolf.sculkvoid.init.SculkVoidModItems;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VoidBowClientProperties {

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

            ItemProperties.register(
                SculkVoidModItems.VOID_BOW.get(),
                new ResourceLocation("pull"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0.0F;
                    return entity.getUseItem() != stack
                        ? 0.0F
                        : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            );

            ItemProperties.register(
                SculkVoidModItems.VOID_BOW.get(),
                new ResourceLocation("pulling"),
                (stack, level, entity, seed) ->
                    entity != null && entity.isUsingItem() && entity.getUseItem() == stack
                        ? 1.0F
                        : 0.0F
            );

        });
    }
}
