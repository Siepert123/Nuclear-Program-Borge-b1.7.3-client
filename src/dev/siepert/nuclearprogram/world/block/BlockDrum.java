package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNReceiverTE;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.fluid.FluidContainers;
import dev.siepert.nuclearprogram.world.te.TileEntityDrum;
import net.minecraft.src.*;

import java.util.List;

public class BlockDrum extends BlockContainer implements IFluidIdentifiable, IOverlayInfo {
	public BlockDrum(int blockID, Material material) {
		super(blockID, material);

		BlockFluidPipe.enableConnection(blockID);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		PipeNet.setNode(world, x, y, z, new PNNReceiverTE(world).positioned(x, y, z));
	}
	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		PipeNet.setNode(world, x, y, z, null);
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.getCurrentEquippedItem() != null) return false;
		ItemStack held = player.getCurrentEquippedItem();
		if (held != null && held.itemID == ItemInit.screwdriver.shiftedIndex) {
			TileEntityDrum te = (TileEntityDrum) world.getBlockTileEntity(x, y, z);
			te.fill = te.capacity;
			te.onInventoryChanged();
			return true;
		}
		return false;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		TileEntityDrum te = new TileEntityDrum();
		switch (meta) {
			case 0: te.setCapacity(8000L); break;
			case 1: te.setCapacity(16000L); break;
			case 2: te.setCapacity(24000L); break;
		}
		return te;
	}

	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		TileEntityDrum te = (TileEntityDrum) world.getBlockTileEntity(x, y, z);
		if (te.type != fluidID) {
			te.type = fluidID;
			te.fill = 0L;
			te.onInventoryChanged();
		}
	}
	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		TileEntityDrum te = (TileEntityDrum) world.getBlockTileEntity(x, y, z);
		information.add(Fluid.getLocalizedName(Fluid.fluidsList[te.type]) + ": " + te.fill + "mB/" + te.capacity + "mB");
		colors.add(0xFFFFFF);
	}
}
