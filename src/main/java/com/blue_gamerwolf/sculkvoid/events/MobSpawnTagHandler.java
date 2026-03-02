package com.blue_gamerwolf.sculkvoid.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobSpawnTagHandler {
	private static final TagKey<net.minecraft.world.entity.EntityType<?>> SCULK = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("sculk_void", "sculk_creatures"));
	private static final TagKey<net.minecraft.world.entity.EntityType<?>> BLOOD = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("sculk_void", "blood"));
	private static final TagKey<net.minecraft.world.entity.EntityType<?>> HELL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("sculk_void", "hell"));

	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide())
			return;
		Entity entity = event.getEntity();
		if (!(entity instanceof LivingEntity))
			return;
		if (entity.getType().is(SCULK)) {
			System.out.println("[SCULK VOID] Spawned Sculk creature: " + entity.getName().getString());
		}
		if (entity.getType().is(BLOOD)) {
			System.out.println("[SCULK VOID] Spawned Blood creature: " + entity.getName().getString());
		}
		if (entity.getType().is(HELL)) {
			System.out.println("[SCULK VOID] Spawned Hell creature: " + entity.getName().getString());
		}
	}
}
