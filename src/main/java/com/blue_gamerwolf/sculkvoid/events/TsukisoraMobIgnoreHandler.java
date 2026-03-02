package com.blue_gamerwolf.sculkvoid.events;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;

@Mod.EventBusSubscriber
public class TsukisoraMobIgnoreHandler {
	@SubscribeEvent
	public static void onMobTargetsPlayer(LivingChangeTargetEvent event) {
		if (!(event.getEntity() instanceof Mob mob))
			return;
		if (!(event.getNewTarget() instanceof Player player))
			return;
		boolean hasTsukisora = CuriosApi.getCuriosHelper().findFirstCurio(player, TsukisoraMobIgnoreHandler::isTsukisoraStack).isPresent();
		if (!hasTsukisora)
			return;
		event.setCanceled(true);
		mob.setTarget(null);
		mob.setLastHurtByMob(null);
	}

	/**
	 * Minimal, safe check without hard class coupling.
	 */
	private static boolean isTsukisoraStack(ItemStack stack) {
		// If you later add an item tag, replace this with stack.is(TAG)
		return stack.getItem().getClass().getSimpleName().equals("TsukisoraItem");
	}
}
