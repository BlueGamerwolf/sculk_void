package com.blue_gamerwolf.sculkvoid.modded.enchantment.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "sculkvoid")
public class Potions extends Enchantment {

    private final MobEffect effect;
    private final int maxLevel;

    public Potions(Rarity rarity, MobEffect effect, EquipmentSlot[] slots, int maxLevel) {
        super(rarity, EnchantmentCategory.ARMOR, slots);
        this.effect = effect;
        this.maxLevel = Math.max(1, maxLevel);
    }

    public MobEffect getEffect() {
        return this.effect;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 15;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        if (other instanceof Potions potions) {
            return potions.getEffect() != this.effect;
        }
        return super.checkCompatibility(other);
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide()) return;

        Map<MobEffect, Integer> strongestEffects = new HashMap<>();

        for (ItemStack armor : player.getArmorSlots()) {
            if (armor.isEmpty()) continue;

            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armor);

            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof Potions potions) {
                    strongestEffects.merge(potions.getEffect(), entry.getValue(), Math::max);
                }
            }
        }

        for (Map.Entry<MobEffect, Integer> entry : strongestEffects.entrySet()) {

            int amplifier = entry.getValue() - 1;

            MobEffectInstance current = player.getEffect(entry.getKey());

            boolean needsApply =
                    current == null ||
                            current.getAmplifier() != amplifier ||
                            current.getDuration() < 60;

            if (needsApply) {
                player.addEffect(new MobEffectInstance(
                        entry.getKey(),
                        140,
                        amplifier,
                        true,
                        false,
                        true
                ));
            }
        }
    }
    @Override
    public @NotNull String getDescriptionId() {
        return this.effect.getDescriptionId();
    }
}