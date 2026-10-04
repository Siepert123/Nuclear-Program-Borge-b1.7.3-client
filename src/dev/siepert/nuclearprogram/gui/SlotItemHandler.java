package dev.siepert.nuclearprogram.gui;

import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraftborge.loader.capability.IItemHandlerModifiable;

public class SlotItemHandler extends Slot {
	protected final IItemHandlerModifiable inventory;
	protected final int id;
	public SlotItemHandler(IItemHandlerModifiable inventory, int id, int x, int y) {
		super(null, id, x, y);
		this.inventory = inventory;
		this.id = id;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return this.inventory.isItemValid(this.id, stack);
	}
	@Override
	public ItemStack getStack() {
		return this.inventory.getStackInSlot(this.id);
	}
	@Override
	public void putStack(ItemStack stack) {
		this.inventory.setStackInSlot(this.id, stack);
		this.onSlotChanged();
	}
	@Override
	public void onSlotChanged() {

	}
	@Override
	public int getSlotStackLimit() {
		return this.inventory.getMaxStackSize(this.id);
	}
	@Override
	public ItemStack decrStackSize(int count) {
		return this.inventory.extractItem(this.id, count, false);
	}
}
