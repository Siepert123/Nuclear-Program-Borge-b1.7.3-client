package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import org.lwjgl.opengl.GL11;

public class RenderBlockManufactory extends RenderBlockMulti {
	public static final RenderBlockManufactory INSTANCE = new RenderBlockManufactory();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		Minecraft mc = Minecraft.getTheMinecraft();

		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(OBJInit.manufactory_tex));
		GL11.glPushMatrix();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		final float scale = 1.0F / 3.0F;
		GL11.glTranslatef(0.0F, -0.5F, 0.0F);
		GL11.glScalef(scale, scale, scale);
		OBJInit.manufactory.callAllLists();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
		mc.renderEngine.bindTerrainTexture();
	}
}
