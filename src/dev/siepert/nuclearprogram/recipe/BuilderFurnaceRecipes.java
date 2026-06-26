package dev.siepert.nuclearprogram.recipe;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.FurnaceRecipesFix;

import java.util.HashMap;
import java.util.Map;

public class BuilderFurnaceRecipes {
	private static final BuilderFurnaceRecipes instance = new BuilderFurnaceRecipes();
	public static BuilderFurnaceRecipes smelting() {
		return instance;
	}

	private final Map<Integer, Result> recipes = new HashMap<>();

	private BuilderFurnaceRecipes() {
		this.addSmelting(Block.sand.blockID, new ItemStack(Block.glass, 1));
		this.addSmelting(Block.cobblestone.blockID, new ItemStack(Block.stone, 1));
		this.addSmelting(Item.clay.shiftedIndex, 0, new ItemStack(Item.brick, 1), 50);
		this.addSmelting(Block.cactus.blockID, new ItemStack(Item.dyePowder, 1, 2));
	}

	public void addSmelting(int itemID, ItemStack result) {
		this.addSmelting(itemID, -1, result, 100);
	}
	public void addSmelting(int itemID, int itemMeta, ItemStack resultItem, int recipeTime) {
		int packed = pack(itemID, itemMeta);
		Result result = new Result(resultItem, recipeTime);
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
		public final int recipeTime;

		public Result(ItemStack item, int recipeTime) {
			this.item = item;
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
