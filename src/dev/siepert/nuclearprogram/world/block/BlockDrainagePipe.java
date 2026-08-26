package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNReceiverTE;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityDrainagePipe;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockDrainagePipe extends BlockMulti implements IFluidIdentifiable, IOverlayInfo {
	public BlockDrainagePipe(int blockID, Material material) {
		super(blockID, material);

		this.coreEnableFluidConnection();
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 0;
		dims[1] = 0;
		dims[2] = 2;
		dims[3] = 0;
		dims[4] = 0;
		dims[5] = 0;
	}

	@Override
	public int getCoreOffset() {
		return 0;
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, EnumFacing facing, int offset) {
		super.fillSpace(world, x, y, z, facing, offset);
		x += facing.getOffsetX() * offset;
		z += facing.getOffsetZ() * offset;
		PipeNet.setNode(world, x, y, z, new PNNReceiverTE(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);

		PipeNet.setNode(world, x, y, z, null);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityDrainagePipe();
		if (meta >= 6) return TileEntityProxy.create(true, false);
		return null;
	}

	private final int[] pos = new int[3];
	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityDrainagePipe te = (TileEntityDrainagePipe) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			te.setDrainedFluid(fluidID, world.multiplayerWorld);
		}
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityDrainagePipe te = (TileEntityDrainagePipe) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			information.add("Draining " + Fluid.getLocalizedName(Fluid.fluidsList[te.fluidType]));
			colors.add(0x00FFFF);
			information.add("Drains maximally a bucket per second");
			colors.add(0x00FFFF);
		} else {
			information.add("Core not found");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}
}
