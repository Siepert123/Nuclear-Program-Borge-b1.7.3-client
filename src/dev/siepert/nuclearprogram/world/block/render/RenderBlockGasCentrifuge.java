package dev.siepert.nuclearprogram.world.block.render;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraftborge.loader.BlockRenderType;

public class RenderBlockGasCentrifuge implements BlockRenderType {
	public static final RenderBlockGasCentrifuge INSTANCE = new RenderBlockGasCentrifuge();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		block.setBlockBoundsBasedOnState(world, x, y, z);
		renderer.renderStandardBlock(block, x, y, z);
		for (int i = 1; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++){
					float ox = j * 0.5F;
					float oz = k * 0.5F;
					block.setBlockBounds(0.125F + ox, 0.0F, 0.125F + oz, 0.375F + ox, 1.0F, 0.375F + oz);
					renderer.renderStandardBlock(block, x, y+i, z);
				}
			}
		}
		block.setBlockBoundsBasedOnState(world, x, y, z);
		return true;
	}
}
