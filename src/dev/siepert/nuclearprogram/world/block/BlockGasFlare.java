package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNMultiblockProxy;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.te.TileEntityGasFlare;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockGasFlare extends BlockMulti implements IOverlayInfo {
	public BlockGasFlare(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityGasFlare();
		if (meta >= 6) return TileEntityProxy.create(true, false);
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 9;
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
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);

		CableNet.setNode(world, x, y, z, null);
		PipeNet.setNode(world, x, y, z, null);
	}

	@Override
	protected void setFlag(World world, int x, int y, int z) {
		super.setFlag(world, x, y, z);

		PipeNet.setNode(world, x, y, z, new PNNMultiblockProxy(world).positioned(x, y, z));
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {

	}
}
