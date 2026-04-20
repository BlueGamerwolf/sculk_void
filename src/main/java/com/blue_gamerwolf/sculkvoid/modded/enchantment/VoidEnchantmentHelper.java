package com.blue_gamerwolf.sculkvoid.modded.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModEnchantments;

public class VoidEnchantmentHelper {

    public static int getVoidGod(ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(
                SculkVoidModEnchantments.VOID_GOD.get(),
                stack
        );
    }

    public static int getPower(ItemStack stack) {
        int base = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        return base + getVoidGod(stack);
    }

    public static int getPunch(ItemStack stack) {
        int base = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
        return base + getVoidGod(stack);
    }

    public static int getFlame(ItemStack stack) {
        int base = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack);
        return Math.max(base, getVoidGod(stack) >= 1 ? 1 : 0);
    }

    public static int getMultishot(ItemStack stack) {
        int base = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
        return base + (getVoidGod(stack) / 2);
    }

    public static int getQuickCharge(ItemStack stack) {
        int base = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return base + getVoidGod(stack);
    }
}