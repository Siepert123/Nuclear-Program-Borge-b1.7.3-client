package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.*;
import net.minecraftborge.loader.Side;

import java.util.Random;

public class BlockFallout extends Block {
	public BlockFallout(int blockID) {
		super(blockID, Material.snow);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F / 16.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return Block.snow.canPlaceBlockAt(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		if (!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return 0;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return side == Side.UP || super.shouldSideBeRendered(world, x, y, z, side);
	}
}
