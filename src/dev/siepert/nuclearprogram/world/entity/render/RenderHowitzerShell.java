package dev.siepert.nuclearprogram.world.entity.render;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.src.Entity;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderHowitzerShell extends Render {
	public static final String TEXTURE = "mod_logo/" + NuclearProgram.MODID + ".png";

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		float scalar = 2.0F;
		GL11.glScalef(scalar, scalar, scalar);
		this.renderManager.renderEngine.bindTexture(this.renderManager.renderEngine.getTexture(TEXTURE));
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		tes.setNormal(0.0F, 1.0F, 0.0F);
		tes.addVertexWithUV(-0.5, -0.25, 0.0F, 0.0, 1.0);
		tes.addVertexWithUV(0.5, -0.25, 0.0F, 1.0, 1.0);
		tes.addVertexWithUV(0.5, 0.75, 0.0F, 1.0, 0.0);
		tes.addVertexWithUV(-0.5, 0.75, 0.0F, 0.0, 0.0);
		tes.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
}
