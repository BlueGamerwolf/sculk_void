package com.blue_gamerwolf.sculkvoid.modded.race.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SubmitRaceSelectionMessage {

    private final String raceId;
    private final String subRaceId;

    public SubmitRaceSelectionMessage(String raceId, String subRaceId) {
        this.raceId = raceId;
        this.subRaceId = subRaceId;
    }

    public static void buffer(SubmitRaceSelectionMessage message, FriendlyByteBuf buffer) {
        buffer.writeUtf(message.raceId);
        buffer.writeUtf(message.subRaceId);
    }

    public static SubmitRaceSelectionMessage decode(FriendlyByteBuf buffer) {
        return new SubmitRaceSelectionMessage(buffer.readUtf(), buffer.readUtf());
    }

    public static void handle(SubmitRaceSelectionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                RaceSelectionHandler.assignRace(context.getSender(), message.raceId, message.subRaceId);
            }
        });
        context.setPacketHandled(true);
    }
}
