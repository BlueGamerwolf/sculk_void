
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.blue_gamerwolf.sculkvoid.init;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

import com.blue_gamerwolf.sculkvoid.client.gui.SculkInvestedbarrelGUIScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SculkVoidModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(SculkVoidModMenus.SCULK_INVESTEDBARREL_GUI.get(), SculkInvestedbarrelGUIScreen::new);
		});
	}
}
