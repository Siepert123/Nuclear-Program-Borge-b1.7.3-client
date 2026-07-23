package dev.siepert.nuclearprogram.bei;

import net.minecraft.src.ItemStack;

public class RecipeGasCentrifuge {
	public final int fluidIn;
	public final int fluidInAmount;
	public final int fluidOut;
	public final int fluidOutAmount;
	public final ItemStack[] products;
	public final boolean withPush;

	public RecipeGasCentrifuge(int fluidIn, int fluidInAmount, int fluidOut, int fluidOutAmount, ItemStack... products) {
		this.fluidIn = fluidIn;
		this.fluidInAmount = fluidInAmount;
		this.fluidOut = fluidOut;
		this.fluidOutAmount = fluidOutAmount;
		this.products = products;
		this.withPush = true;
	}
	public RecipeGasCentrifuge(int fluidIn, int fluidInAmount, ItemStack... products) {
		this.fluidIn = fluidIn;
		this.fluidInAmount = fluidInAmount;
		this.fluidOut = 0;
		this.fluidOutAmount = 0;
		this.products = products;
		this.withPush = false;
	}
}
