package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.te.TileEntityDerrick;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

public class BlockDerrick extends BlockMulti {
	public BlockDerrick(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableEnergyConnection();
		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityDerrick();
		if (meta >= 6) return TileEntityProxy.create(true, true);
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

	@Override
	protected void fillSpace(World world, int x, int y, int z, EnumFacing facing, int offset) {
		super.fillSpace(world, x, y, z, facing, offset);
		x += facing.getOffsetX() * offset;
		z += facing.getOffsetZ() * offset;

		this.setFlag(world, x+1, y, z);
		this.setFlag(world, x-1, y, z);
		this.setFlag(world, x, y, z+1);
		this.setFlag(world, x, y, z-1);
	}
}
