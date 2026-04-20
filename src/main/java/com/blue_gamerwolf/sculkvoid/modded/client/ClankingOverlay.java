package com.blue_gamerwolf.sculkvoid.modded.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClankingOverlay {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("sculkvoid", "textures/gui/clanking_black_screen.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {

        if (!ClankingClientHandler.isActive()) return;

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics gg = event.getGuiGraphics();

        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        gg.blit(TEXTURE, 0, 0, 0, 0, w, h, w, h);

        RenderSystem.disableBlend();
    }
}