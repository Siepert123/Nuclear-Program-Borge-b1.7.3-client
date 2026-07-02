package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.te.TileEntityNuke;
import net.minecraft.src.*;

public abstract class BlockNuke extends BlockContainer implements IDetonateBehaviour {
	public BlockNuke(int blockID) {
		super(blockID, Material.iron);
		this.setLightOpacity(0);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, facing);
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
	protected abstract TileEntityNuke getBlockEntity();

	@Override
	public Callback detonate(World world, int x, int y, int z) {
		return Callback.MISSING_COMPONENTS;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		if (world.isBlockGettingPowered(x, y, z)) this.detonate(world, x, y, z);
	}
}
