package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.te.TileEntityModulator;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockModulator extends BlockContainer {
	public BlockModulator(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityModulator();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		TileEntityModulator te = (TileEntityModulator) world.getBlockTileEntity(x, y, z);
		if (te != null) {
			te.onNeighbourBlockChange();
		}
	}
}
