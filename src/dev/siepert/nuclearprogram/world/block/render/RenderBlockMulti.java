package dev.siepert.nuclearprogram.world.block.render;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraftborge.loader.BlockRenderType;

public abstract class RenderBlockMulti implements BlockRenderType {
	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		if (renderer.overrideBlockIcon != null) {
			renderer.renderStandardBlock(block, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public boolean renderIn3D() {
		return true;
	}
}
