package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockHatch;
import dev.siepert.nuclearprogram.world.te.TileEntityHatch;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import org.lwjgl.opengl.GL11;

public class RenderHatch extends TileEntitySpecialRenderer<TileEntityHatch> {
	public static final RenderHatch RENDERER = new RenderHatch();

	private RenderHatch() {}

	private static final RenderBlocks renderer = new RenderBlocks();
	private static final RenderBlockHatch instance = RenderBlockHatch.INSTANCE;

	public static final float DESTINATION_ANGLE = 140.0F;

	private World worldObj;
	private Icon texture;
	public void setTexture(Icon texture) {
		this.texture = texture == null ? BlockInit.hatch.blockTexture : texture;
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		this.worldObj = world;
		this.setTexture(null);
	}

	@Override
	public String getRenderTexture(TileEntityHatch te) {
		return "terrain.png";
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		this.renderDo((TileEntityHatch) te, x, y, z, partialTick);
	}

	private void renderDo(TileEntityHatch te, double x, double y, double z, float pt) {
		RenderHelper.enableStandardItemLighting();
		Tessellator tes = Tessellator.instance;
		float b = this.worldObj != null ? this.worldObj.getLightBrightness(te.xCoord, te.yCoord, te.zCoord) : 1.0F;
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y, z + 0.5);
		GL11.glRotatef((te.getBlockMetadata() & 0b0011) * -90.0F + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, 0.0F, -0.5F);

		GL11.glRotatef(-Easing.OUT_CUBIC.ease(te.getOpen(pt)) * DESTINATION_ANGLE, 1.0F, 0.0F, 0.0F);
		tes.startDrawingQuads();
		tes.setColorOpaque_F(b, b, b);
		instance.renderLid(BlockInit.hatch, tes, renderer, this.texture);
		tes.draw();
		GL11.glTranslatef(0.5F, 0.0F, 0.5F);
		GL11.glRotatef(720.0F * Easing.IN_OUT_QUAD.ease(te.getValve(pt)) + te.valveOffset, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
		tes.startDrawingQuads();
		tes.setColorOpaque_F(b, b, b);
		instance.renderValve(BlockInit.hatch, tes, renderer, this.texture);
		tes.draw();
		GL11.glPopMatrix();
	}
}
