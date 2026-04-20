package com.blue_gamerwolf.sculkvoid.modded.client;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

import net.minecraft.client.KeyMapping;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RaceKeybinds {
	public static final KeyMapping OPEN_RACE_MENU = new KeyMapping("key.sculk_void.race_menu", GLFW.GLFW_KEY_R, "key.categories.sculk_void");

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		event.register(OPEN_RACE_MENU);
	}
}
