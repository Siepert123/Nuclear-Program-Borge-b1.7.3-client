package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityAlloyKiln;
import net.minecraft.src.*;

public class ContainerAlloyKiln extends Container {
	private final TileEntityAlloyKiln furnace;
	private int cookTime = 0;
	private int burnTime = 0;
	private int itemBurnTime = 0;
	private int cookTimeMax = 0;

	public ContainerAlloyKiln(InventoryPlayer inventory, TileEntityAlloyKiln furnace) {
		this.furnace = furnace;
		this.addSlot(new Slot(furnace, 0, 16, 24));
		this.addSlot(new Slot(furnace, 1, 72, 24));
		this.addSlot(new Slot(furnace, 2, 44, 52));
		this.addSlot(new SlotCraftResult(inventory.player, furnace, 3, 134, 36));

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
}
