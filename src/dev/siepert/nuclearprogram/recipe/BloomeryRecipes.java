package dev.siepert.nuclearprogram.recipe;

import dev.siepert.nuclearprogram.world.te.TileEntityBloomery;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.FurnaceRecipesFix;

import java.util.HashMap;
import java.util.Map;

public class BloomeryRecipes {
	private static final BloomeryRecipes instance = new BloomeryRecipes();
	public static BloomeryRecipes blooming() {
		return instance;
	}

	private final Map<Integer, Result> recipes = new HashMap<>();

	private BloomeryRecipes() {

	}

	public void addSmelting(int itemID, ItemStack result, ItemStack byproduct) {
		this.addSmelting(itemID, -1, result, byproduct, TileEntityBloomery.RECIPE_TICKS);
	}
	public void addSmelting(int itemID, int itemMeta, ItemStack resultItem, ItemStack byproduct, int recipeTime) {
		int packed = pack(itemID, itemMeta);
		Result result = new Result(resultItem, byproduct, recipeTime);
		this.recipes.put(packed, result);
	}

	public Result getSmeltingResult(ItemStack stack) {
		if (stack == null || stack.stackSize == 0) return null;
		return this.getSmeltingResult(stack.itemID, stack.getItemDamage());
	}
	public Result getSmeltingResult(int itemID, int itemMeta) {
		int packed = pack(itemID, itemMeta);
		Result result = this.recipes.get(packed);
		if (result != null) return result;
		return this.recipes.get(pack(itemID, -1));
	}

	public static int pack(int itemID, int itemMeta) {
		return FurnaceRecipesFix.pack(itemID, itemMeta);
	}
	public static int pack(ItemStack stack) {
		return pack(stack.itemID, stack.getItemDamage());
	}

	public Map<Integer, Result> getSmeltingList() {
		return this.recipes;
	}

	public static final class Result {
		public final ItemStack item;
		public final ItemStack byproduct;
		public final int recipeTime;

		public Result(ItemStack item, ItemStack byproduct, int recipeTime) {
			this.item = item;
			this.byproduct = byproduct;
			this.recipeTime = recipeTime;
		}
	}
	public static final class Recipe {
		public final int recipeID;
		public final int recipeTime;

		public Recipe(int recipeID, int recipeTime) {
			this.recipeID = recipeID;
			this.recipeTime = recipeTime;
		}
	}
}
