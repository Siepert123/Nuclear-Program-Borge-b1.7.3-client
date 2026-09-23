package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityPBR;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerPBR extends Container {
	private final TileEntityPBR te;
	public ContainerPBR(InventoryPlayer inventory, TileEntityPBR te) {
		this.te = te;

		this.addSlot(new Slot(te, 0, 8, 14));
		this.addSlot(new SlotCraftResult(inventory.player, te, 1, 8, 60));
		this.addSlot(new Slot(te, 2, 62, 14));
		this.addSlot(new SlotCraftResult(inventory.player, te, 3, 62, 60));

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
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.te.canInteractWith(player);
	}
}
