package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.util.OBJRenderHelper;
import dev.siepert.nuclearprogram.world.te.TileEntityDerrick;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderDerrick extends TileEntitySpecialRenderer<TileEntityDerrick> {
	public static final RenderDerrick INSTANCE = new RenderDerrick();
	private RenderDerrick() {}

	@Override
	public String getRenderTexture(TileEntityDerrick te) {
		return OBJInit.oil_derrick_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		OBJRenderHelper.enableMachineLight();
		OBJInit.oil_derrick.callList("Base");
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
