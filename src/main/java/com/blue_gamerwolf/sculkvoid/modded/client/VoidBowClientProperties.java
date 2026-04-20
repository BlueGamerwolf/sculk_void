package com.blue_gamerwolf.sculkvoid.modded.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.CrossbowItem;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModItems;
import com.blue_gamerwolf.sculkvoid.SculkVoidMod;

@Mod.EventBusSubscriber(modid = SculkVoidMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VoidBowClientProperties {

	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {

			ItemProperties.register(SculkVoidModItems.VOID_CROSSBOW.get(),
					new ResourceLocation("pull"),
					(stack, level, entity, seed) -> {
						if (entity == null) return 0.0F;
						return CrossbowItem.isCharged(stack)
								? 0.0F
								: (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(stack);
					});

			ItemProperties.register(SculkVoidModItems.VOID_CROSSBOW.get(),
					new ResourceLocation("pulling"),
					(stack, level, entity, seed) ->
							entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack)
									? 1.0F : 0.0F);

			ItemProperties.register(SculkVoidModItems.VOID_CROSSBOW.get(),
					new ResourceLocation("charged"),
					(stack, level, entity, seed) ->
							CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
		});
	}
}