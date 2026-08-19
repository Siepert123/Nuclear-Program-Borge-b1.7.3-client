package dev.siepert.nuclearprogram.world.block.render;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraftborge.loader.BlockRenderType;

public class RenderBlockInvisible implements BlockRenderType {
	public static final RenderBlockInvisible INSTANCE = new RenderBlockInvisible();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		if (renderer.overrideBlockIcon != null) {
			block.setBlockBoundsBasedOnState(world, x, y, z);
			renderer.renderStandardBlock(block, x, y, z);
			return true;
		} else return false;
	}
}
