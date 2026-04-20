
package com.blue_gamerwolf.sculkvoid.modded.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import com.blue_gamerwolf.sculkvoid.modded.client.model.Modelcrystal_arrow;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class SculkVoidModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelcrystal_arrow.LAYER_LOCATION, Modelcrystal_arrow::createBodyLayer);
	}
}
