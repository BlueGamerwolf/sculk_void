package com.blue_gamerwolf.sculkvoid.modded.client.gui;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.races.registry.RaceRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.network.RequestRandomRaceMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;

import static com.blue_gamerwolf.sculkvoid.SculkVoidMod.PACKET_HANDLER;

public class RaceSelectionScreen extends Screen {

    public RaceSelectionScreen() {
        super(Component.literal("Choose Your Race"));
    }

    @Override
    protected void init() {
        super.init();

        List<RaceModule> races = RaceRegistry.getAll().values().stream()
                .sorted(Comparator.comparing((RaceModule module) -> module.category().ordinal()).thenComparing(RaceModule::displayName))
                .toList();

        int buttonWidth = 140;
        int gap = 8;
        int startX = this.width / 2 - buttonWidth - gap / 2;
        int startY = 74;

        for (int i = 0; i < races.size(); i++) {
            RaceModule race = races.get(i);
            int column = i % 2;
            int row = i / 2;
            int x = startX + column * (buttonWidth + gap);
            int y = startY + row * 24;

            addRenderableWidget(Button.builder(Component.literal(race.displayName()), button ->
                    Minecraft.getInstance().setScreen(new SubRaceSelectionScreen(race.id()))
            ).bounds(x, y, buttonWidth, 20).build());
        }

        addRenderableWidget(Button.builder(Component.literal("Random"), button -> {
            PACKET_HANDLER.sendToServer(new RequestRandomRaceMessage());
            onClose();
        }).bounds(this.width / 2 - 70, this.height - 52, 140, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        graphics.drawCenteredString(this.font, Component.literal("Story races drive the storyline. Extra races are optional world races."), this.width / 2, 40, 0xAAAAAA);
        graphics.drawCenteredString(this.font, Component.literal("Choose a race, then choose a subrace, or use random."), this.width / 2, 52, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
