package com.blue_gamerwolf.sculkvoid.modded.race.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestRandomRaceMessage {

    public RequestRandomRaceMessage() {
    }

    public static void buffer(RequestRandomRaceMessage message, FriendlyByteBuf buffer) {
    }

    public static RequestRandomRaceMessage decode(FriendlyByteBuf buffer) {
        return new RequestRandomRaceMessage();
    }

    public static void handle(RequestRandomRaceMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                RaceSelectionHandler.assignRandomRace(context.getSender());
            }
        });
        context.setPacketHandled(true);
    }
}
