package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityAlloyKiln;
import org.lwjgl.opengl.GL11;

public class RenderAlloyKiln extends RenderMachineBase<TileEntityAlloyKiln> {
	public static final RenderAlloyKiln INSTANCE = new RenderAlloyKiln();
	private RenderAlloyKiln() {
		super(TileEntityAlloyKiln.class);
	}

	@Override
	public String getRenderTexture(TileEntityAlloyKiln te) {
		return OBJInit.alloy_kiln_tex;
	}

	@Override
	protected void renderMachine(TileEntityAlloyKiln te, double x, double y, double z, float partialTick) {
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()) + 180.0F, 0.0F, 1.0F, 0.0F);
		OBJInit.alloy_kiln.callList("Kiln");
		GL11.glDisable(GL11.GL_LIGHTING);
		float b = te.isBurning() ? 1.0F : 0.0F;
		GL11.glColor3f(b, b, b);
		OBJInit.alloy_kiln.callList("Fire");
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glShadeModel(GL11.GL_FLAT);
	}
}
