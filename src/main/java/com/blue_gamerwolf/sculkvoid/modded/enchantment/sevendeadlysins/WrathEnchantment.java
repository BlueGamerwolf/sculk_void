package com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Random;

public class WrathEnchantment extends Enchantment {

    private static final Random RANDOM = new Random();

    public WrathEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        if (user.level().isClientSide) return;

        // rage boost chance
        if (RANDOM.nextFloat() <= 0.25f) {
            user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 1));
        }
    }

    public static void applyCurse(LivingEntity entity) {
        if (entity.level().isClientSide) return;

        if (entity.tickCount % 40 == 0 && entity.getHealth() < entity.getMaxHealth() / 2f) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
        }
    }
}