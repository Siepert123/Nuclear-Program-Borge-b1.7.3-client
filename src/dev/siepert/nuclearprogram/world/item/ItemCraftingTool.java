package dev.siepert.nuclearprogram.world.item;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemCraftingTool extends Item {
	public ItemCraftingTool(int itemID) {
		super(itemID);
		this.setMaxStackSize(1);
		this.setContainerItem(this);
	}

	@Override
	public ItemStack createCraftingRemainder(ItemStack stack) {
		if (stack == null || stack.getItemDamage() >= this.getMaxDamage()) return null;
		ItemStack copy = stack.copy();
		copy.setItemDamage(copy.getItemDamage() + 1);
		return copy;
	}
}
