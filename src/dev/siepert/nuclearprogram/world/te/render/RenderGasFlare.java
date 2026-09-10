package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.te.TileEntityGasFlare;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderGasFlare extends RenderMachineBase<TileEntityGasFlare> {
	public static final RenderGasFlare INSTANCE = new RenderGasFlare();
	private RenderGasFlare() {
		super(TileEntityGasFlare.class);
	}

	@Override
	public String getRenderTexture(TileEntityGasFlare te) {
		return OBJInit.gas_flare_tex;
	}

	@Override
	protected void renderMachine(TileEntityGasFlare te, double x, double y, double z, float partialTick) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		OBJInit.gas_flare.callAllLists();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
