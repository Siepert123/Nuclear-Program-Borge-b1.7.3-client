package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.gui.GuiAlloyKiln;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockAlloyKiln;
import dev.siepert.nuclearprogram.world.te.TileEntityAlloyKiln;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.IconRegister;

public class BlockAlloyKiln extends BlockMulti {
	public BlockAlloyKiln(int blockID, Material material) {
		super(blockID, material);
		noResizeItem[blockID] = true;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityAlloyKiln();
		if (meta >= 6) return TileEntityProxy.create(false, false);
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 1;
		dims[1] = 0;
		dims[2] = 1;
		dims[3] = 0;
		dims[4] = 1;
		dims[5] = 0;
	}
	@Override
	public int getCoreOffset() {
		return 0;
	}


	private final int[] pos = new int[3];

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.getCurrentEquippedItem() != null) return false;
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityAlloyKiln te = (TileEntityAlloyKiln) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			Minecraft.getTheMinecraft().displayGuiScreen(new GuiAlloyKiln(player.inventory, te));
		}
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockAlloyKiln.RENDER_TYPE;
	}
	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = BlockInit.firebricks.blockTexture;
	}
}
