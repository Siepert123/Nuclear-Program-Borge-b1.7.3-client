package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import org.lwjgl.opengl.GL11;

public class RenderBlockTransmissionTower extends RenderBlockMulti {
	public static final RenderBlockTransmissionTower INSTANCE = new RenderBlockTransmissionTower();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		Minecraft mc = Minecraft.getTheMinecraft();

		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(OBJInit.transmission_tower_tex));
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		final float scale = 1.0F / 12.0F;
		GL11.glTranslatef(0.0F, -0.5F, 0.0F);
		GL11.glScalef(scale, scale, scale);
		OBJInit.transmission_tower.callAllLists();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		mc.renderEngine.bindTerrainTexture();
	}
}
