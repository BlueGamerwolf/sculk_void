package com.blue_gamerwolf.sculkvoid.modded.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModBlocks;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SculkVoidBlockRenderLayers {
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(SculkVoidModBlocks.NULL_PORTAL.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(SculkVoidModBlocks.HELL_GATE_FRAME.get(), RenderType.cutout());
		});
	}
}
