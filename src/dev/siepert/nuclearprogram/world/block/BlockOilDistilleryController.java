package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.node.CNNMultiblockProxy;
import dev.siepert.nuclearprogram.gui.GuiOilDistillery;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNMultiblockProxy;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistilleryController;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockOilDistilleryController extends BlockMulti {
	public BlockOilDistilleryController(int blockID, Material material) {
		super(blockID, material);

		flagEnableEnergyConnection();
		flagEnableFluidConnection();
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 0;
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
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityOilDistilleryController();
		if (meta >= 6) return TileEntityProxy.create(true, true);
		return null;
	}

	@Override
	protected void setFlag(World world, int x, int y, int z) {
		super.setFlag(world, x, y, z);

		CableNet.setNode(world, x, y, z, new CNNMultiblockProxy(world).positioned(x, y, z));
		PipeNet.setNode(world, x, y, z, new PNNMultiblockProxy(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);

		CableNet.setNode(world, x, y, z, null);
		PipeNet.setNode(world, x, y, z, null);
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (!world.multiplayerWorld) {
			int[] core = new int[3];
			this.findCore(world, x, y, z, core);
			TileEntityOilDistilleryController te = (TileEntityOilDistilleryController) world.getBlockTileEntity(core[0], core[1], core[2]);
			Minecraft.getTheMinecraft().displayGuiScreen(new GuiOilDistillery(player.inventory, te));
		}
		return true;
	}
}
