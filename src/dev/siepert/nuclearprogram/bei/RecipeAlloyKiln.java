package dev.siepert.nuclearprogram.bei;

import net.minecraft.src.ItemStack;

public class RecipeAlloyKiln {
	public final ItemStack in1, in2;
	public final ItemStack out;
	public final int ticks;

	public RecipeAlloyKiln(ItemStack in1, ItemStack in2, ItemStack out, int ticks) {
		this.in1 = in1;
		this.in2 = in2;
		this.out = out;
		this.ticks = ticks;
	}
}
