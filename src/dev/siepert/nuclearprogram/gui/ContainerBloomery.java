package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityBloomery;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerBloomery extends Container {
	private final TileEntityBloomery furnace;

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
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.furnace.canInteractWith(player);
	}
}
