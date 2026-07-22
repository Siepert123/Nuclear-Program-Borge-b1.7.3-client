package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityRTG;
import net.minecraft.src.*;

public class ContainerRTG extends Container {
	private final TileEntityRTG te;

	private int energy = 0;
	private int depletion = 0;
	private int maxDepletion = 0;
	private int energyPerTick = 0;

	public ContainerRTG(InventoryPlayer inventory, TileEntityRTG te) {
		this.te = te;

		this.addSlot(new Slot(te, 0, 26, 36));
		this.addSlot(new SlotCraftResult(inventory.player, te, 1, 134, 36));

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
			if (this.energy != this.te.energy) {
				listener.sendData(this, 0, this.te.energy);
			}

			if (this.depletion != this.te.depletion) {
				listener.sendData(this, 0, this.te.depletion);
			}

			if (this.maxDepletion != this.te.maxDepletion) {
				listener.sendData(this, 0, this.te.maxDepletion);
			}

			if (this.energyPerTick != this.te.energyPerTick) {
				listener.sendData(this, 0, this.te.energyPerTick);
			}
		}

		this.energy = this.te.energy;
		this.depletion = this.te.depletion;
		this.maxDepletion = this.te.maxDepletion;
		this.energyPerTick = this.te.energyPerTick;
	}

	@Override
	public void updateData(int id, int value) {
		super.updateData(id, value);

		switch (id) {
			case 0:
				this.energy = value;
				break;
			case 1:
				this.depletion = value;
				break;
			case 2:
				this.maxDepletion = value;
				break;
			case 3:
				this.energyPerTick = value;
				break;
		}
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.te.canInteractWith(player);
	}
}
