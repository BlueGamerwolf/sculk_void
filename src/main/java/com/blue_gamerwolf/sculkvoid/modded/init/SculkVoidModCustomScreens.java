package com.blue_gamerwolf.sculkvoid.modded.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.minecraft.client.gui.screens.MenuScreens;

import com.blue_gamerwolf.sculkvoid.modded.client.gui.VoidAssemblerScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SculkVoidModCustomScreens {
	private SculkVoidModCustomScreens() {
	}

	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> MenuScreens.register(SculkVoidModCustomContent.VOID_ASSEMBLER_MENU.get(), VoidAssemblerScreen::new));
	}
}
