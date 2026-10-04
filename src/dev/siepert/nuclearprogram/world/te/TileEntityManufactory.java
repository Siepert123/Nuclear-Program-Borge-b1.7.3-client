package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.capability.IItemHandlerModifiable;

public class TileEntityManufactory extends TileEntityMachineBase implements IItemHandlerModifiable {
	public final ItemStack[] inventory = new ItemStack[4];

	public TileEntityManufactory() {

	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
	}
	@Override
	public int getSlots() {
		return this.inventory.length;
	}
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return stack;
	}
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return null;
	}
	@Override
	public int getMaxStackSize(int slot) {
		return 64;
	}
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return IItemHandlerModifiable.super.isItemValid(slot, stack);
	}
}
