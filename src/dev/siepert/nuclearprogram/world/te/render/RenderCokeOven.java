package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityCokeOven;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderCokeOven extends RenderMachineBase<TileEntityCokeOven> {
	public static final RenderCokeOven INSTANCE = new RenderCokeOven();
	private RenderCokeOven() {
		super(TileEntityCokeOven.class);
	}
	private final Random rnd = new Random();

	@Override
	public String getRenderTexture(TileEntityCokeOven te) {
		return OBJInit.coke_oven_tex;
	}

	@Override
	protected void renderMachine(TileEntityCokeOven te, double x, double y, double z, float partialTick) {
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.coke_oven.callList("Base");
		float anim = (te.isOpen != te.wasOpen) ? (te.isOpen ? partialTick : 1.0F - partialTick) : te.isOpen ? 1.0F : 0.0F;
		if (anim == 0.0F) {
			OBJInit.coke_oven.callList("Door");
		} else {
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.5F + (3F/16F), 0.0F, -1.5F);
			GL11.glRotatef(anim * 100.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.5F - (3F/16F), 0.0F, 1.5F);
			OBJInit.coke_oven.callList("Door");
			GL11.glPopMatrix();
		}
		GL11.glDisable(GL11.GL_LIGHTING);
		float brightness = this.rnd.nextFloat() * 0.05F + 0.95F;
		GL11.glColor3f(brightness, brightness, brightness);
		OBJInit.coke_oven.callList("Fire");
		GL11.glEnable(GL11.GL_LIGHTING);
	}
}
