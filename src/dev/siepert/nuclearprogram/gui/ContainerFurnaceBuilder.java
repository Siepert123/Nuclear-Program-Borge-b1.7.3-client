package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityFurnaceBuilder;
import net.minecraft.src.*;

public class ContainerFurnaceBuilder extends Container {
	private final TileEntityFurnaceBuilder furnace;
	private int cookTime = 0;
	private int burnTime = 0;
	private int itemBurnTime = 0;
	private int cookTimeMax = 0;

	public ContainerFurnaceBuilder(InventoryPlayer inventory, TileEntityFurnaceBuilder furnace) {
		this.furnace = furnace;
		this.addSlot(new Slot(furnace, 0, 56, 17));
		this.addSlot(new Slot(furnace, 1, 56, 53));
		this.addSlot(new SlotFurnace(inventory.player, furnace, 2, 116, 35));

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();

		for (ICrafting listener : this.listeners) {
			if (this.cookTime != this.furnace.furnaceCookTime) {
				listener.sendData(this, 0, this.furnace.furnaceCookTime);
			}

			if (this.burnTime != this.furnace.furnaceBurnTime) {
				listener.sendData(this, 1, this.furnace.furnaceBurnTime);
			}

			if (this.itemBurnTime != this.furnace.currentItemBurnTime) {
				listener.sendData(this, 2, this.furnace.currentItemBurnTime);
			}

			if (this.cookTimeMax != this.furnace.getMaxRecipeTime()) {
				listener.sendData(this, 3, this.furnace.getMaxRecipeTime());
			}
		}

		this.cookTime = this.furnace.furnaceCookTime;
		this.burnTime = this.furnace.furnaceBurnTime;
		this.itemBurnTime = this.furnace.currentItemBurnTime;
		this.cookTimeMax = this.furnace.getMaxRecipeTime();
	}

	public void updateData(int id, int value) {
		if (id == 0) {
			this.furnace.furnaceCookTime = value;
		}

		if (id == 1) {
			this.furnace.furnaceBurnTime = value;
		}

		if (id == 2) {
			this.furnace.currentItemBurnTime = value;
		}

		if (id == 3) {
			this.furnace.maxRecipeTime = value;
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.furnace.canInteractWith(player);
	}

	public ItemStack getStackInSlot(int slotID) {
		ItemStack stack = null;
		Slot slot = this.slots.get(slotID);
		if(slot != null && slot.hasItem()) {
			ItemStack move = slot.getStack();
			stack = move.copy();
			if(slotID == 2) {
				this.func_28125_a(move, 3, 39, true);
			} else if(slotID >= 3 && slotID < 30) {
				this.func_28125_a(move, 30, 39, false);
			} else if(slotID >= 30 && slotID < 39) {
				this.func_28125_a(move, 3, 30, false);
			} else {
				this.func_28125_a(move, 3, 39, false);
			}

			if(move.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if(move.stackSize == stack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(move);
		}

		return stack;
	}
}
