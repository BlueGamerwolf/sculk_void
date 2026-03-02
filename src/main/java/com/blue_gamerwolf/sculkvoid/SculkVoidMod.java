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

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

import com.blue_gamerwolf.sculkvoid.init.SculkVoidModMenus;
import com.blue_gamerwolf.sculkvoid.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.init.SculkVoidModEnchantments;
import com.blue_gamerwolf.sculkvoid.init.SculkVoidModBlocks;
import com.blue_gamerwolf.sculkvoid.init.SculkVoidModBlockEntities;

@Mod("sculk_void")
public class SculkVoidMod {
	public static final Logger LOGGER = LogManager.getLogger(SculkVoidMod.class);
	public static final String MODID = "sculk_void";
	// 🔥 DEBUG TOGGLE
	public static final boolean DEBUG = true;

	public SculkVoidMod() {
		LOGGER.info("=== SCULK VOID MOD STARTING ===");
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		SculkVoidModBlocks.REGISTRY.register(bus);
		SculkVoidModItems.REGISTRY.register(bus);
		SculkVoidModBlockEntities.REGISTRY.register(bus);
		SculkVoidModMenus.REGISTRY.register(bus);
		SculkVoidModEnchantments.REGISTRY.register(bus);
		LOGGER.info("Registries loaded successfully.");
		// Register network messages
		SculkVoidMod.addNetworkMessage(com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage.class, (msg, buf) -> com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage.buffer(msg, buf),
				buf -> com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage.decode(buf), (msg, ctx) -> com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage.handle(msg, ctx));
	}

	// ================= NETWORK =================
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
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

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0) {
					actions.add(work);
				}
			});
			actions.forEach(e -> {
				try {
					if (DEBUG) {
						LOGGER.info("[QUEUE] Running queued server task");
					}
					e.getKey().run();
				} catch (Exception ex) {
					LOGGER.error("Error while executing queued server work!", ex);
				}
			});
			workQueue.removeAll(actions);
		}
	}

	// ================= DEBUG HELPER =================
	public static void debug(String message, Object... args) {
		if (DEBUG) {
			LOGGER.info("[DEBUG] " + message, args);
		}
	}
}
