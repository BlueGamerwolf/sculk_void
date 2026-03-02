package com.blue_gamerwolf.sculkvoid.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import java.util.Queue;
import java.util.ArrayDeque;

@Mod.EventBusSubscriber
public class SculkNukeboomboomProcedure {
	// ====================
	// CONFIG (SERVER SAFE)
	// ====================
	private static final int RINGS = 7;
	private static final int BASE_RADIUS = 40;
	private static final int RING_THICKNESS = 20;
	private static final int RING_GAP = 12;
	private static final int RING_HEIGHT = 7;
	private static final int INITIAL_DELAY = 200;
	private static final int MAX_BLOCKS_PER_TICK = 1200;
	// ====================
	// STATE
	// ====================
	private static ServerLevel level;
	private static BlockPos center;
	private static int delayTimer = -1;
	private static int currentRing = 0;
	private static final Queue<BlockPos> placementQueue = new ArrayDeque<>();
	private static int explosionDelay = 0;
	private static boolean shouldExplode = false;
	private static ServerLevel explosionLevel;
	private static double explosionX, explosionY, explosionZ;

	// ====================
	// START EVENT
	// ====================
	public static void execute(Level world, double x, double y, double z) {
		if (!(world instanceof ServerLevel serverLevel))
			return;
		explosionLevel = serverLevel;
		explosionX = x;
		explosionY = y;
		explosionZ = z;
		explosionDelay = 100;
		shouldExplode = true;
	}

	@Mod.EventBusSubscriber
	public class ExplosionHandler {
		@SubscribeEvent
		public static void onServerTick(TickEvent.ServerTickEvent event) {
			if (event.phase != TickEvent.Phase.END)
				return;
			if (shouldExplode) {
				explosionDelay--;
				if (explosionDelay <= 0) {
					shouldExplode = false;
					explosionLevel.explode(null, explosionX, explosionY, explosionZ, 280.0F, Level.ExplosionInteraction.TNT);
				}
			}
		}
	}

	// ====================
	// SERVER TICK
	// ====================
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END)
			return;
		if (level == null || center == null)
			return;
		// Initial delay
		if (delayTimer > 0) {
			delayTimer--;
			return;
		}
		// Enqueue next ring if needed
		if (placementQueue.isEmpty()) {
			currentRing++;
			if (currentRing > RINGS) {
				spawnFinalWardens();
				cleanup();
				return;
			}
			enqueueRing(currentRing);
		}
		// Process queue with hard TPS budget
		int processed = 0;
		while (!placementQueue.isEmpty() && processed < MAX_BLOCKS_PER_TICK) {
			BlockPos pos = placementQueue.poll();
			if (pos == null)
				break;
			// Chunk-aware: do NOT force load
			if (!level.hasChunkAt(pos))
				continue;
			if (level.getBlockState(pos).canBeReplaced()) {
				level.setBlock(pos, getBlockForCurrentRing(pos), 3);
			}
			processed++;
		}
	}

	// ====================
	// RING ENQUEUE
	// ====================
	private static void enqueueRing(int ring) {
		int inner = BASE_RADIUS + (ring - 1) * (RING_THICKNESS + RING_GAP);
		int outer = inner + RING_THICKNESS;
		level.playSound(null, center, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.HOSTILE, 3.5F, 0.6F + ring * 0.02F);
		for (int dx = -outer; dx <= outer; dx++) {
			for (int dz = -outer; dz <= outer; dz++) {
				double dist = Math.sqrt(dx * dx + dz * dz);
				if (dist < inner || dist > outer)
					continue;
				for (int dy = -3; dy <= RING_HEIGHT; dy++) {
					placementQueue.add(center.offset(dx, dy, dz));
				}
			}
		}
	}

	// ====================
	// BLOCK SELECTION (FIXED)
	// ====================
	private static BlockState getBlockForCurrentRing(BlockPos pos) {
		// FINAL RING = SCULK NUKE + REDSTONE FOUNDATION
		if (currentRing == RINGS && pos.getY() == center.getY()) {
			Block sculkNuke = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("sculk_void", "sculk_nuke"));
			if (sculkNuke != null) {
				BlockPos below = pos.below();
				if (level.getBlockState(below).canBeReplaced()) {
					level.setBlock(below, Blocks.REDSTONE_BLOCK.defaultBlockState(), 3);
				}
				return sculkNuke.defaultBlockState();
			}
		}
		// Default corruption
		return Blocks.SCULK.defaultBlockState();
	}

	// ====================
	// FINAL WARDENS
	// ====================
	private static void spawnFinalWardens() {
		level.playSound(null, center, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.HOSTILE, 6.0F, 0.4F);
		for (int i = 0; i < 3; i++) {
			var warden = EntityType.WARDEN.create(level);
			if (warden != null) {
				warden.moveTo(center.getX() + 0.5, center.getY(), center.getZ() + 0.5, level.random.nextFloat() * 360F, 0);
				warden.setPersistenceRequired();
				level.addFreshEntity(warden);
			}
		}
	}

	// ====================
	// DARKNESS EFFECT
	// ====================
	private static void applyDarkness() {
		int radius = BASE_RADIUS + currentRing * (RING_THICKNESS + RING_GAP);
		for (Player player : level.players()) {
			if (player.distanceToSqr(center.getX(), center.getY(), center.getZ()) < radius * radius) {
				player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 120, 0, false, false));
			}
		}
	}

	// ====================
	// CLEANUP
	// ====================
	private static void cleanup() {
		level = null;
		center = null;
		delayTimer = -1;
		placementQueue.clear();
	}
}
