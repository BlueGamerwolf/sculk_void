package com.blue_gamerwolf.sculkvoid.modded.race.network;

import com.blue_gamerwolf.sculkvoid.modded.client.gui.RaceSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenRaceSelectionMessage {

    public OpenRaceSelectionMessage() {
    }

    public static void buffer(OpenRaceSelectionMessage message, FriendlyByteBuf buffer) {
    }

    public static OpenRaceSelectionMessage decode(FriendlyByteBuf buffer) {
        return new OpenRaceSelectionMessage();
    }

    public static void handle(OpenRaceSelectionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null && !minecraft.player.isSpectator()) {
                minecraft.setScreen(new RaceSelectionScreen());
            }
        }));
        context.setPacketHandled(true);
    }
}
