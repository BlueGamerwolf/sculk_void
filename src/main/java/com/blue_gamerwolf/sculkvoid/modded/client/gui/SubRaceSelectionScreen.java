package com.blue_gamerwolf.sculkvoid.modded.client.gui;

import com.blue_gamerwolf.sculkvoid.api.race.api.RaceModule;
import com.blue_gamerwolf.sculkvoid.api.race.api.SubRace;
import com.blue_gamerwolf.sculkvoid.api.race.races.registry.RaceRegistry;
import com.blue_gamerwolf.sculkvoid.modded.race.network.RequestRandomRaceMessage;
import com.blue_gamerwolf.sculkvoid.modded.race.network.SubmitRaceSelectionMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

import static com.blue_gamerwolf.sculkvoid.SculkVoidMod.PACKET_HANDLER;

public class SubRaceSelectionScreen extends Screen {

    private final String raceId;

    public SubRaceSelectionScreen(String raceId) {
        super(Component.literal("Choose Subrace"));
        this.raceId = raceId;
    }

    @Override
    protected void init() {
        super.init();

        RaceModule race = RaceRegistry.get(raceId);
        if (race == null) {
            onClose();
            return;
        }

        List<SubRace> subRaces = race.getSubRaces().stream()
                .filter(subRace -> Minecraft.getInstance().player != null && subRace.isUnlockedFor(Minecraft.getInstance().player))
                .toList();

        int buttonWidth = 180;
        int startY = 76;

        for (int i = 0; i < subRaces.size(); i++) {
            SubRace subRace = subRaces.get(i);
            int y = startY + i * 24;

            addRenderableWidget(Button.builder(Component.literal(subRace.displayName()), button -> {
                PACKET_HANDLER.sendToServer(new SubmitRaceSelectionMessage(raceId, subRace.id()));
                onClose();
            }).bounds(this.width / 2 - buttonWidth / 2, y, buttonWidth, 20).build());
        }

        addRenderableWidget(Button.builder(Component.literal("Back"), button ->
                Minecraft.getInstance().setScreen(new RaceSelectionScreen())
        ).bounds(this.width / 2 - 92, this.height - 52, 88, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Random"), button -> {
            PACKET_HANDLER.sendToServer(new RequestRandomRaceMessage());
            onClose();
        }).bounds(this.width / 2 + 4, this.height - 52, 88, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        RaceModule race = RaceRegistry.get(raceId);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        graphics.drawCenteredString(this.font, Component.literal(race == null ? raceId : race.displayName()), this.width / 2, 40, 0xB8D8FF);
        graphics.drawCenteredString(this.font, Component.literal("UUID-locked subraces only appear for allowed players."), this.width / 2, 56, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null);
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
