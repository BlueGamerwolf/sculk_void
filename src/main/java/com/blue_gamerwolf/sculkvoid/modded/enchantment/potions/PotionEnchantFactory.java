package com.blue_gamerwolf.sculkvoid.modded.enchantment.potions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.ArrayList;
import java.util.List;

public class PotionEnchantFactory {

    public static final List<RegistryObject<Enchantment>> ENCHANTS = new ArrayList<>();

    public static void init(DeferredRegister<Enchantment> reg) {

        for (MobEffect effect : ForgeRegistries.MOB_EFFECTS) {
            ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(effect);
            if (key == null) continue;

            String name = "potion_" + key.getPath();

            ENCHANTS.add(
                    reg.register(name, () -> new Potions(
                            Enchantment.Rarity.RARE,
                            effect,
                            new EquipmentSlot[]{
                                    EquipmentSlot.HEAD,
                                    EquipmentSlot.CHEST,
                                    EquipmentSlot.LEGS,
                                    EquipmentSlot.FEET
                            },
                            3
                    ))
            );
        }
    }
}
