package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.block.render.RenderBlockBellows;
import dev.siepert.nuclearprogram.world.te.TileEntityBellows;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.IconRegister;

public class BlockBellows extends BlockMulti {
	public BlockBellows(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityBellows();
		if (meta >= 6) return TileEntityProxy.create(false, false);
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 1;
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
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		super.onNeighborBlockChange(world, x, y, z, neighborBlockID);

		if (!keepInventory) {
			TileEntityBellows te = (TileEntityBellows) world.getBlockTileEntity(x, y, z);
			if (te != null) {
				te.update();
			}
		}
	}

	@Override
	public int getRenderType() {
		return RenderBlockBellows.RENDER_TYPE;
	}
	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = Block.planks.blockTexture;
	}
}
