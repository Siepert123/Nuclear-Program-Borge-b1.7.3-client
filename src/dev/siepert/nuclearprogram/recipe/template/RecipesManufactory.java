package dev.siepert.nuclearprogram.recipe.template;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.recipe.IngredientSized;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Ingredient;

public class RecipesManufactory extends RecipesGeneric<RecipeGeneric> {
	public static final RecipesManufactory INSTANCE = new RecipesManufactory();

	@Override
	public void initialize() {
		this.add(new RecipeGeneric("test").setRecipeTicks(100).setEnergyCost(100)
				.setInputItems(
						new IngredientSized(Ingredient.of("ingotIron"), 16),
						new IngredientSized(Ingredient.of("plateCopper"), 8)
				).setOutputItems(
						new ItemStack(BlockInit.simpleTurbine)
				).setIconToFirstOutput()
		);
		this.add(new RecipeGeneric("test2").setRecipeTicks(100).setEnergyCost(100)
				.setInputItems(
						new IngredientSized(Ingredient.of("ingotCopper"), 16),
						new IngredientSized(Ingredient.of("plateIron"), 8)
				).setOutputItems(
						new ItemStack(BlockInit.simpleCondenser)
				).setIconToFirstOutput()
		);
		this.add(new RecipeGeneric("geeked").setRecipeTicks(100).setEnergyCost(100)
				.setInputItems(
						new IngredientSized(Ingredient.of(Block.dirt.blockID), 64),
						new IngredientSized(Ingredient.of(Block.cobblestone.blockID), 64),
						new IngredientSized(Ingredient.of(Item.stick.shiftedIndex), 64)
				).setOutputItems(
						new ItemStack(BlockInit.cableElectrum, 8)
				).setIconToFirstOutput()
		);

		this.validate();
	}

	@Override
	public String getName() {
		return "manufactory";
	}

	private void validate() {
		for (RecipeGeneric recipe : this.recipes) {
			if (recipe.itemsOut.size() != 1) throw new IllegalStateException("Recipe must have exactly one output item!");
			if (recipe.itemsIn.isEmpty()) throw new IllegalStateException("Recipe must have at least one input item!");
			if (recipe.recipeTicks == 0) throw new IllegalStateException("Recipe must have a tick time specified!");
		}
	}
}
