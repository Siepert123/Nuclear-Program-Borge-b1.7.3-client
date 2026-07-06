package dev.siepert.nuclearprogram.gui;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotCraftResult extends Slot {
	private final EntityPlayer player;
	public SlotCraftResult(EntityPlayer player, IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public void onPickupFromSlot(ItemStack stack) {
		stack.onCrafting(this.player.worldObj, this.player);
		super.onPickupFromSlot(stack);
	}
}
