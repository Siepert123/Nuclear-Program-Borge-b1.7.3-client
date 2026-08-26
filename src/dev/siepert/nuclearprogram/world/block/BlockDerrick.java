package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.node.CNNMultiblockProxy;
import dev.siepert.nuclearprogram.gui.GuiDerrick;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.te.TileEntityDerrick;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockDerrick extends BlockMulti implements IOverlayInfo {
	public BlockDerrick(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableEnergyConnection();
		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityDerrick();
		if (meta >= 6) return TileEntityProxy.create(false, true);
		return null;
	}

	/**
	 * Writes multiblock dimensions to array.
	 * Ordered as UP-DOWN-FORWARD-BACKWARD-LEFT-RIGHT.
	 * @param dims The dimensions array to write to
	 */
	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 4;
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
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (!world.multiplayerWorld) {
			int[] core = new int[3];
			this.findCore(world, x, y, z, core);
			TileEntityDerrick te = (TileEntityDerrick) world.getBlockTileEntity(core[0], core[1], core[2]);
			Minecraft.getTheMinecraft().displayGuiScreen(new GuiDerrick(player.inventory, te));
		}
		return true;
	}

	@Override
	protected void setFlag(World world, int x, int y, int z) {
		super.setFlag(world, x, y, z);

		CableNet.setNode(world, x, y, z, new CNNMultiblockProxy(world).positioned(x, y, z));
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (this.hasFlag(metadata)) {
			PipeNetNode node = PipeNet.getNode(x, y, z);
			if (node != null) {
				information.add("Network ID: " + node.network);
				colors.add(0x00FFFF);
			} else {
				information.add("(no attached PipeNet node)");
				colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
			}
		}
	}
}
