package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityCokeOven;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderCokeOven extends TileEntitySpecialRenderer<TileEntityCokeOven> {
	public static final RenderCokeOven INSTANCE = new RenderCokeOven();
	private RenderCokeOven() {}

	@Override
	public String getRenderTexture(TileEntityCokeOven te) {
		return OBJInit.coke_oven_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.coke_oven.callList("Base");
		TileEntityCokeOven oven = (TileEntityCokeOven) te;
		float anim = (oven.isOpen != oven.wasOpen) ? (oven.isOpen ? partialTick : 1.0F - partialTick) : oven.isOpen ? 1.0F : 0.0F;
		if (anim == 0.0F) {
			OBJInit.coke_oven.callList("DoorL");
			OBJInit.coke_oven.callList("DoorR");
		} else {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.0F, -1.5F);
			GL11.glRotatef(anim * 100.0F, 0.0F, -1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, 0.0F, 1.5F);
			OBJInit.coke_oven.callList("DoorL");
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.5F, 0.0F, -1.5F);
			GL11.glRotatef(anim * 100.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.5F, 0.0F, 1.5F);
			OBJInit.coke_oven.callList("DoorR");
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
}
