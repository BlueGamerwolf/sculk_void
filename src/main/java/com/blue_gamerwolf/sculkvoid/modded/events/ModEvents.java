package com.blue_gamerwolf.sculkvoid.modded.events;

import com.blue_gamerwolf.sculkvoid.modded.enchantment.potions.Potions;
import com.blue_gamerwolf.sculkvoid.modded.enchantment.sevendeadlysins.GluttonyEnchantment;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class ModEvents {

    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingTickEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;

        for (ItemStack stack : player.getArmorSlots()) {

            Map<Enchantment, Integer> enchants =
                    EnchantmentHelper.getEnchantments(stack);

            for (var entry : enchants.entrySet()) {

                if (entry.getKey() instanceof Potions pot) {

                    player.addEffect(new MobEffectInstance(
                            pot.getEffect(),
                            220,
                            entry.getValue() - 1,
                            true,
                            false
                    ));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        GluttonyEnchantment.applyHungerCurse(event.player);
    }
}
