package dev.siepert.nuclearprogram.recipe.template;

import dev.siepert.nuclearprogram.recipe.IngredientSized;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.fluid.FluidStack;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;

import java.util.List;

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
	public String getLocalizedName(RecipeGeneric recipe) {
		return StringTranslate.getInstance().translateNamedKey("recipe." + this.recipes.getName() + "." + recipe.name);
	}
	public void addAdditionalData(RecipeGeneric recipe, List<String> tooltip) {
		// noinspection unchecked
		this.recipes.addAdditionalData((R) recipe, tooltip);
	}

	public List<String> collectRecipeTooltips(RecipeGeneric recipe, List<String> tooltip) {
		StringTranslate translate = StringTranslate.getInstance();
		tooltip.clear();
		if (!recipe.itemsIn.isEmpty() || !recipe.fluidsIn.isEmpty()) {
			tooltip.add("Inputs:");
			for (IngredientSized in : recipe.itemsIn) {
				tooltip.add(" " + in.size + "x " + translate.translateNamedKey(in.getDisplayItems().get((int) ((System.currentTimeMillis() / 500) % in.getDisplayItems().size())).getItemName()));
			}
			for (FluidStack in : recipe.fluidsIn) {
				tooltip.add(" " + in.amount + "mB " + Fluid.getLocalizedName(Fluid.fluidsList[in.fluidType]) + " at " + in.bar + " bar");
			}
		}
		if (!recipe.itemsOut.isEmpty() || !recipe.fluidsOut.isEmpty()) {
			tooltip.add("Outputs:");
			for (ItemStack out : recipe.itemsOut) {
				tooltip.add(" " + out.stackSize + "x " + translate.translateNamedKey(out.getItemName()));
			}
			for (FluidStack out : recipe.fluidsOut) {
				tooltip.add(" " + out.amount + "mB " + Fluid.getLocalizedName(Fluid.fluidsList[out.fluidType]) + " at " + out.bar + " bar");
			}
		}
		boolean energy = this.recipes.includesEnergy();
		boolean time = this.recipes.includesTime();
		if (energy || time) {
			tooltip.add("");
			if (energy && time) {
				tooltip.add((recipe.recipeTicks * 0.05F) + "s at " + NumFormat.format(recipe.energyCost * 20) + "RF/s");
			} else if (energy) {
				tooltip.add(NumFormat.format(recipe.energyCost * 20) + "RF/s");
			} else {
				tooltip.add((recipe.recipeTicks * 0.05F) + "s");
			}
		}
		this.addAdditionalData(recipe, tooltip);
		return tooltip;
	}
}
