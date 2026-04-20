package com.blue_gamerwolf.sculkvoid.modded.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class WitchKeybinds {

    public static final KeyMapping OPEN_WITCH_GUI = new KeyMapping(
            "key.sculk_void.open_witch_gui",
            GLFW.GLFW_KEY_V,
            "key.categories.sculk_void"
    );
}