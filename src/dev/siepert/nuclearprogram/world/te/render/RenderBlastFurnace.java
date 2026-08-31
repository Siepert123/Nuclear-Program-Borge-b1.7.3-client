package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.te.TileEntityBlastFurnace;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderBlastFurnace extends TileEntitySpecialRenderer<TileEntityBlastFurnace> {
	public static final RenderBlastFurnace INSTANCE = new RenderBlastFurnace();
	private RenderBlastFurnace() {}

	@Override
	public String getRenderTexture(TileEntityBlastFurnace te) {
		return OBJInit.blast_furnace_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		OBJInit.blast_furnace.callAllLists();
		GL11.glPopMatrix();
	}
}
