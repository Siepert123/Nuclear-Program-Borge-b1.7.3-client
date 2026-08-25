package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNMultiblockProxy;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityGasFlare;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.*;
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
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking()) return false;
		ItemStack held = player.inventory.getCurrentItem();
		if (held == null || held.itemID != ItemInit.fluidIdentifier.shiftedIndex) return false;
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityGasFlare te = (TileEntityGasFlare) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			return te.setFlaredGas(held.getItemDamage(), world.multiplayerWorld);
		} else return false;
	}

	private final int[] pos = new int[3];
	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityGasFlare te = (TileEntityGasFlare) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			information.add("Flaring " + Fluid.getLocalizedName(Fluid.fluidsList[te.fluidType]));
			colors.add(0x00FFFF);
			information.add("Flares maximally a bucket per second");
			colors.add(0x00FFFF);
		} else {
			information.add("Core not found");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}
}
