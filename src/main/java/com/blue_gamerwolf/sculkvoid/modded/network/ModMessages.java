package com.blue_gamerwolf.sculkvoid.modded.network;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {

    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static final String PROTOCOL_VERSION = "1.0";

    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SculkVoidMod.MODID, "messages"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();

        INSTANCE.messageBuilder(ClankingPacket.class, id())
                .decoder(ClankingPacket::new)
                .encoder(ClankingPacket::toBytes)
                .consumerMainThread(ClankingPacket::handle)
                .add();
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
}