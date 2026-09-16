package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.te.TileEntityRBMKControl;
import net.minecraft.src.*;
import net.minecraftborge.loader.BorgeMath;
import net.minecraftborge.loader.Side;
import org.lwjgl.opengl.GL11;

public class RenderRBMKControl extends TileEntitySpecialRenderer<TileEntityRBMKControl> {
	private static final RenderBlocks RENDER_BLOCKS = new RenderBlocks();
	public static final RenderRBMKControl INSTANCE = new RenderRBMKControl();
	private RenderRBMKControl() {}

	@Override
	public String getRenderTexture(TileEntityRBMKControl te) {
		return "terrain.png";
	}

	private World worldObj = null;
	@Override
	public void setWorld(World world) {
		this.worldObj = world;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		Block block = BlockInit.rbmkControl;

		TileEntityRBMKControl rbmk = (TileEntityRBMKControl) te;
		float ext = BorgeMath.clampedLerp(rbmk.controlOld, rbmk.control, partialTick);
		y = y + ext + 7;

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		if (this.worldObj != null) {
			float brightness = this.worldObj.getLightBrightness(te.xCoord, te.yCoord + 7, te.zCoord);
			tes.setColorOpaque_F(brightness, brightness, brightness);
		} else tes.setColorOpaque_I(0xFFFFFF);

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		tes.setNormal(0.0F, -1.0F, 0.0F);
		RENDER_BLOCKS.renderBottomFace(block, x, y, z, block.getBlockIconFromSide(Side.DOWN));
		tes.setNormal(0.0F, 1.0F, 0.0F);
		RENDER_BLOCKS.renderTopFace(block, x, y, z, block.getBlockIconFromSide(Side.UP));
		tes.setNormal(0.0F, 0.0F, -1.0F);
		RENDER_BLOCKS.renderEastFace(block, x, y, z, block.getBlockIconFromSide(Side.EAST));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		RENDER_BLOCKS.renderWestFace(block, x, y, z, block.getBlockIconFromSide(Side.WEST));
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		RENDER_BLOCKS.renderNorthFace(block, x, y, z, block.getBlockIconFromSide(Side.NORTH));
		tes.setNormal(1.0F, 0.0F, 0.0F);
		RENDER_BLOCKS.renderSouthFace(block, x, y, z, block.getBlockIconFromSide(Side.SOUTH));

		y--;
		block.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
		tes.setNormal(0.0F, 0.0F, -1.0F);
		RENDER_BLOCKS.renderEastFace(block, x, y, z, block.getBlockIconFromSide(Side.EAST));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		RENDER_BLOCKS.renderWestFace(block, x, y, z, block.getBlockIconFromSide(Side.WEST));
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		RENDER_BLOCKS.renderNorthFace(block, x, y, z, block.getBlockIconFromSide(Side.NORTH));
		tes.setNormal(1.0F, 0.0F, 0.0F);
		RENDER_BLOCKS.renderSouthFace(block, x, y, z, block.getBlockIconFromSide(Side.SOUTH));

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		tes.draw();
	}
}
