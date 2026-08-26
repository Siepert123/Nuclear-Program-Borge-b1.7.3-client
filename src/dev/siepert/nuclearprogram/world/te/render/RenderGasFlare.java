package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.te.TileEntityGasFlare;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderGasFlare extends TileEntitySpecialRenderer<TileEntityGasFlare> {
	public static final RenderGasFlare INSTANCE = new RenderGasFlare();
	private RenderGasFlare() {}

	@Override
	public String getRenderTexture(TileEntityGasFlare te) {
		return OBJInit.gas_flare_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		//OBJRenderHelper.enableMachineLight();
		OBJInit.gas_flare.callAllLists();
		//OBJRenderHelper.disableMachineLight();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
