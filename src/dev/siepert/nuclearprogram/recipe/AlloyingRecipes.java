package dev.siepert.nuclearprogram.recipe;

import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.FurnaceRecipesFix;

import java.util.HashMap;
import java.util.Map;

public final class AlloyingRecipes {
	private static final AlloyingRecipes instance = new AlloyingRecipes();
	public static AlloyingRecipes alloying() {
		return instance;
	}

	private final Map<Long, Result> recipes = new HashMap<>();

	private AlloyingRecipes() {

	}

	public void addAlloying(int itemID1, int itemID2, ItemStack result) {
		this.addAlloying(itemID1, -1, itemID2, -1, result, 200);
	}
	public void addAlloying(int itemID1, int meta1, int itemID2, int meta2, ItemStack resultItem, int recipeTime) {
		long packed = pack(itemID1, meta1, itemID2, meta2);
		Result result = new Result(resultItem, recipeTime);
		this.recipes.put(packed, result);
	}

	public Result getAlloyingResult(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null || stack1.stackSize == 0 || stack2.stackSize == 0) return null;
		return this.getAlloyingResult(stack1.itemID, stack1.getItemDamage(), stack2.itemID, stack2.getItemDamage());
	}
	public Result getAlloyingResult(int itemID1, int meta1, int itemID2, int meta2) {
		long packed = pack(itemID1, meta1, itemID2, meta2);
		Result result = this.recipes.get(packed);
		if (result != null) return result;
		packed = pack(itemID1, -1, itemID2, meta2);
		result = this.recipes.get(packed);
		if (result != null) return result;
		packed = pack(itemID1, meta1, itemID2, -1);
		result = this.recipes.get(packed);
		if (result != null) return result;
		packed = pack(itemID1, -1, itemID2, -1);
		return this.recipes.get(packed);
	}

	public static int pack(int itemID, int itemMeta) {
		return FurnaceRecipesFix.pack(itemID, itemMeta);
	}
	public static long pack(int itemID1, int meta1, int itemID2, int meta2) {
		return (pack(itemID1, meta1) & 0xFFFFFFFFL) | (pack(itemID2, meta2) & 0xFFFFFFFFL) << 32;
	}
	public static int pack(ItemStack stack) {
		return pack(stack.itemID, stack.getItemDamage());
	}
	public static long pack(ItemStack stack1, ItemStack stack2) {
		return (pack(stack1) & 0xFFFFFFFFL) | (pack(stack2) & 0xFFFFFFFFL) << 32;
	}

	public Map<Long, Result> getAlloyingList() {
		return this.recipes;
	}

	public static final class Result {
		public final ItemStack item;
		public final int recipeTime;

		public Result(ItemStack item, int recipeTime) {
			this.item = item;
			this.recipeTime = recipeTime;
		}
	}
	public static final class Recipe {
		public final long recipeID;
		public final int recipeTime;

		public Recipe(long recipeID, int recipeTime) {
			this.recipeID = recipeID;
			this.recipeTime = recipeTime;
		}
	}
}
