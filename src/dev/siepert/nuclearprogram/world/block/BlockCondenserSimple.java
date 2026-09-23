package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNReceiverTE;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.te.TileEntityCondenserSimple;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import java.util.List;

public class BlockCondenserSimple extends BlockContainer implements IOverlayInfo {
	public BlockCondenserSimple(int blockID, Material material) {
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
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityCondenserSimple();
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		TileEntityCondenserSimple te = (TileEntityCondenserSimple) world.getBlockTileEntity(x, y, z);
		information.add("-> Depleted Steam: " + te.tankDepletedSteam + "/" + TileEntityCondenserSimple.CAPACITY + "mB");
		colors.add(0xFFFFFF);
		information.add("<- Water: " + te.tankWater + "/" + TileEntityCondenserSimple.CAPACITY + "mB");
		colors.add(0xFFFFFF);
	}
}
