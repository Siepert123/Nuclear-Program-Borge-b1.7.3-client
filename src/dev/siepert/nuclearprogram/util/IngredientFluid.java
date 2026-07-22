package dev.siepert.nuclearprogram.util;

import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Ingredient;

import java.util.function.Predicate;

public class IngredientFluid implements Predicate<ItemStack> {
	private static final IngredientFluid[] CACHE = new IngredientFluid[Fluid.ID_SIZE];

	public static Ingredient create(Fluid fluid, long amount, byte pressure) {
		ItemStack stack = Fluid.createItemRepresentation(fluid, amount, pressure);
		return Ingredient.custom(CACHE[fluid.fluidID], stack);
	}

	private final int fluidID;
	private IngredientFluid(int fluidID) {
		this.fluidID = fluidID;
	}
	@Override
	public boolean test(ItemStack stack) {
		return stack != null && stack.itemID == ItemInit.fluid.shiftedIndex && stack.getItemDamage() == this.fluidID;
	}

	static {
		for (int i = 0; i < Fluid.ID_SIZE; i++) {
			CACHE[i] = new IngredientFluid(i);
		}
	}
}
