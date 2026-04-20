package com.blue_gamerwolf.sculkvoid.modded.util;

import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Holder;

public class VoidDamageSources {
	public static final ResourceKey<DamageType> VOID_PRISON = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("sculk_void", "void_prison_type"));

	public static DamageSource voidPrison(RegistryAccess registryAccess) {
		Holder.Reference<DamageType> type = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(VOID_PRISON);
		return new DamageSource(type);
	}
}
