package com.blue_gamerwolf.sculkvoid.modded.network;

import com.blue_gamerwolf.sculkvoid.modded.item.weapons.SkyVampSytheItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SkyVampSlashMessage {

    public SkyVampSlashMessage() {}

    public static void buffer(SkyVampSlashMessage message, FriendlyByteBuf buffer) {
    }

    public static SkyVampSlashMessage decode(FriendlyByteBuf buffer) {
        return new SkyVampSlashMessage();
    }

    public static void handle(SkyVampSlashMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            try {
                if (ctx.getSender() != null) {
                    SkyVampSytheItem.handleSlash(ctx.getSender());
                }
            } catch (Exception e) {
                com.blue_gamerwolf.sculkvoid.SculkVoidMod.LOGGER.error("Error handling SkyVampSlashMessage", e);
            }
        });
        ctx.setPacketHandled(true);
    }
}
