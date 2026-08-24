package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.util.OBJRenderHelper;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistilleryController;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderOilDistilleryController extends TileEntitySpecialRenderer<TileEntityOilDistilleryController> {
	public static final RenderOilDistilleryController INSTANCE = new RenderOilDistilleryController();
	private RenderOilDistilleryController() {}

	@Override
	public String getRenderTexture(TileEntityOilDistilleryController te) {
		return OBJInit.oil_distillery_base_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		OBJRenderHelper.enableMachineLight();
		OBJInit.oil_distillery_base.callList("Base");
		OBJRenderHelper.disableMachineLight();
		GL11.glPopMatrix();
	}
}
