package com.blue_gamerwolf.sculkvoid.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;

import com.mojang.blaze3d.systems.RenderSystem;
import com.blue_gamerwolf.sculkvoid.world.inventory.SculkInvestedbarrelGUIMenu;

public class SculkInvestedbarrelGUIScreen extends AbstractContainerScreen<SculkInvestedbarrelGUIMenu> {

    private static final ResourceLocation TEXTURE =
        new ResourceLocation("sculk_void:textures/screens/sculk_investedbarrel_gui.png");

    public SculkInvestedbarrelGUIScreen(SculkInvestedbarrelGUIMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        guiGraphics.blit(
            TEXTURE,
            this.leftPos,
            this.topPos,
            0,
            0,
            this.imageWidth,
            this.imageHeight,
            this.imageWidth,
            this.imageHeight
        );

        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 4210752, false);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (key == 256) { // ESC
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, scanCode, modifiers);
    }
}
