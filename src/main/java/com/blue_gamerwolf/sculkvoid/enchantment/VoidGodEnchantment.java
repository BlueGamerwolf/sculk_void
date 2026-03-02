package com.blue_gamerwolf.sculkvoid.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import com.blue_gamerwolf.sculkvoid.init.SculkVoidModItems;

public class VoidGodEnchantment extends Enchantment {

	public VoidGodEnchantment() {
		super(Rarity.VERY_RARE, EnchantmentCategory.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack.is(SculkVoidModItems.VOID_BOW.get());
	}

	@Override
	public boolean isAllowedOnBooks() {
		return false;
	}

	@Override
	public boolean isDiscoverable() {
		return false;
	}

	@Override
	public boolean isTradeable() {
		return false;
	}

	@Override
	public boolean checkCompatibility(Enchantment other) {
		return false;
	}
}
