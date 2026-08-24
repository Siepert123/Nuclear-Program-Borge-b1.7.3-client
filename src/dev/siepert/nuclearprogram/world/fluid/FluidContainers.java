package dev.siepert.nuclearprogram.world.fluid;

import dev.siepert.nuclearprogram.world.item.ItemUniversalTank;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class FluidContainers {
	public static final boolean[] isUniversalTank = new boolean[Item.itemsList.length];

	public static FluidStack getContainedFluid(ItemStack stack) {
		if (isUniversalTank[stack.itemID]) {
			if (stack.getItemDamage() == 0) return null;
			return FluidStack.GLOBAL_POOL.get(stack.getItemDamage(), ((ItemUniversalTank)stack.getItem()).fluidCapacity, 1);
		}
		return null;
	}
	public static ItemStack fill1Container(ItemStack stack, FluidStack from) {
		if (from.fluidType == 0 || from.amount == 0) return null;
		if (isUniversalTank[stack.itemID]) {
			if (stack.getItemDamage() != 0) return null;
			long capacity = ((ItemUniversalTank)stack.getItem()).fluidCapacity;
			if (from.amount < capacity) return null;
			from.amount -= capacity;
			return new ItemStack(stack.itemID, 1, from.fluidType);
		}
		return null;
	}
	public static ItemStack drain1Container(ItemStack stack, FluidStack to, long cap) {
		if (to.fluidType == 0 || to.amount >= cap) return null;
		if (isUniversalTank[stack.itemID]) {
			if (stack.getItemDamage() == 0) return null;
			long capacity = ((ItemUniversalTank)stack.getItem()).fluidCapacity;
			if (cap - to.amount < capacity) return null;
			to.amount += capacity;
			return new ItemStack(stack.itemID, 1, 0);
		}
		return null;
	}
}
