package dev.siepert.nuclearprogram.world.block.render;

import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;

public class RenderBlockManufactory extends RenderBlockMulti {
	public static final RenderBlockManufactory INSTANCE = new RenderBlockManufactory();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		super.renderOnInventory(block, metadata, brightness, renderer);
	}
}
