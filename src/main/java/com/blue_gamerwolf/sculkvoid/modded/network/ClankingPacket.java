package com.blue_gamerwolf.sculkvoid.modded.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClankingPacket {

    private final int duration;

    public ClankingPacket(int duration) {
        this.duration = duration;
    }

    public ClankingPacket(FriendlyByteBuf buf) {
        this.duration = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(duration);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                com.blue_gamerwolf.sculkvoid.modded.client.ClankingClientHandler.trigger(duration);
            });
        });

        context.setPacketHandled(true);
    }
}