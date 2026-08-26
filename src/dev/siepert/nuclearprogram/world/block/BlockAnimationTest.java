package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.te.TileEntityAnimationTest;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;

public class BlockAnimationTest extends BlockMulti {
	public BlockAnimationTest(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableEnergyConnection();
		this.flagEnableFluidConnection();
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 2;
		dims[1] = 0;
		dims[2] = 2;
		dims[3] = 2;
		dims[4] = 2;
		dims[5] = 2;
	}

	@Override
	public int getCoreOffset() {
		return 2;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityAnimationTest();
		if (meta >= 6) return TileEntityProxy.create(true, true);
		return null;
	}
}
