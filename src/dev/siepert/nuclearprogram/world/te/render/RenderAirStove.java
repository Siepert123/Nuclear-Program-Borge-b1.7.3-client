package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityAirStove;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderAirStove extends TileEntitySpecialRenderer<TileEntityAirStove> {
	public static final RenderAirStove INSTANCE = new RenderAirStove();
	private RenderAirStove() {}

	@Override
	public String getRenderTexture(TileEntityAirStove te) {
		return OBJInit.air_stove_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.air_stove.callAllLists();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
	}
}
