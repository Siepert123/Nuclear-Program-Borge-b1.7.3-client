package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.te.TileEntityDerrick;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;

public class BlockDerrick extends BlockMulti {
	public BlockDerrick(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityDerrick();
		//if (meta >= 6) return new TileEntityProxy();
		return null;
	}

	/**
	 * Writes multiblock dimensions to array.
	 * Ordered as UP-DOWN-FORWARD-BACKWARD-LEFT-RIGHT.
	 * @param dims The dimensions array to write to
	 */
	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 4;
		dims[1] = 0;
		dims[2] = 1;
		dims[3] = 1;
		dims[4] = 1;
		dims[5] = 1;
	}

	@Override
	public int getCoreOffset() {
		return 1;
	}
}
