package dev.siepert.nuclearprogram.bei;

import net.minecraft.src.ItemStack;

public class RecipeFurnaceBuilder {
	public final ItemStack in;
	public final ItemStack out;
	public final int ticks;

	public RecipeFurnaceBuilder(ItemStack in, ItemStack out, int ticks) {
		this.in = in;
		this.out = out;
		this.ticks = ticks;
	}
}
