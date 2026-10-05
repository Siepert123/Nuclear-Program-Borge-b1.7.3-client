package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.node.CNNMultiblockProxy;
import dev.siepert.nuclearprogram.gui.GuiManufactory;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockManufactory;
import dev.siepert.nuclearprogram.world.te.TileEntityManufactory;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

public class BlockManufactory extends BlockMulti {
	public BlockManufactory(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableEnergyConnection();
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 2;
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

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					this.setFlag(world, x + i, y, z + j);
				}
			}
		}
	}
	@Override
	protected void setFlag(World world, int x, int y, int z) {
		super.setFlag(world, x, y, z);

		CableNet.setNode(world, x, y, z, new CNNMultiblockProxy(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);

		CableNet.setNode(world, x, y, z, null);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityManufactory();
		if (meta >= 6) return TileEntityProxy.create(false, true);
		return null;
	}

	private final int[] pos = new int[3];
	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.getCurrentEquippedItem() != null) return false;
		if (this.findCore(world, x, y, z, this.pos)) {
			if (!world.multiplayerWorld) {
				TileEntityManufactory te = (TileEntityManufactory) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
				Minecraft.getTheMinecraft().displayGuiScreen(new GuiManufactory(player.inventory, te));
			}
			return true;
		}
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockManufactory.RENDER_TYPE;
	}
}
