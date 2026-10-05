package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityManufactory;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerManufactory extends Container {
	private final TileEntityManufactory te;

	private int energy = 0;
	private int progress = 0;

	public ContainerManufactory(InventoryPlayer inventory, TileEntityManufactory te) {
		this.te = te;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.addSlot(new Slot100IQ(te, j + i * 3, 35 + j * 18, 17 + i * 18));
			}
		}
		this.addSlot(new SlotCraftResult(inventory.player, te, 9, 134, 35, TileEntityManufactory.WORKSTATION));

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

		this.energy = (int) this.te.energy;
	}
	@Override
	public void updateData(int id, int value) {
		super.updateData(id, value);

		switch (id) {
			case 0:
				this.te.energy = this.energy;
				break;
			case 1:
				break;
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.te.canInteractWith(player);
	}
}
