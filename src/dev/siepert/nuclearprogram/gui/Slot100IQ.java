package dev.siepert.nuclearprogram.gui;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraftborge.loader.capability.IItemHandler;

public class Slot100IQ extends Slot {
	private final IItemHandler inv;
	private final int id;
	public <T extends IInventory & IItemHandler> Slot100IQ(T inventory, int id, int x, int y) {
		super(inventory, id, x, y);
		this.inv = inventory;
		this.id = id;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return this.inv.isItemValid(this.id, stack);
	}
}
