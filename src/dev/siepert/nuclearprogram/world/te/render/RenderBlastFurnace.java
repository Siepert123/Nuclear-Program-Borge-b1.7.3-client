package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.te.TileEntityBlastFurnace;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RenderBlastFurnace extends RenderMachineBase<TileEntityBlastFurnace> {
	public static final RenderBlastFurnace INSTANCE = new RenderBlastFurnace();
	private RenderBlastFurnace() {
		super(TileEntityBlastFurnace.class);
	}

	@Override
	public String getRenderTexture(TileEntityBlastFurnace te) {
		return OBJInit.blast_furnace_tex;
	}

	@Override
	protected void renderMachine(TileEntityBlastFurnace te, double x, double y, double z, float partialTick) {
		OBJInit.blast_furnace.callAllLists();
	}
}
