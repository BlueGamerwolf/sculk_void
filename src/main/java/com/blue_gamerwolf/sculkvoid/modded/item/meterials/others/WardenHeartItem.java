package com.blue_gamerwolf.sculkvoid.modded.item.meterials.others;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WardenHeartItem extends Item {

    private static final ResourceKey<Level> SCULK_VOID =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation("sculk_void", "sculk_void"));

    public WardenHeartItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .fireResistant()
                .rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right click on sculk to teleport"));
        tooltip.add(Component.literal("Right click on sculk catalyst to return"));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        if (level.getRandom().nextFloat() < 0.012f) {
            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.WARDEN_HEARTBEAT,
                    SoundSource.AMBIENT,
                    1.7f,
                    1.0f
            );
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer))
            return InteractionResult.SUCCESS;

        ServerLevel serverLevel = (ServerLevel) level;

        if (serverLevel.dimension().equals(SCULK_VOID)
                && level.getBlockState(pos).getBlock() == Blocks.SCULK_CATALYST) {

            ServerLevel overworld = serverPlayer.server.getLevel(Level.OVERWORLD);
            if (overworld == null) return InteractionResult.SUCCESS;

            BlockPos spawn = overworld.getSharedSpawnPos();

            serverPlayer.teleportTo(
                    overworld,
                    spawn.getX() + 0.5,
                    spawn.getY(),
                    spawn.getZ() + 0.5,
                    serverPlayer.getYRot(),
                    serverPlayer.getXRot()
            );

            return InteractionResult.SUCCESS;
        }

        if (level.getBlockState(pos).getBlock() != Blocks.SCULK)
            return InteractionResult.SUCCESS;

        if (serverLevel.dimension().equals(SCULK_VOID))
            return InteractionResult.SUCCESS;

        ServerLevel targetLevel = serverPlayer.server.getLevel(SCULK_VOID);
        if (targetLevel == null)
            return InteractionResult.SUCCESS;

        BlockPos basePos = new BlockPos(0, 79, 0);

        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                BlockPos platformPos = basePos.offset(x, 0, z);

                if (x == 0 && z == 0) {
                    targetLevel.setBlock(platformPos, Blocks.SCULK_CATALYST.defaultBlockState(), 3);
                } else if (targetLevel.getBlockState(platformPos).isAir()) {
                    targetLevel.setBlock(platformPos, Blocks.SCULK.defaultBlockState(), 3);
                }

                for (int y = 1; y <= 3; y++) {
                    BlockPos airPos = platformPos.above(y);
                    if (!targetLevel.getBlockState(airPos).isAir()) {
                        targetLevel.setBlock(airPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        serverPlayer.teleportTo(
                targetLevel,
                0.5,
                79.5,
                0.5,
                serverPlayer.getYRot(),
                serverPlayer.getXRot()
        );

        return InteractionResult.SUCCESS;
    }
}
