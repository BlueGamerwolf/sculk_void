package com.blue_gamerwolf.sculkvoid.modded.enchantment;

import com.blue_gamerwolf.sculkvoid.modded.network.ClankingPacket;
import com.blue_gamerwolf.sculkvoid.modded.network.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Clanking extends Enchantment {

    public Clanking() {
        super(
                Rarity.UNCOMMON,
                EnchantmentCategory.WEAPON,
                new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        );
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        if (target instanceof ServerPlayer player) {
            int duration = 40 + (level * 20); // 2s → 4s

            ModMessages.sendToPlayer(
                    new ClankingPacket(duration),
                    player
            );
        }
    }
}