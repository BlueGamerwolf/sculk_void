package com.blue_gamerwolf.sculkvoid.enchantment;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import java.util.List;

public class SculknessEnchantment extends Enchantment {
	public static final TagKey<net.minecraft.world.entity.EntityType<?>> SCULK_CREATURES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("sculk_void", "sculk_creature"));

	public SculknessEnchantment(EquipmentSlot... slots) {
		super(Rarity.RARE, EnchantmentCategory.WEAPON, slots);
	}

	@Override
	public void doPostAttack(LivingEntity attacker, Entity target, int level) {
		if (!(target instanceof LivingEntity livingTarget))
			return;
		if (livingTarget.getType().is(SCULK_CREATURES)) {
			float maxHealth = livingTarget.getMaxHealth();
			// 💀 Scales with target max HP
			float bonusDamage = maxHealth * (0.08F * level);
			bonusDamage = Math.min(bonusDamage, 120.0F);
			livingTarget.hurt(attacker.damageSources().magic(), bonusDamage);
		}
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		return this != ench && !List.of(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS).contains(ench);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return Ingredient
				.of(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD, Items.TRIDENT, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE)
				.test(stack);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isTradeable() {
		return false;
	}
}
