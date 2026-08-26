package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityDrainagePipe;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderDrainagePipe extends TileEntitySpecialRenderer<TileEntityDrainagePipe> {
	public static final RenderDrainagePipe INSTANCE = new RenderDrainagePipe();
	private RenderDrainagePipe() {}

	@Override
	public String getRenderTexture(TileEntityDrainagePipe te) {
		return OBJInit.drainage_pipe_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		//OBJRenderHelper.enableMachineLight();
		GL11.glRotatef(BlockMulti.ROTATIONS[te.getBlockMetadata() - BlockMulti.OFFSET] + 180.0F, 0.0F, 1.0F, 0.0F);
		OBJInit.drainage_pipe.callAllLists();
		//OBJRenderHelper.disableMachineLight();
		GL11.glPopMatrix();
	}
}
