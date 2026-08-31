package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import org.lwjgl.opengl.GL11;

public class RenderBlockBlastFurnace extends RenderBlockMulti {
	public static final RenderBlockBlastFurnace INSTANCE = new RenderBlockBlastFurnace();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		Minecraft mc = Minecraft.getTheMinecraft();

		mc.renderEngine.bindTexture(mc.renderEngine.getTexture(OBJInit.blast_furnace_tex));
		GL11.glPushMatrix();
		final float scale = 1.0F / 3.5F;
		GL11.glTranslatef(0.0F, -0.5F, 0.0F);
		GL11.glScalef(scale, scale, scale);
		OBJInit.blast_furnace.callAllLists();
		GL11.glPopMatrix();
		mc.renderEngine.bindTerrainTexture();
	}
}
