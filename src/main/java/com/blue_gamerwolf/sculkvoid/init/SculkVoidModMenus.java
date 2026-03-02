
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.blue_gamerwolf.sculkvoid.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import com.blue_gamerwolf.sculkvoid.world.inventory.SculkInvestedbarrelGUIMenu;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

public class SculkVoidModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SculkVoidMod.MODID);
	public static final RegistryObject<MenuType<SculkInvestedbarrelGUIMenu>> SCULK_INVESTEDBARREL_GUI = REGISTRY.register("sculk_investedbarrel_gui", () -> IForgeMenuType.create(SculkInvestedbarrelGUIMenu::new));
}
