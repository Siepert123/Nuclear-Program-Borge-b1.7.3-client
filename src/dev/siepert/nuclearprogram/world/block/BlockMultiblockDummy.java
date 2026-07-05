package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;

public class BlockMultiblockDummy extends BlockContainer {
	public BlockMultiblockDummy(int blockID) {
		super(blockID, Material.iron);
	}

	@Override
	protected TileEntity getBlockEntity() {
		return null;
	}
}
