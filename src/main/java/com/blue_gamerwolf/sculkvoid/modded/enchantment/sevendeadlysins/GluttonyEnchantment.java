package com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Random;

public class GluttonyEnchantment extends Enchantment {

    private static final Random RANDOM = new Random();

    // ✅ REQUIRED for registry (THIS FIXES YOUR ERROR)
    public GluttonyEnchantment() {
        super(
                Rarity.RARE,
                EnchantmentCategory.WEAPON,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    public GluttonyEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    // =========================
    // ON HIT EFFECT
    // =========================
    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        if (user.level().isClientSide) return;

        if (RANDOM.nextFloat() <= 0.20f) {
            user.addEffect(new MobEffectInstance(
                    MobEffects.SATURATION,
                    60,
                    4,
                    false,
                    false,
                    true
            ));
        }
    }

    // =========================
    // CURSE (tick event)
    // =========================
    public static void applyHungerCurse(LivingEntity entity) {
        if (entity.level().isClientSide) return;

        boolean hasGluttony =
                entity.getMainHandItem().getEnchantmentLevel(SculkVoidModEnchantments.GLUTTONY.get()) > 0
                        || entity.getOffhandItem().getEnchantmentLevel(SculkVoidModEnchantments.GLUTTONY.get()) > 0;

        if (!hasGluttony) return;

        if (entity.tickCount % 20 == 0) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.HUNGER,
                    40,
                    1,
                    true,
                    false,
                    true
            ));
        }
    }
}