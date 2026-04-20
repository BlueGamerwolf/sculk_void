package com.blue_gamerwolf.sculkvoid.modded.enchantment.unbreakable;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class UnbreakableHandler {

    private static boolean hasUnbreakable(ItemStack stack) {
        return stack != null &&
                stack.getEnchantmentLevel(SculkVoidModEnchantments.UNBREAKABLE.get()) > 0;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        for (ItemStack stack : player.getInventory().items) {
            if (hasUnbreakable(stack)) {
                stack.setDamageValue(0);

                if (stack.hasTag()) {
                    assert stack.getTag() != null;
                    stack.getTag().remove("Fire");
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();

        if (hasUnbreakable(left)) {
            event.setCost(0);
            event.setMaterialCost(0);
            event.setOutput(left.copy());
        }
    }

    @SubscribeEvent
    public static void onItemJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ItemEntity item) {
            if (hasUnbreakable(item.getItem())) {
                item.setRemainingFireTicks(0);
                item.setInvulnerable(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {

        if (event.phase != TickEvent.Phase.END) return;

        Level level = event.level;

        for (ItemEntity item : level.getEntitiesOfClass(
                ItemEntity.class,
                new net.minecraft.world.phys.AABB(
                        Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                        Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY
                )
        )) {

            ItemStack stack = item.getItem();

            if (!hasUnbreakable(stack)) continue;

            if (item.getY() < -64) {

                BlockPos pos = new BlockPos(
                        item.getBlockX(),
                        100,
                        item.getBlockZ()
                );

                if (!level.getBlockState(pos).is(Blocks.CHEST)) {
                    level.setBlockAndUpdate(pos, Blocks.CHEST.defaultBlockState());
                }

                BlockEntity be = level.getBlockEntity(pos);

                if (be instanceof ChestBlockEntity chest) {
                    for (int i = 0; i < chest.getContainerSize(); i++) {
                        if (chest.getItem(i).isEmpty()) {
                            chest.setItem(i, stack.copy());
                            break;
                        }
                    }
                }

                item.discard();
            }
        }
    }
}