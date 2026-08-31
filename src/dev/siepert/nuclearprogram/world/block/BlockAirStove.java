package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.node.CNNMultiblockProxy;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNMultiblockProxy;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityAirStove;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockAirStove extends BlockMulti implements IFluidIdentifiable, IOverlayInfo {
	public BlockAirStove(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityAirStove();
		if (meta >= 6) return TileEntityProxy.create(true, false);
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 3;
		dims[1] = 0;
		dims[2] = 1;
		dims[3] = 1;
		dims[4] = 2;
		dims[5] = 2;
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

		if (facing.getOffsetX() != 0) {
			this.setFlag(world, x+1, y, z);
			this.setFlag(world, x-1, y, z);
			this.setFlag(world, x, y+2, z+2);
			this.setFlag(world, x, y+2, z-2);
		} else {
			this.setFlag(world, x, y, z+1);
			this.setFlag(world, x, y, z-1);
			this.setFlag(world, x+2, y+2, z);
			this.setFlag(world, x-2, y+2, z);
		}
	}

	@Override
	protected void setFlag(World world, int x, int y, int z) {
		super.setFlag(world, x, y, z);

		PipeNet.setNode(world, x, y, z, new PNNMultiblockProxy(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);

		PipeNet.setNode(world, x, y, z, null);
	}

	private final int[] pos = new int[3];
	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityAirStove te = (TileEntityAirStove) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			te.setFuelType(fluidID);
		}
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		if (this.findCore(world, x, y, z, this.pos)) {
			if (this.hasFlag(world.getBlockMetadata(x, y, z))) {
				TileEntityAirStove te = (TileEntityAirStove) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
				information.add(Fluid.getLocalizedName(Fluid.fluidsList[te.fluidType]) + ": " + te.tankHeatSource + "mB/" + TileEntityAirStove.TANK_CAPACITY_FUEL + "mB");
				colors.add(0xFFFFFF);
				information.add("Air: " + te.tankAirIn + "mB/" + TileEntityAirStove.TANK_CAPACITY_AIR + "mB");
				colors.add(0xFFFFFF);
				information.add("Heated Air: " + te.tankAirOut + "mB/" + TileEntityAirStove.TANK_CAPACITY_AIR + "mB");
				colors.add(0xFFFFFF);
			}
		} else {
			information.add("Core not found");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}
}
