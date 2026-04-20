package com.blue_gamerwolf.sculkvoid.modded.crafting.voidassembler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import com.blue_gamerwolf.sculkvoid.modded.init.SculkVoidModCustomContent;

public class VoidAssemblyRecipe implements Recipe<SimpleContainer> {
	private final ResourceLocation id;
	private final Ingredient firstIngredient;
	private final Ingredient secondIngredient;
	private final ItemStack result;
	private final int craftTime;

	public VoidAssemblyRecipe(ResourceLocation id, Ingredient firstIngredient, Ingredient secondIngredient, ItemStack result, int craftTime) {
		this.id = id;
		this.firstIngredient = firstIngredient;
		this.secondIngredient = secondIngredient;
		this.result = result;
		this.craftTime = craftTime;
	}

	@Override
	public boolean matches(SimpleContainer container, Level level) {
		return matchesOrder(container.getItem(0), container.getItem(1)) || matchesOrder(container.getItem(1), container.getItem(0));
	}

	private boolean matchesOrder(ItemStack first, ItemStack second) {
		return this.firstIngredient.test(first) && this.secondIngredient.test(second);
	}

	@Override
	public ItemStack assemble(SimpleContainer container, net.minecraft.core.RegistryAccess registryAccess) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public ItemStack getResultItem(net.minecraft.core.RegistryAccess registryAccess) {
		return this.result.copy();
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SculkVoidModCustomContent.VOID_ASSEMBLY_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	public Ingredient getFirstIngredient() {
		return this.firstIngredient;
	}

	public Ingredient getSecondIngredient() {
		return this.secondIngredient;
	}

	public int getCraftTime() {
		return this.craftTime;
	}

	public static class Type implements RecipeType<VoidAssemblyRecipe> {
		public static final Type INSTANCE = new Type();
		public static final String ID = "void_assembly";

		private Type() {
		}

		@Override
		public String toString() {
			return ID;
		}
	}

	public static class Serializer implements RecipeSerializer<VoidAssemblyRecipe> {
		private static final int DEFAULT_CRAFT_TIME = 120;

		@Override
		public VoidAssemblyRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			JsonArray ingredientArray = GsonHelper.getAsJsonArray(json, "ingredients");
			if (ingredientArray.size() != 2) {
				throw new JsonParseException("Void assembly recipes require exactly 2 ingredients");
			}

			NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
			for (int i = 0; i < 2; i++) {
				ingredients.set(i, Ingredient.fromJson(ingredientArray.get(i)));
			}

			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			int craftTime = GsonHelper.getAsInt(json, "craft_time", DEFAULT_CRAFT_TIME);
			return new VoidAssemblyRecipe(recipeId, ingredients.get(0), ingredients.get(1), result, craftTime);
		}

		@Override
		public VoidAssemblyRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			Ingredient first = Ingredient.fromNetwork(buffer);
			Ingredient second = Ingredient.fromNetwork(buffer);
			ItemStack result = buffer.readItem();
			int craftTime = buffer.readVarInt();
			return new VoidAssemblyRecipe(recipeId, first, second, result, craftTime);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, VoidAssemblyRecipe recipe) {
			recipe.firstIngredient.toNetwork(buffer);
			recipe.secondIngredient.toNetwork(buffer);
			buffer.writeItem(recipe.result);
			buffer.writeVarInt(recipe.craftTime);
		}
	}
}
