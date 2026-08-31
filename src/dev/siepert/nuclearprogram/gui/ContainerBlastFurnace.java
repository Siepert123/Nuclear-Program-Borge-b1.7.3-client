package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityBlastFurnace;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerBlastFurnace extends Container {
	private final TileEntityBlastFurnace furnace;
	private int cookTime = 0;
	private int cokeBuffer = 0;
	private int airIntake = 0;
	private int maxCookTime = 0;

	public ContainerBlastFurnace(InventoryPlayer inventory, TileEntityBlastFurnace furnace) {
		this.furnace = furnace;

		this.addSlot(new SlotCoke(furnace, 0, 80, 32));
		this.addSlot(new Slot(furnace, 1, 80, 13));
		this.addSlot(new SlotCraftResult(inventory.player, furnace, 2, 125, 13, TileEntityBlastFurnace.WORKSTATION));
		this.addSlot(new SlotCraftResult(inventory.player, furnace, 3, 125, 32, TileEntityBlastFurnace.WORKSTATION));

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
