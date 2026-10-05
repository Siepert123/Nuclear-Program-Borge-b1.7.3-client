package dev.siepert.nuclearprogram.recipe.template;

import dev.siepert.nuclearprogram.recipe.IngredientSized;
import dev.siepert.nuclearprogram.world.fluid.FluidStack;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Ingredient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeGeneric {
	public final String name;
	public ItemStack icon;
	public List<IngredientSized> itemsIn = Collections.emptyList();
	public List<FluidStack> fluidsIn = Collections.emptyList();
	public List<ItemStack> itemsOut = Collections.emptyList();
	public List<FluidStack> fluidsOut = Collections.emptyList();
	public int recipeTicks = 0;
	public long energyCost = 0;

	public int recipeID = -1;

	public RecipeGeneric(String name) {
		this.name = name;
	}

	public RecipeGeneric setIcon(ItemStack icon) {
		this.icon = icon;
		return this;
	}
	public RecipeGeneric setIconToFirstOutput() {
		this.icon = this.itemsOut.get(0);
		return this;
	}

	public RecipeGeneric setInputItems(IngredientSized... in) {
		this.itemsIn = Arrays.asList(in);
		return this;
	}
	public RecipeGeneric setInputFluids(FluidStack... in) {
		this.fluidsIn = Arrays.asList(in);
		return this;
	}
	public RecipeGeneric setOutputItems(ItemStack... out) {
		this.itemsOut = Arrays.asList(out);
		return this;
	}
	public RecipeGeneric setOutputFluids(FluidStack... out) {
		this.fluidsOut = Arrays.asList(out);
		return this;
	}

	public RecipeGeneric setRecipeTicks(int ticks) {
		this.recipeTicks = ticks;
		return this;
	}
	public RecipeGeneric setEnergyCost(long rf) {
		this.energyCost = rf;
		return this;
	}
}
