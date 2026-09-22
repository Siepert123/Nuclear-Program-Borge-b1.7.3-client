package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityPBR;
import org.lwjgl.opengl.GL11;

public class RenderPBR extends RenderMachineBase<TileEntityPBR>  {
	public static final RenderPBR INSTANCE = new RenderPBR(TileEntityPBR.class);

	private RenderPBR(Class<TileEntityPBR> type) {
		super(type);
	}

	@Override
	public String getRenderTexture(TileEntityPBR te) {
		return OBJInit.pebble_bed_reactor_tex;
	}

	@Override
	protected void renderMachine(TileEntityPBR te, double x, double y, double z, float partialTick) {
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.pebble_bed_reactor.callAllLists();
	}
}
