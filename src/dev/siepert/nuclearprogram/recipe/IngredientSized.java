package dev.siepert.nuclearprogram.recipe;

import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Ingredient;

import java.util.List;

public class IngredientSized extends Ingredient {
	public final Ingredient ingredient;
	public final int size;

	public IngredientSized(Ingredient ingredient, int size) {
		this.ingredient = ingredient;
		this.size = size;
	}
	public IngredientSized(Ingredient ingredient) {
		this(ingredient, 1);
	}

	@Override
	public List<ItemStack> getDisplayItems() {
		return this.ingredient.getDisplayItems();
	}
	@Override
	public boolean test(ItemStack stack) {
		return this.ingredient.test(stack);
	}
}
