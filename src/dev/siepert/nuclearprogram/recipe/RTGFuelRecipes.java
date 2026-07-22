package dev.siepert.nuclearprogram.recipe;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.FurnaceRecipesFix;
import net.minecraftborge.loader.tag.ItemTags;

import java.util.HashMap;
import java.util.Map;

public final class RTGFuelRecipes {
	private static final RTGFuelRecipes instance = new RTGFuelRecipes();
	public static RTGFuelRecipes instance() {
		return instance;
	}

	public final Map<Integer, Recipe> recipes = new HashMap<>();

	private RTGFuelRecipes() {

	}

	public void addRecipe(Item item, int metadata, int duration, int production, ItemStack product) {
		this.addRecipe(item.shiftedIndex, metadata, duration, production, product);
	}
	public void addRecipe(int itemID, int metadata, int duration, int production, ItemStack product) {
		this.recipes.put(pack(itemID, metadata), new Recipe(duration, production, product));
	}
	public void addRecipe(ItemStack in, int duration, int production, ItemStack product) {
		this.recipes.put(pack(in), new Recipe(duration, production, product));
	}
	public Recipe getRecipe(ItemStack in) {
		if (ItemTags.isItemEmpty(in)) return null;
		return this.recipes.get(pack(in));
	}

	public static int pack(ItemStack stack) {
		return pack(stack.itemID, stack.getItemDamage());
	}
	public static int pack(int itemID, int metadata) {
		return FurnaceRecipesFix.pack(itemID, metadata);
	}

	public static class Recipe {
		public final int duration;
		public final int production;
		public final ItemStack product;

		public Recipe(int duration, int production, ItemStack product) {
			this.duration = duration;
			this.production = production;
			this.product = product;
		}
	}
}
