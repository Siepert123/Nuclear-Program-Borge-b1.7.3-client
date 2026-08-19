package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.te.TileEntityGasCentrifuge;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderGasCentrifuge extends TileEntitySpecialRenderer<TileEntityGasCentrifuge> {
	public static final RenderGasCentrifuge INSTANCE = new RenderGasCentrifuge();

	private RenderGasCentrifuge() {}

	@Override
	public String getRenderTexture(TileEntityGasCentrifuge te) {
		return OBJInit.gas_centrifuge_tex;
	}

	private final Random rnd = new Random();
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		boolean vibrate = ((TileEntityGasCentrifuge)te).progress > 0;

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		OBJInit.gas_centrifuge.callList("Base");
		for (int i = 1; i <= 4; i++) {
			if (vibrate) {
				GL11.glPushMatrix();
				GL11.glTranslatef((this.rnd.nextFloat() - 0.5F) * 0.0625F, 0.0F, (this.rnd.nextFloat() - 0.5F) * 0.0625F);
			}
			OBJInit.gas_centrifuge.callList("Centrifuge" + i);
			if (vibrate) {
				GL11.glPopMatrix();
			}
		}
		GL11.glPopMatrix();
		GL11.glShadeModel(GL11.GL_FLAT);
	}
}
