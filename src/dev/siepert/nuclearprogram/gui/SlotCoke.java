package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotCoke extends Slot {
	public SlotCoke(IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.itemID == ItemInit.cokeCoal.shiftedIndex || stack.itemID == ItemInit.cokePetroleum.shiftedIndex;
	}
}
