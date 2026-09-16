package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import dev.siepert.nuclearprogram.world.te.TileEntityTransmissionTower;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

public class BlockTransmissionTower extends BlockMulti {
	public BlockTransmissionTower(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 25;
		dims[1] = 0;
		dims[2] = 1;
		dims[3] = 1;
		dims[4] = 1;
		dims[5] = 1;
	}

	@Override
	protected EnumFacing limitRotations(EnumFacing facing) {
		switch (facing) {
			case EAST: return EnumFacing.WEST;
			case NORTH: return EnumFacing.SOUTH;
			default: return facing;
		}
	}

	@Override
	public int getCoreOffset() {
		return 1;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityTransmissionTower();
		if (meta >= 6) return TileEntityProxy.create(false, true);
		return null;
	}

	private final int[] pos = new int[3];
	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityTransmissionTower te = (TileEntityTransmissionTower) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			te.collapsed = true;
			te.onInventoryChanged();
			return true;
		} else return false;
	}
}
