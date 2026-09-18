package dev.siepert.nuclearprogram.bei;

import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Ingredient;

public class RecipeBlastFurnace {
	public final Ingredient in;
	public final ItemStack out;
	public final ItemStack byproduct;

	public RecipeBlastFurnace(Ingredient in, ItemStack out, ItemStack byproduct) {
		this.in = in;
		this.out = out;
		this.byproduct = byproduct;
	}
}
