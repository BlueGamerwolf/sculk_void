package com.blue_gamerwolf.sculkvoid.mixin;

import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

@Mixin(StructureTemplatePool.class)
public class StructureTemplatePoolMixin {
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 150), require = 0)
	private static int increaseWeightLimit(int original) {
		return 5000;
	}
}