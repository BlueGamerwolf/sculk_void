package com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Random;

public class GreedEnchantment extends Enchantment {

    private static final Random RANDOM = new Random();

    public GreedEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        if (user.level().isClientSide) return;

        if (RANDOM.nextFloat() <= 0.30f) {
            user.heal(1.0f);
        }
    }

    public static void applyCurse(LivingEntity entity) {
        if (entity.tickCount % 20 == 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 0));
        }
    }
}