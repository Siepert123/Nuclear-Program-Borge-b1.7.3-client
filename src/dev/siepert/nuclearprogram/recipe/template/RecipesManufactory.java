package dev.siepert.nuclearprogram.recipe.template;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.IngredientInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.recipe.IngredientSized;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Ingredient;

public class RecipesManufactory extends RecipesGeneric<RecipeGeneric> {
	public static final RecipesManufactory INSTANCE = new RecipesManufactory();

	@Override
	public void initialize() {
		this.add(new RecipeGeneric("motor").setRecipeTicks(100).setEnergyCost(100)
				.setInputItems(
						new IngredientSized(Ingredient.of(ItemInit.stator.shiftedIndex)),
						new IngredientSized(Ingredient.of(ItemInit.rotor.shiftedIndex)),
						new IngredientSized(IngredientInit.plateSteel, 2)
				).setOutputItems(
						new ItemStack(ItemInit.motor)
				).setIconToFirstOutput()
		);
		this.add(new RecipeGeneric("heatex_boiler").setRecipeTicks(200).setEnergyCost(250)
				.setInputItems(
						new IngredientSized(IngredientInit.plateCopper, 16),
						new IngredientSized(Ingredient.of(BlockInit.fluidPipeCopper.blockID), 3),
						new IngredientSized(IngredientInit.ingotSteel, 8)
				).setOutputItems(
						new ItemStack(BlockInit.heatexBoiler)
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
