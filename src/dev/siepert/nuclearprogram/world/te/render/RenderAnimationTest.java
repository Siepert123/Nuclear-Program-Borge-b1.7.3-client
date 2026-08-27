package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.world.te.TileEntityAnimationTest;
import net.minecraft.client.Minecraft;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderAnimationTest extends TileEntitySpecialRenderer<TileEntityAnimationTest> {
	public static final RenderAnimationTest INSTANCE = new RenderAnimationTest();
	private RenderAnimationTest() {}

	@Override
	public String getRenderTexture(TileEntityAnimationTest te) {
		return OBJInit.animation_test_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		this.renderAnimationTest((TileEntityAnimationTest) te, Minecraft.getTicksRan(), partialTick);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glShadeModel(GL11.GL_FLAT);
	}

	private void renderAnimationTest(TileEntityAnimationTest te, int time, float pt) {
		OBJInit.animation_test.callList("Base");
		float anim = te.animationTicks - pt;
		if (te.isOpen) {
			if (te.animationTicks == 0) return;
			if (anim > TileEntityAnimationTest.DOME_OPEN_TICKS) {
				float slowdownTicks = anim - TileEntityAnimationTest.DOME_OPEN_TICKS;
				float larp = slowdownTicks / (float) TileEntityAnimationTest.DOME_SLOWDOWN_TICKS;
				GL11.glRotatef((Easing.OUT_QUAD.ease(1.0F-larp) * 360.0F) + te.rotation * TileEntityAnimationTest.ROTATION_SPEED, 0.0F, 1.0F, 0.0F);
			} else {
				float larp = anim / (float) TileEntityAnimationTest.DOME_OPEN_TICKS;
				GL11.glRotatef((Easing.IN_QUART.ease(1.0F-larp) * 180.0F), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(te.rotation * TileEntityAnimationTest.ROTATION_SPEED, 0.0F, 1.0F, 0.0F);
			}
			OBJInit.animation_test.callList("Dome");
		} else {
			if (te.animationTicks == 0) {
				GL11.glRotatef((te.rotation + pt) * TileEntityAnimationTest.ROTATION_SPEED, 0.0F, 1.0F, 0.0F);
			} else {
				if (anim > TileEntityAnimationTest.DOME_SPEEDUP_TICKS) {
					float closeTicks = anim - TileEntityAnimationTest.DOME_SPEEDUP_TICKS;
					float larp = closeTicks / (float) TileEntityAnimationTest.DOME_CLOSE_TICKS;
					GL11.glRotatef((Easing.OUT_QUART.ease(larp) * 180.0F), 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(te.rotation * TileEntityAnimationTest.ROTATION_SPEED, 0.0F, 1.0F, 0.0F);
				} else {
					float larp = anim / (float) TileEntityAnimationTest.DOME_SPEEDUP_TICKS;
					GL11.glRotatef(Easing.IN_CUBIC.ease(1.0F-larp) * 360.0F + te.rotation * TileEntityAnimationTest.ROTATION_SPEED, 0.0F, 1.0F, 0.0F);
				}
			}
			OBJInit.animation_test.callList("Dome");
		}
	}
}
