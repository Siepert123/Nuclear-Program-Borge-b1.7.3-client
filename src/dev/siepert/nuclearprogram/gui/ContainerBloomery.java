package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityBloomery;
import net.minecraft.src.*;

public class ContainerBloomery extends Container {
	private final TileEntityBloomery furnace;
	private int cookTime = 0;
	private int burnTime = 0;
	private int cooldown = 0;

	public ContainerBloomery(InventoryPlayer inventory, TileEntityBloomery furnace) {
		this.furnace = furnace;

		this.addSlot(new SlotCoal(furnace, 0, 80, 36));
		this.addSlot(new Slot(furnace, 1, 80, 12));
		this.addSlot(new SlotCraftResult(inventory.player, furnace, 2, 120, 12));
		this.addSlot(new SlotCraftResult(inventory.player, furnace, 3, 120, 33));

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84-16 + i * 18));
			}
		}

		for(int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 142-16));
		}
	}

	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();

		for (ICrafting listener : this.listeners) {
			if (this.cookTime != this.furnace.recipeTicks) {
				listener.sendData(this, 0, this.furnace.recipeTicks);
			}

			if (this.burnTime != this.furnace.fuelHeap) {
				listener.sendData(this, 1, this.furnace.fuelHeap);
			}

			if (this.cooldown != this.furnace.cooldown) {
				listener.sendData(this, 2, this.furnace.cooldown);
			}
		}

		this.cookTime = this.furnace.recipeTicks;
		this.burnTime = this.furnace.fuelHeap;
		this.cooldown = this.furnace.cooldown;
	}

	@Override
	public void updateData(int id, int value) {
		if (id == 0) {
			this.furnace.recipeTicks = value;
		}

		if (id == 1) {
			this.furnace.fuelHeap = value;
		}

		if (id == 2) {
			this.furnace.cooldown = value;
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.furnace.canInteractWith(player);
	}
}
