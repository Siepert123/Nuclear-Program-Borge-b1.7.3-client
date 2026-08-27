package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNMultiblockProxy;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityEngineV8;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockEngineV8 extends BlockMulti implements IFluidIdentifiable, IOverlayInfo{
	public BlockEngineV8(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableEnergyConnection();
		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityEngineV8();
		if (meta >= 6) return TileEntityProxy.create(true, false);
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 0;
		dims[1] = 0;
		dims[2] = 1;
		dims[3] = 1;
		dims[4] = 0;
		dims[5] = 0;
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

		this.setFlag(world, x + facing.getOffsetX(), y, z + facing.getOffsetZ());
		this.setFlag(world, x - facing.getOffsetX(), y, z - facing.getOffsetZ());
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
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		ItemStack held = player.inventory.getCurrentItem();
		if (held == null || held.itemID != ItemInit.screwdriver.shiftedIndex) return false;
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityEngineV8 te = (TileEntityEngineV8) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			te.burnRate = te.burnRate + 1;
			if (te.burnRate < 1) te.burnRate = 10;
			else if (te.burnRate > 10) te.burnRate = 1;
			te.onInventoryChanged();
			return true;
		} else return false;
	}

	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityEngineV8 te = (TileEntityEngineV8) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			te.setBurnedFluid(fluidID);
		}
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityEngineV8 te = (TileEntityEngineV8) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			information.add("Combusts at a rate of " + te.burnRate + "mB/t");
			colors.add(0x00FFFF);
			information.add(Fluid.getLocalizedName(Fluid.fluidsList[te.fluidType]) + ": " + te.tank + "mB/" + TileEntityEngineV8.TANK_CAPACITY + "mB");
			colors.add(0xFFFFFF);
			information.add("Energy gen: " + NumFormat.format(te.energy) + "RF/t or " + NumFormat.format(te.energy * 20) + "RF/s");
			colors.add(0xFFFFFF);
		} else {
			information.add("Core not found");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}
}
