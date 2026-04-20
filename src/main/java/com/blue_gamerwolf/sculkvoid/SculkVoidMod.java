package com.blue_gamerwolf.sculkvoid;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.blue_gamerwolf.sculkvoid.modded.network.SkyVampSlashMessage;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModCustomContent;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModMenus;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModEnchantments;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModBlocks;
import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModBlockEntities;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.potions.PotionEnchantFactory;
import com.blue_gamerwolf.sculkvoid.modded.race.init.BuiltInRaceBootstrap;
import com.blue_gamerwolf.sculkvoid.modded.race.loader.RaceAddonLoader;
import com.blue_gamerwolf.sculkvoid.modded.race.network.OpenRaceSelectionMessage;
import com.blue_gamerwolf.sculkvoid.modded.race.network.RequestRandomRaceMessage;
import com.blue_gamerwolf.sculkvoid.modded.race.network.SubmitRaceSelectionMessage;

@Mod("sculk_void")
public class SculkVoidMod {
    public static final Logger LOGGER = LogManager.getLogger(SculkVoidMod.class);
    public static final String MODID = "sculk_void";
    public static final boolean DEBUG = true;

    public SculkVoidMod() {
        LOGGER.info("=== SCULK VOID MOD STARTING ===");


        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();


        SculkVoidModBlocks.REGISTRY.register(bus);
        SculkVoidModItems.REGISTRY.register(bus);
        SculkVoidModBlockEntities.REGISTRY.register(bus);
        SculkVoidModMenus.REGISTRY.register(bus);
        SculkVoidModCustomContent.register(bus);
        SculkVoidModEnchantments.REGISTRY.register(bus);
        PotionEnchantFactory.init(SculkVoidModEnchantments.REGISTRY);
        BuiltInRaceBootstrap.init();
        RaceAddonLoader.init();


        LOGGER.info("Registries loaded successfully.");

        SculkVoidMod.addNetworkMessage(
                SkyVampSlashMessage.class,
                SkyVampSlashMessage::buffer,
                SkyVampSlashMessage::decode,
                SkyVampSlashMessage::handle
        );
        SculkVoidMod.addNetworkMessage(
                OpenRaceSelectionMessage.class,
                OpenRaceSelectionMessage::buffer,
                OpenRaceSelectionMessage::decode,
                OpenRaceSelectionMessage::handle
        );
        SculkVoidMod.addNetworkMessage(
                SubmitRaceSelectionMessage.class,
                SubmitRaceSelectionMessage::buffer,
                SubmitRaceSelectionMessage::decode,
                SubmitRaceSelectionMessage::handle
        );
        SculkVoidMod.addNetworkMessage(
                RequestRandomRaceMessage.class,
                RequestRandomRaceMessage::buffer,
                RequestRandomRaceMessage::decode,
                RequestRandomRaceMessage::handle
        );
    }

    // ================= NETWORK =================
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, MODID),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int messageID = 0;

    public static <T> void addNetworkMessage(Class<T> messageType,
                                             BiConsumer<T, FriendlyByteBuf> encoder,
                                             Function<FriendlyByteBuf, T> decoder,
                                             BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {

        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);

        if (DEBUG) {
            LOGGER.info("[NETWORK] Registered message: {} with ID {}", messageType.getSimpleName(), messageID);
        }

        messageID++;
    }

    // ================= SERVER WORK QUEUE =================
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (DEBUG) {
            LOGGER.info("[QUEUE] Queued server work to run in {} ticks", tick);
        }
        workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    public static void debug(String s, String string, int stage, float health) {
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {

    }
}
