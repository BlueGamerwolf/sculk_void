package com.blue_gamerwolf.sculkvoid.modded.init;

import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.blue_gamerwolf.sculkvoid.SculkVoidMod;
import com.blue_gamerwolf.sculkvoid.modded.block.VoidAssemblerBlock;
import com.blue_gamerwolf.sculkvoid.modded.block.entity.VoidAssemblerBlockEntity;
import com.blue_gamerwolf.sculkvoid.modded.crafting.voidassembler.VoidAssemblyRecipe;
import com.blue_gamerwolf.sculkvoid.modded.world.inventory.VoidAssemblerMenu;

public final class SculkVoidModCustomContent {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SculkVoidMod.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SculkVoidMod.MODID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SculkVoidMod.MODID);
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SculkVoidMod.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SculkVoidMod.MODID);

	public static final RegistryObject<Block> VOID_ASSEMBLER = BLOCKS.register("void_assembler", VoidAssemblerBlock::new);
	public static final RegistryObject<Item> VOID_ASSEMBLER_ITEM = ITEMS.register("void_assembler", () -> new BlockItem(VOID_ASSEMBLER.get(), new Item.Properties()));
	public static final RegistryObject<BlockEntityType<VoidAssemblerBlockEntity>> VOID_ASSEMBLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("void_assembler",
			() -> BlockEntityType.Builder.of(VoidAssemblerBlockEntity::new, VOID_ASSEMBLER.get()).build(null));
	public static final RegistryObject<MenuType<VoidAssemblerMenu>> VOID_ASSEMBLER_MENU = MENUS.register("void_assembler",
			() -> IForgeMenuType.create(VoidAssemblerMenu::new));
	public static final RegistryObject<RecipeSerializer<VoidAssemblyRecipe>> VOID_ASSEMBLY_SERIALIZER = RECIPE_SERIALIZERS.register("void_assembly",
			VoidAssemblyRecipe.Serializer::new);

	private SculkVoidModCustomContent() {
	}

	public static void register(IEventBus bus) {
		BLOCKS.register(bus);
		ITEMS.register(bus);
		BLOCK_ENTITIES.register(bus);
		MENUS.register(bus);
		RECIPE_SERIALIZERS.register(bus);
	}
}
