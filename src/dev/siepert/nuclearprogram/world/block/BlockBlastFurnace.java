package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.gui.GuiBlastFurnace;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNMultiblockProxy;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockBlastFurnace;
import dev.siepert.nuclearprogram.world.te.TileEntityBlastFurnace;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.IconRegister;

public class BlockBlastFurnace extends BlockMulti {
	public BlockBlastFurnace(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityBlastFurnace();
		if (meta >= 6) return TileEntityProxy.create(true, false);
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 6;
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
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (this.findCore(world, x, y, z, this.pos)) {
			if (!world.multiplayerWorld) {
				TileEntityBlastFurnace te = (TileEntityBlastFurnace) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
				Minecraft.getTheMinecraft().displayGuiScreen(new GuiBlastFurnace(player.inventory, te));
			}
			return true;
		} else return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockBlastFurnace.RENDER_TYPE;
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = BlockInit.firebricks.blockTexture;
	}
}
