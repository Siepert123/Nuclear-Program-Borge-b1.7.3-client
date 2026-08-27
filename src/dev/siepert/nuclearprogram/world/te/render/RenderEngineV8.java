package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityEngineV8;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderEngineV8 extends TileEntitySpecialRenderer<TileEntityEngineV8> {
	public static final RenderEngineV8 INSTANCE = new RenderEngineV8();
	private RenderEngineV8() {}

	@Override
	public String getRenderTexture(TileEntityEngineV8 te) {
		return OBJInit.combustion_engine_v8_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		TileEntityEngineV8 engine = (TileEntityEngineV8) te;
		float anim = (engine.running ? engine.animation + partialTick : engine.animation) * engine.getAnimationSpeed();
		float extensionsR1 = (MathHelper.sin((anim + 0.0F) / 180.0F * (float) Math.PI) + 1) * 0.5F * 0.25F;
		float extensionsR2 = (MathHelper.sin((anim + 180.0F) / 180.0F * (float) Math.PI) + 1) * 0.5F * 0.25F;
		float extensionsL1 = (MathHelper.sin((anim + 90.0F) / 180.0F * (float) Math.PI) + 1) * 0.5F * 0.25F;
		float extensionsL2 = (MathHelper.sin((anim + 270.0F) / 180.0F * (float) Math.PI) + 1) * 0.5F * 0.25F;

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.combustion_engine_v8.callList("Base");

		GL11.glTranslatef(-extensionsR1, extensionsR1, 0.0F);
		OBJInit.combustion_engine_v8.callList("PistonsR1");
		GL11.glTranslatef(extensionsR1, -extensionsR1, 0.0F);

		GL11.glTranslatef(-extensionsR2, extensionsR2, 0.0F);
		OBJInit.combustion_engine_v8.callList("PistonsR2");
		GL11.glTranslatef(extensionsR2, -extensionsR2, 0.0F);

		GL11.glTranslatef(extensionsL1, extensionsL1, 0.0F);
		OBJInit.combustion_engine_v8.callList("PistonsL1");
		GL11.glTranslatef(-extensionsL1, -extensionsL1, 0.0F);

		GL11.glTranslatef(extensionsL2, extensionsL2, 0.0F);
		OBJInit.combustion_engine_v8.callList("PistonsL2");
		GL11.glTranslatef(-extensionsL2, -extensionsL2, 0.0F);

		GL11.glPopMatrix();
	}
}
