package dev.siepert.nuclearprogram.world.block.render;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraftborge.loader.BlockRenderType;

public class RenderBlockPane implements BlockRenderType {
	public static final RenderBlockPane INSTANCE = new RenderBlockPane();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		block.setBlockBoundsBasedOnState(world, x, y, z);
		return renderer.renderStandardBlock(block, x, y, z);
	}
}
