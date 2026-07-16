package dev.siepert.nuclearprogram.bei;

import net.minecraft.src.ItemStack;

public class RecipeBloomery {
	public final ItemStack in;
	public final ItemStack out;
	public final ItemStack byproduct;
	public final int ticks;

	public RecipeBloomery(ItemStack in, ItemStack out, ItemStack byproduct, int ticks) {
		this.in = in;
		this.out = out;
		this.byproduct = byproduct;
		this.ticks = ticks;
	}
}
