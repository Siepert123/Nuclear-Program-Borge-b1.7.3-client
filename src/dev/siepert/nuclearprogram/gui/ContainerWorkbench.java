package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockWorkbench;
import net.minecraft.src.*;

public class ContainerWorkbench extends Container {
	private final World world;
	private final int x, y, z;

	public ContainerWorkbench(InventoryPlayer inventory, World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;

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
		return this.world.getBlockId(this.x, this.y, this.z) == BlockInit.workbench.blockID
				&& this.world.getBlockMetadata(this.x, this.y, this.z) < BlockWorkbench.VARIANTS.length;
	}
}
