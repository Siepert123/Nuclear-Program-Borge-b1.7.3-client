package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityManufactory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderManufactory extends RenderMachineBase<TileEntityManufactory> {
	public RenderManufactory() {
		super(TileEntityManufactory.class);
	}

	@Override
	public String getRenderTexture(TileEntityManufactory te) {
		return OBJInit.manufactory_tex;
	}

	@Override
	protected void renderMachine(TileEntityManufactory te, double x, double y, double z, float partialTick) {
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.manufactory.callList("FlatShading");
		GL11.glShadeModel(GL11.GL_SMOOTH);
		OBJInit.manufactory.callList("SmoothShading");
		float scalar = Math.max(2.0F * MathHelper.sin((Minecraft.getTicksRan() + partialTick) * 0.1F), 0.0F) + 1.0F;
		GL11.glTranslatef(0.0F, 2.75F, 0.0F);
		GL11.glScalef(1.0F, scalar, 1.0F);
		GL11.glTranslatef(0.0F, -2.75F, 0.0F);
		OBJInit.manufactory.callList("Press");
		GL11.glShadeModel(GL11.GL_FLAT);
	}
}
