package com.blue_gamerwolf.sculkvoid.modded.race;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import com.blue_gamerwolf.sculkvoid.modded.race.data.storage.RaceData;
import com.blue_gamerwolf.sculkvoid.modded.race.network.OpenRaceSelectionMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RaceEvents {

    private RaceEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }

        var subRace = RaceManager.getSubRace(event.player);
        if (subRace != null) {
            subRace.tick(event.player);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }
        LivingEntity target = event.getEntity();
        if (target == player) {
            return;
        }

        var subRace = RaceManager.getSubRace(player);
        if (subRace != null) {
            subRace.onHit(player, target);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        if (event.getEntity() instanceof Player player) {
            var subRace = RaceManager.getSubRace(player);
            if (subRace != null) {
                subRace.onDeath(player);
            }
        }

        if (event.getSource().getEntity() instanceof Player player) {
            LivingEntity killed = event.getEntity();
            if (killed == player) {
                return;
            }
            var subRace = RaceManager.getSubRace(player);
            if (subRace != null) {
                subRace.onKill(player, killed);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }

        CompoundTag originalForgeData = event.getOriginal().getPersistentData().getCompound("ForgeData");
        CompoundTag clonedForgeData = originalForgeData.copy();
        if (!clonedForgeData.isEmpty()) {
            event.getEntity().getPersistentData().put("ForgeData", clonedForgeData);
        } else if (RaceData.hasRace(event.getOriginal())) {
            RaceData.setRace(event.getEntity(), RaceData.getRaceId(event.getOriginal()));
            if (RaceData.hasSubRace(event.getOriginal())) {
                RaceData.setSubRace(event.getEntity(), RaceData.getSubRaceId(event.getOriginal()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) {
            return;
        }
        if (RaceData.hasRace(serverPlayer)) {
            return;
        }

        SculkVoidMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenRaceSelectionMessage());
    }
}
