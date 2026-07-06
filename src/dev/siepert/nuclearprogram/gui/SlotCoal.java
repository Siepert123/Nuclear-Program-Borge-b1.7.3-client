package dev.siepert.nuclearprogram.gui;

import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotCoal extends Slot {
	public SlotCoal(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.itemID == Item.coal.shiftedIndex;
	}
}
