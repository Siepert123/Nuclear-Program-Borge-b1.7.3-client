package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.util.OBJRenderHelper;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistillerySegment;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderOilDistillerySegment extends TileEntitySpecialRenderer<TileEntityOilDistillerySegment> {
	public static final RenderOilDistillerySegment INSTANCE = new RenderOilDistillerySegment();
	private RenderOilDistillerySegment() {}

	@Override
	public String getRenderTexture(TileEntityOilDistillerySegment te) {
		return OBJInit.oil_distillery_segment_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		OBJRenderHelper.enableMachineLight();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		OBJInit.oil_distillery_segment.callAllLists();
		GL11.glPopMatrix();
		OBJRenderHelper.disableMachineLight();
	}
}
