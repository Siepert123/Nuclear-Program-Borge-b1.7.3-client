package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import org.lwjgl.opengl.GL11;

public class RenderBlockAlloyKiln extends RenderBlockMulti {
	public static final RenderBlockAlloyKiln INSTANCE = new RenderBlockAlloyKiln();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		Minecraft mc = Minecraft.getTheMinecraft();

		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(OBJInit.alloy_kiln_tex));
		GL11.glPushMatrix();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		final float scale = 1.0F / 1.8F;
		GL11.glTranslatef(0.25F, -0.5F, 0.25F);
		GL11.glScalef(scale, scale, scale);
		OBJInit.alloy_kiln.callList("Kiln");
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		OBJInit.alloy_kiln.callList("Fire");
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
		mc.renderEngine.bindTerrainTexture();
	}
}
