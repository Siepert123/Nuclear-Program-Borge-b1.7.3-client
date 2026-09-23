package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNReceiverTE;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.te.TileEntityTurbineSimple;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import java.util.List;

public class BlockTurbineSimple extends BlockContainer implements IOverlayInfo {
	public BlockTurbineSimple(int blockID, Material material) {
		super(blockID, material);

		BlockFluidPipe.enableConnection(blockID);
		BlockCable.enableConnection(blockID);
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
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityTurbineSimple();
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		TileEntityTurbineSimple te = (TileEntityTurbineSimple) world.getBlockTileEntity(x, y, z);
		information.add("-> Steam: " + te.tankSteam + "/" + TileEntityTurbineSimple.CAPACITY_STEAM + "mB");
		colors.add(0xFFFFFF);
		information.add("<- Depleted Steam: " + te.tankDepletedSteam + "/" + TileEntityTurbineSimple.CAPACITY_DEPLETED_STEAM + "mB");
		colors.add(0xFFFFFF);
		information.add("<- Energy: " + NumFormat.format(te.energy) + "RF");
		colors.add(0xFFFFFF);
	}
}
