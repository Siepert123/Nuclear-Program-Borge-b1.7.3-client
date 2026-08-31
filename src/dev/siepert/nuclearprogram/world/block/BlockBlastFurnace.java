package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.block.render.RenderBlockBlastFurnace;
import dev.siepert.nuclearprogram.world.te.TileEntityBlastFurnace;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

public class BlockBlastFurnace extends BlockMulti {
	public BlockBlastFurnace(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityBlastFurnace();
		if (meta >= 6) return TileEntityProxy.create(true, false);
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 6;
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

	@Override
	public int getRenderType() {
		return RenderBlockBlastFurnace.RENDER_TYPE;
	}
}
