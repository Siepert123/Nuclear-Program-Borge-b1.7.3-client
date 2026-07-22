package dev.siepert.nuclearprogram.bei;

import net.minecraft.src.ItemStack;

public class RecipeRTGFuel {
	public final ItemStack in;
	public final int ticks;
	public final int watts;
	public final ItemStack out;

	public RecipeRTGFuel(ItemStack in, int ticks, int watts, ItemStack out) {
		this.in = in;
		this.ticks = ticks;
		this.watts = watts;
		this.out = out;
	}
}
