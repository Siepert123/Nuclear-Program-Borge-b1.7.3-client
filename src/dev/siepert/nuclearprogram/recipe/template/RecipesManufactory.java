package dev.siepert.nuclearprogram.recipe.template;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.recipe.IngredientSized;
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
	}
}
