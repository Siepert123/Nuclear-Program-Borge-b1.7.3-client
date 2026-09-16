package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityTransmissionTower;
import org.lwjgl.opengl.GL11;

public class RenderTransmissionTower extends RenderMachineBase<TileEntityTransmissionTower> {
	public RenderTransmissionTower(Class<TileEntityTransmissionTower> type) {
		super(type);
	}

	@Override
	public String getRenderTexture(TileEntityTransmissionTower te) {
		return OBJInit.transmission_tower_tex;
	}

	@Override
	protected void renderMachine(TileEntityTransmissionTower te, double x, double y, double z, float partialTick) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.transmission_tower.callAllLists();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
