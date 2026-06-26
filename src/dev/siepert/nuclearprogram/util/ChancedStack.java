package dev.siepert.nuclearprogram.util;

import net.minecraft.src.ItemStack;

public class ChancedStack {
	public final ItemStack stack;
	public final float chance;

	public ChancedStack(ItemStack stack, float chance) {
		this.stack = stack;
		this.chance = chance;
	}
}
