
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.blue_gamerwolf.sculkvoid.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import com.blue_gamerwolf.sculkvoid.enchantment.VoidGodEnchantment;
import com.blue_gamerwolf.sculkvoid.enchantment.SculknessEnchantment;
import com.blue_gamerwolf.sculkvoid.enchantment.SculkGodEnchantment;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

public class SculkVoidModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SculkVoidMod.MODID);
	public static final RegistryObject<Enchantment> SCULKNESS = REGISTRY.register("sculkness", () -> new SculknessEnchantment());
	public static final RegistryObject<Enchantment> VOID_GOD = REGISTRY.register("void_god", () -> new VoidGodEnchantment());
	public static final RegistryObject<Enchantment> SCULK_GOD = REGISTRY.register("sculk_god", () -> new SculkGodEnchantment());
}
