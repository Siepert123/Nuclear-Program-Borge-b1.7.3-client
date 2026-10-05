package dev.siepert.nuclearprogram.recipe.template;

import dev.siepert.nuclearprogram.recipe.IngredientSized;
import net.minecraft.src.ItemStack;

public class MachineRecipesManager<R extends RecipeGeneric, T extends RecipesGeneric<R>> {
	public final T recipes;
	private int recipeSlotsStart;
	private int recipeSlotsCount;

	public MachineRecipesManager(T recipes) {
		this.recipes = recipes;
	}

	public MachineRecipesManager<R, T> setSlots(int start, int end) {
		this.recipeSlotsStart = start;
		this.recipeSlotsCount = end - start;
		return this;
	}

	public boolean matchesSlot(R selected, ItemStack item, int slot) {
		if (selected == null) return false;
		if (selected.itemsIn.size() > this.recipeSlotsCount) return false;
		int id = slot - this.recipeSlotsStart;
		if (id < 0 || id >= selected.itemsIn.size()) return false;
		IngredientSized in = selected.itemsIn.get(id);
		return in.test(item);
	}
	public boolean matches(R selected, long energy, ItemStack[] inventory) {
		if (selected == null) return false;
		if (energy < selected.energyCost) return false;
		if (selected.itemsIn.size() > this.recipeSlotsCount) return false;
		for (int i = 0; i < selected.itemsIn.size(); i++) {
			IngredientSized in = selected.itemsIn.get(i);
			ItemStack compare = inventory[this.recipeSlotsStart + i];
			if (compare == null || !in.test(compare) || compare.stackSize < in.size) return false;
		}
		return true;
	}
	public void extract(R selected, ItemStack[] inventory) {
		if (selected == null) return;
		if (selected.itemsIn.size() > this.recipeSlotsCount) return;
		for (int i = 0; i < selected.itemsIn.size(); i++) {
			IngredientSized in = selected.itemsIn.get(i);
			ItemStack compare = inventory[this.recipeSlotsStart + i];
			compare.stackSize -= in.size;
			if (compare.stackSize <= 0) inventory[this.recipeSlotsStart + i] = null;
		}
	}

	public R getRecipe(String name) {
		return this.recipes.getRecipe(name);
	}
	public R getRecipe(int index) {
		if (index < 0 || index >= this.recipes.recipes.size()) return null;
		return this.recipes.recipes.get(index);
	}
}
