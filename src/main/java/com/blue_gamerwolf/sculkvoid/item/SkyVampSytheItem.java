package com.blue_gamerwolf.sculkvoid.item;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;

import java.util.Objects;
import java.util.List;

import com.blue_gamerwolf.sculkvoid.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SkyVampSytheItem extends SwordItem {
	private static final String TAG_KILLS = "SkyKills";
	private static final String TAG_STAGE = "SkyStage";

	public SkyVampSytheItem() {
		super(new Tier() {
			public int getUses() {
				return 6800;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 7f;
			}

			public int getLevel() {
				return 5;
			}

			public int getEnchantmentValue() {
				return 18;
			}

			@SuppressWarnings("null")
			@Nonnull
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(SculkVoidModItems.SCULK_INGOT.get()));
			}
		}, 4, -1.8f, new Item.Properties().fireResistant());
	}

	// ================= TOOLTIP =================
	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		var tag = stack.getOrCreateTag();
		list.add(Component.literal("§cA Gift from the one they call Voidstone..."));
		list.add(Component.literal("§cKills: §7" + tag.getInt(TAG_KILLS)));
		list.add(Component.literal("§cStage: §7" + tag.getInt(TAG_STAGE)));
		super.appendHoverText(stack, world, list, flag);
	}

	// ================= ABILITY TRIGGER =================
	@SubscribeEvent
	@SuppressWarnings("null")
	public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
		if (event.getLevel().isClientSide) {
			// send request to server to run the ability from client-side clicks
			com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage msg = new com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage();
			com.blue_gamerwolf.sculkvoid.SculkVoidMod.PACKET_HANDLER.sendToServer(msg);
			return;
		}
		handleSlash(event.getEntity());
	}

	@SubscribeEvent
	@SuppressWarnings("null")
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getLevel().isClientSide) {
			com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage msg = new com.blue_gamerwolf.sculkvoid.network.SkyVampSlashMessage();
			com.blue_gamerwolf.sculkvoid.SculkVoidMod.PACKET_HANDLER.sendToServer(msg);
			return;
		}
		handleSlash(event.getEntity());
	}

	public static void handleSlash(Player player) {
		Level level = player.level();
		ItemStack stack = player.getMainHandItem();
		if (!(stack.getItem() instanceof SkyVampSytheItem item)) {
			return;
		}
		var tag = stack.getOrCreateTag();
		int stage = tag.getInt(TAG_STAGE);
		SculkVoidMod.debug("[SCYTHE] Attempt ability | Player={} Stage={} Health={}", player.getName().getString(), stage, player.getHealth());
		if (stage < 1) {
			SculkVoidMod.LOGGER.info("[SCYTHE] Blocked: Stage too low");
			return;
		}
		if (player.getCooldowns().isOnCooldown(item)) {
			SculkVoidMod.LOGGER.info("[SCYTHE] Blocked: On cooldown");
			return;
		}
		if (player.getHealth() <= 3.0F) {
			SculkVoidMod.LOGGER.warn("[SCYTHE] Blocked: Health too low");
			return;
		}
		if (level.isClientSide) {
			SculkVoidMod.debug("[SCYTHE] Cancelled: Client side execution");
			return;
		}
		SculkVoidMod.LOGGER.info("[SCYTHE] Ability ACTIVATED by {}", player.getName().getString());
		@SuppressWarnings("unused")
		var look = player.getLookAngle();
		@SuppressWarnings("unused")
		ServerLevel server = (ServerLevel) level;
		// Self damage + cooldown
		@SuppressWarnings("null")
		var damageSrc = Objects.requireNonNull(player.damageSources().magic());
		player.hurt(damageSrc, 3.0F);
		player.getCooldowns().addCooldown(item, 20);
		// Scale slash behavior by stage
		@SuppressWarnings("unused")
		int maxTicks;
		@SuppressWarnings("unused")
		int damageAmount;
		@SuppressWarnings("unused")
		double speedPerTick;
		@SuppressWarnings("unused")
		double hitRadius;
		@SuppressWarnings("unused")
		boolean pierceBlocks; // Stage 2+ pierces through blocks
		if (stage == 1) {
			// Stage 1: Basic slash
			maxTicks = 15 * 20; // 15 seconds
			speedPerTick = 0.6;
			hitRadius = 1.75;
			damageAmount = 15; // base
			pierceBlocks = false;
		} else if (stage == 2) {
			// Stage 2: Piercing bleed slash
			maxTicks = 20 * 20; // 20 seconds
			speedPerTick = 0.8; // faster
			hitRadius = 2;
			damageAmount = 20; // increased damage for stage 2
			pierceBlocks = true; // pierce through blocks
		} else if (stage == 3) {
			// Stage 3: Extended range vampire slash
			maxTicks = 25 * 20; // 25 seconds
			speedPerTick = 1.0; // even faster
			hitRadius = 4; // larger hit box
			damageAmount = 26; // stronger for vampiric stage
			pierceBlocks = true;
		} else {
			// Stage 4: Massive void slash
			maxTicks = 30 * 20; // 30 seconds
			speedPerTick = 1.3;
			hitRadius = 6; // huge hit box
			damageAmount = 30; // max power
			pierceBlocks = true;
		}
		final double[] px = {player.getX()};
		@SuppressWarnings("unused")
		final double[] py = {player.getEyeY()};
		@SuppressWarnings("unused")
		final double[] pz = {player.getZ()};
		@SuppressWarnings("unused")
		final int[] ticksLeft = {maxTicks};
		@SuppressWarnings("unused")
		final boolean[] finished = {false};
		@SuppressWarnings({"null", "unused"})
		Runnable tickRunner = new Runnable() {
			@Override
			public void run() {
				if (finished[0])
					return;
				if (ticksLeft[0]-- <= 0) {
					finished[0] = true;
					return;
				}
				px[0] += look.x * speedPerTick;
				py[0] += look.y * speedPerTick;
				pz[0] += look.z * speedPerTick;
				double x = px[0];
				double y = py[0];
				double z = pz[0];
				// Calculate direction vectors for particle positioning
				var lookVec = look.normalize();
				var upVec = new net.minecraft.world.phys.Vec3(0, 1, 0);
				var rightVec = lookVec.cross(upVec).normalize();
				if (stage == 1) {
					// Stage 1: Subtle crimson arc + dark smoke (red/black theme)
					for (int a = -2; a <= 2; a++) {
						double t = a * 0.18;
						double h = 0.08 * (2 - Math.abs(a));
						double pxOff = rightVec.x * t;
						double pyOff = h;
						double pzOff = rightVec.z * t;
						// Red damage indicator for arc
						server.sendParticles(net.minecraft.core.particles.ParticleTypes.DAMAGE_INDICATOR, x + pxOff, y + pyOff, z + pzOff, 1, 0, 0, 0, 0);
						if (Math.abs(a) <= 1) {
							server.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, x + pxOff, y + pyOff + 0.05, z + pzOff, 1, 0, 0, 0, 0.02);
						}
					}
					// Dark smoke trail (black)
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, x - lookVec.x * 0.2, y - 0.1, z - lookVec.z * 0.2, 2, 0.06, 0.06, 0.06, 0.01);
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, x - lookVec.x * 0.25, y - 0.12, z - lookVec.z * 0.25, 1, 0.08, 0.08, 0.08, 0.02);
					// Subtle void portal
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, x, y + 0.05, z, 1, 0.02, 0.02, 0.02, 0.01);
					if (ticksLeft[0] > maxTicks - 3) {
						server.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, player.getX(), player.getEyeY() + 0.5, player.getZ(), 3, 0.05, 0.5, 0.05, 0.1);
					}
				} else if (stage == 2) {
					// Stage 2: Bleed-heavy arc + thick black squid ink (dark red/black)
					for (int a = -3; a <= 3; a++) {
						double t = a * 0.15;
						double h = 0.12 * (3 - Math.abs(a));
						double pxOff = rightVec.x * t;
						double pyOff = h;
						double pzOff = rightVec.z * t;
						// Heavy red for bleed
						server.sendParticles(net.minecraft.core.particles.ParticleTypes.DAMAGE_INDICATOR, x + pxOff, y + pyOff, z + pzOff, 2, 0.02, 0.02, 0.02, 0.01);
						if (Math.abs(a) <= 2) {
							server.sendParticles(net.minecraft.core.particles.ParticleTypes.DAMAGE_INDICATOR, x + pxOff, y + pyOff + 0.1, z + pzOff, 2, 0.02, 0.02, 0.02, 0.02);
						}
					}
					// Very dark smoke trail
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, x - lookVec.x * 0.3, y - 0.15, z - lookVec.z * 0.3, 4, 0.1, 0.1, 0.1, 0.02);
					// Heavy black squid ink (bleed effect)
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, x - lookVec.x * 0.35, y - 0.2, z - lookVec.z * 0.35, 3, 0.12, 0.12, 0.12, 0.03);
					// Dark portal aura
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, x, y + 0.1, z, 2, 0.05, 0.05, 0.05, 0.02);
				} else if (stage == 3) {
					// Stage 3: Vampire crimson + soul purple (deep red/purple)
					for (int a = -4; a <= 4; a++) {
						double t = a * 0.12;
						double h = 0.15 * (4 - Math.abs(a) * 0.75);
						double pxOff = rightVec.x * t;
						double pyOff = h;
						double pzOff = rightVec.z * t;
						// Bright red crit for vampire essence
						server.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, x + pxOff, y + pyOff, z + pzOff, 2, 0.05, 0.05, 0.05, 0.01);
						// Soul purple (dark purple void)
						server.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL, x + pxOff, y + pyOff + 0.15, z + pzOff, 2, 0.05, 0.05, 0.05, 0.02);
					}
					// Intense dark void trail
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, x - lookVec.x * 0.4, y - 0.2, z - lookVec.z * 0.4, 4, 0.12, 0.12, 0.12, 0.02);
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, x - lookVec.x * 0.45, y - 0.25, z - lookVec.z * 0.45, 3, 0.15, 0.15, 0.15, 0.03);
					// Intense soul purple vortex
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL, x, y + 0.15, z, 3, 0.08, 0.08, 0.08, 0.03);
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL, x + rightVec.x * 0.5, y, z + rightVec.z * 0.5, 2, 0.05, 0.05, 0.05, 0.02);
				} else {
					// Stage 4: Void black with red shockwave flashes (intense black/red)
					for (int a = -5; a <= 5; a++) {
						double t = a * 0.1;
						double h = 0.2 * (5 - Math.abs(a) * 0.6);
						double pxOff = rightVec.x * t;
						double pyOff = h;
						double pzOff = rightVec.z * t;
						// Intense red bursts
						server.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, x + pxOff, y + pyOff, z + pzOff, 3, 0.08, 0.08, 0.08, 0.02);
						// Black void underneath
						server.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, x + pxOff, y + pyOff + 0.2, z + pzOff, 3, 0.08, 0.08, 0.08, 0.02);
					}
					// Massive black void shockwave
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, x - lookVec.x * 0.5, y - 0.3, z - lookVec.z * 0.5, 7, 0.15, 0.15, 0.15, 0.03);
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, x - lookVec.x * 0.55, y - 0.35, z - lookVec.z * 0.55, 5, 0.2, 0.2, 0.2, 0.04);
					// Red shockwave rings with void
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, x, y + 0.2, z, 4, 0.1, 0.1, 0.1, 0.04);
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL, x + rightVec.x * 1.0, y + 0.1, z + rightVec.z * 1.0, 3, 0.08, 0.08, 0.08, 0.03);
					server.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL, x - rightVec.x * 1.0, y + 0.1, z - rightVec.z * 1.0, 3, 0.08, 0.08, 0.08, 0.03);
				}
				// check for block collision (Stage 2+ pierce)
				if (!pierceBlocks) {
					@SuppressWarnings("null")
					BlockPos pos = BlockPos.containing(x, y, z);
					if (!level.getBlockState(pos).isAir()) {
						finished[0] = true;
						return;
					}
				}
				// check for entity collision (stage-scaled hit radius)
				AABB aabb = new AABB(x - hitRadius, y - hitRadius, z - hitRadius, x + hitRadius, y + hitRadius, z + hitRadius);
				var list = level.getEntitiesOfClass(LivingEntity.class, aabb, e -> e != player);
				if (!list.isEmpty()) {
					for (var living : list) {
						// Base hit
						float baseDamage = damageAmount;
						if (stage >= 2) {
							// Stage 2: apply slightly less initial damage and start bleed effect
							float initialDamage = baseDamage * 0.7F;
							@SuppressWarnings("null")
							var dmgSrcInitial = player.damageSources().playerAttack(player);
							living.hurt(dmgSrcInitial, initialDamage);
							SculkVoidMod.debug("[SCYTHE] Stage2 Hit entity (bleed): {}", living.getName().getString());
							// Stage 3-4: Life steal from initial hit
							if (stage >= 3) {
								float lifeSteal = stage == 3 ? initialDamage * 0.4F : initialDamage * 0.6F; // Stage 3: 40%, Stage 4: 60%
								player.heal(lifeSteal);
							}
							@SuppressWarnings("null")
							var dmgSrc = player.damageSources().playerAttack(player);
							// Bleed runner: ticks of bleed, damage per tick
							final int[] bleedTicks = {60}; // 3 seconds
							final float bleedDamage = stage >= 3 ? 1.0F : 0.5F; // Stage 3+ does more bleed damage
							final int stageRef = stage;
							final Player playerRef = player;
							Runnable bleedRunner = new Runnable() {
								@Override
								public void run() {
									if (bleedTicks[0]-- <= 0)
										return;
									if (!living.isAlive())
										return;
									// drip particles from mob
									ServerLevel srv = (ServerLevel) level;
									srv.sendParticles(net.minecraft.core.particles.ParticleTypes.DAMAGE_INDICATOR, living.getX(), living.getY() + living.getBbHeight() * 0.5, living.getZ(), stageRef <= 2 ? 2 : 3, 0.15, 0.25, 0.15, 0.02);
									// dark aura
									srv.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, living.getX(), living.getY() + living.getBbHeight() * 0.6, living.getZ(), stageRef <= 2 ? 1 : 2, 0.25, 0.5, 0.25, stageRef <= 2 ? 0.01 : 0.02);
									// occasional red burst
									if (bleedTicks[0] % 5 == 0) {
										// occasional red burst
										srv.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, living.getX(), living.getY() + living.getBbHeight() * 0.5, living.getZ(), stageRef <= 2 ? 2 : 3, 0.1, 0.1, 0.1, 0.02);
										@SuppressWarnings("null")
										var dmgSrcBleed = playerRef.damageSources().playerAttack(playerRef);
										living.hurt(dmgSrcBleed, bleedDamage);
										// Stage 3-4: Life steal from bleed ticks
										if (stageRef >= 3) {
											float bleedLifeSteal = stageRef == 3 ? bleedDamage * 0.5F : bleedDamage * 0.7F; // 50-70% of bleed damage
											playerRef.heal(bleedLifeSteal);
										}
									}
									// requeue
									com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, this);
								}
							};
							com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, bleedRunner);
						} else {
							@SuppressWarnings("null")
							var dmgSrc1 = player.damageSources().playerAttack(player);
							living.hurt(dmgSrc1, baseDamage);
							SculkVoidMod.debug("[SCYTHE] Hit entity: {}", living.getName().getString());
						}
					}
					// Stage 2+ can pierce multiple entities before stopping
					if (stage < 2) {
						finished[0] = true;
						return;
					}
				}
				// continue next tick
				com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, this);
			}
		};
		// start the scheduled slash
		com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, tickRunner);
		// play initial sound
		@SuppressWarnings("null")
		var sweepSound = Objects.requireNonNull(SoundEvents.PLAYER_ATTACK_SWEEP);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), sweepSound, SoundSource.PLAYERS, 1.0F, 1.0F);
	}

	// ================= KILL PROGRESSION =================
	@SubscribeEvent
	@SuppressWarnings("null")
	public static void onKill(LivingDeathEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide) {
			return;
		}
		ItemStack main = player.getMainHandItem();
		if (!(main.getItem() instanceof SkyVampSytheItem)) {
			return;
		}
		if (!event.getEntity().getTags().contains("Blood")) {
			SculkVoidMod.debug("[SCYTHE] Kill ignored: Missing Blood tag");
			return;
		}
		var tag = main.getOrCreateTag();
		int kills = tag.getInt(TAG_KILLS) + 1;
		tag.putInt(TAG_KILLS, kills);
		int oldStage = tag.getInt(TAG_STAGE);
		int newStage = calculateStage(kills);
		tag.putInt(TAG_STAGE, newStage);
		SculkVoidMod.LOGGER.info("[SCYTHE] Blood Kill registered | Player={} Kills={} Stage={}", player.getName().getString(), kills, newStage);
		if (newStage != oldStage) {
			SculkVoidMod.LOGGER.warn("[SCYTHE] STAGE LEVEL UP! {} -> {}", oldStage, newStage);
		}
		if (newStage >= 3) {
			// Stage 3: vampiric heal visuals + bigger heal
			ServerLevel srv = (ServerLevel) player.level();
			double sx = event.getEntity().getX();
			double sy = event.getEntity().getY() + event.getEntity().getBbHeight() * 0.5;
			double sz = event.getEntity().getZ();
			// spawn spiral particles moving toward player over ~30 ticks
			final int[] spiralTicks = {30};
			Runnable spiral = new Runnable() {
				@Override
				public void run() {
					if (spiralTicks[0]-- <= 0)
						return;
					double t = (30 - spiralTicks[0]) / 30.0;
					double px = sx + Math.cos(t * Math.PI * 4) * (1.0 * (1.0 - t));
					double py = sy + t * (player.getEyeY() - sy);
					double pz = sz + Math.sin(t * Math.PI * 4) * (1.0 * (1.0 - t));
					// use CRIT as a red-ish sparkle instead of Dust particle
					srv.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, px, py, pz, 2, 0.05, 0.05, 0.05, 0.02);
					srv.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, px, py, pz, 1, 0.02, 0.02, 0.02, 0.01);
					// final tick: heal player and show absorb
					if (spiralTicks[0] == 0) {
						player.heal(6.0F);
						@SuppressWarnings("null")
						var teleportSound = Objects.requireNonNull(net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT);
						@SuppressWarnings("null")
						BlockPos healPos = player.blockPosition();
						srv.playSound(null, healPos, teleportSound, SoundSource.PLAYERS, 0.6F, 0.8F);
					}
					com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, this);
				}
			};
			com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, spiral);
			SculkVoidMod.debug("[SCYTHE] Player vampiric heal triggered (Stage >=3)");
		}
		if (newStage >= 4) {
			// Stage 4 cinematic void explosion: small inward pull visuals then explode
			ServerLevel srv = (ServerLevel) player.level();
			double cx = player.getX();
			double cy = player.getY();
			double cz = player.getZ();
			// inward pull visuals for 12 ticks
			final int[] pullTicks = {12};
			Runnable pull = new Runnable() {
				@Override
				public void run() {
					if (pullTicks[0]-- <= 0) {
						// detonate
						srv.explode(null, cx, cy, cz, 3.0F, Level.ExplosionInteraction.NONE);
						@SuppressWarnings("null")
						var breakSound = Objects.requireNonNull(net.minecraft.sounds.SoundEvents.WITHER_BREAK_BLOCK);
						@SuppressWarnings("null")
						BlockPos explosionPos = player.blockPosition();
						srv.playSound(null, explosionPos, breakSound, SoundSource.PLAYERS, 1.0F, 0.9F);
						// big particle shock
						for (int i = 0; i < 40; i++) {
							double ang = (i / 40.0) * Math.PI * 2;
							double rx = cx + Math.cos(ang) * 2.5;
							double rz = cz + Math.sin(ang) * 2.5;
							srv.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, rx, cy, rz, 2, 0.3, 0.3, 0.3, 0.05);
							srv.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, rx, cy + 0.5, rz, 1, 0.2, 0.2, 0.2, 0.02);
							srv.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT, rx, cy + 0.1, rz, 2, 0.2, 0.2, 0.2, 0.05);
						}
						SculkVoidMod.LOGGER.error("[SCYTHE] Stage 4 cinematic explosion triggered!");
						return;
					}
					double radius = 3.0 * (pullTicks[0] / 12.0);
					// particles moving inward
					for (int i = 0; i < 12; i++) {
						double ang = (i / 12.0) * Math.PI * 2;
						double rx = cx + Math.cos(ang) * radius;
						double rz = cz + Math.sin(ang) * radius;
						srv.sendParticles(net.minecraft.core.particles.ParticleTypes.SQUID_INK, rx, cy, rz, 1, 0.1, 0.1, 0.1, 0.02);
						srv.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL, rx, cy + 0.3, rz, 1, 0.05, 0.05, 0.05, 0.01);
					}
					com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, this);
				}
			};
			com.blue_gamerwolf.sculkvoid.SculkVoidMod.queueServerWork(1, pull);
		}
	}

	private static int calculateStage(int kills) {
		if (kills >= 50)
			return 4;
		if (kills >= 30)
			return 3;
		if (kills >= 15)
			return 2;
		if (kills >= 5)
			return 1;
		return 0;
	}
}
