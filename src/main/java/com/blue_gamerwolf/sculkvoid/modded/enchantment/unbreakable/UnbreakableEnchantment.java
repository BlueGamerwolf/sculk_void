package com.blue_gamerwolf.sculkvoid.modded.enchantment.unbreakable;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class UnbreakableEnchantment extends Enchantment {

    public UnbreakableEnchantment() {
        super(
                Rarity.RARE,
                EnchantmentCategory.VANISHABLE,
                EquipmentSlot.values()
        );
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
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
    public int getMaxLevel() {
        return 1;
    }
}