package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.world.te.TileEntityAnimationTest;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

public class RenderAnimationTest extends TileEntitySpecialRenderer<TileEntityAnimationTest> {
	public static final RenderAnimationTest INSTANCE = new RenderAnimationTest();
	private RenderAnimationTest() {}

	@Override
	public String getRenderTexture(TileEntityAnimationTest te) {
		return super.getRenderTexture(te);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {

	}
}
