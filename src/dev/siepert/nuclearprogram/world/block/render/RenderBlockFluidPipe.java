package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.world.block.BlockFluidPipe;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraftborge.loader.BlockRenderType;
import net.minecraftborge.loader.EnumFacing;

public class RenderBlockFluidPipe implements BlockRenderType {
	public static final RenderBlockFluidPipe INSTANCE = new RenderBlockFluidPipe();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		boolean[] sides = new boolean[6];
		int ox, oy, oz;
		int count = 0;

		EnumFacing last = null;
		boolean center = false;
		for (EnumFacing side : EnumFacing.VALUES) {

			ox = x + side.getOffsetX();
			oy = y + side.getOffsetY();
			oz = z + side.getOffsetZ();
			if (world.getBlockId(ox, oy, oz) == block.blockID) {
				if (last != null) {
					if (side.getOpposite() != last) {
						center = true;
						last = null;
					}
				}
				if (!center) last = side;

				sides[side.getIndex()] = true;
				count++;

				float size = 0.1874F;
				switch (side) {
					case UP:
						block.setBlockBounds(0.5F-size, 0.5F, 0.5F-size, 0.5F+size, 1.0F, 0.5F+size);
						break;
					case DOWN:
						block.setBlockBounds(0.5F-size, 0.0F, 0.5F-size, 0.5F+size, 0.5F, 0.5F+size);
						break;
					case NORTH:
						block.setBlockBounds(0.0F, 0.5F-size, 0.5F-size, 0.5F, 0.5F+size, 0.5F+size);
						break;
					case EAST:
						block.setBlockBounds(0.5F-size, 0.5F-size, 0.0F, 0.5F+size, 0.5F+size, 0.5F);
						break;
					case SOUTH:
						block.setBlockBounds(0.5F, 0.5F-size, 0.5F-size, 1.0F, 0.5F+size, 0.5F+size);
						break;
					case WEST:
						block.setBlockBounds(0.5F-size, 0.5F-size, 0.5F, 0.5F+size, 0.5F+size, 1.0F);
						break;
				}
				BlockFluidPipe.axis = side.getOpposite().getIndex();
				renderer.renderStandardBlock(block, x, y, z);
			}
		}

		if (last != null) {
			float size = 0.1874F;
			switch (last.getOpposite()) {
				case UP:
					block.setBlockBounds(0.5F-size, 0.5F, 0.5F-size, 0.5F+size, 1.0F, 0.5F+size);
					break;
				case DOWN:
					block.setBlockBounds(0.5F-size, 0.0F, 0.5F-size, 0.5F+size, 0.5F, 0.5F+size);
					break;
				case NORTH:
					block.setBlockBounds(0.0F, 0.5F-size, 0.5F-size, 0.5F, 0.5F+size, 0.5F+size);
					break;
				case EAST:
					block.setBlockBounds(0.5F-size, 0.5F-size, 0.0F, 0.5F+size, 0.5F+size, 0.5F);
					break;
				case SOUTH:
					block.setBlockBounds(0.5F, 0.5F-size, 0.5F-size, 1.0F, 0.5F+size, 0.5F+size);
					break;
				case WEST:
					block.setBlockBounds(0.5F-size, 0.5F-size, 0.5F, 0.5F+size, 0.5F+size, 1.0F);
					break;
			}
			BlockFluidPipe.axis = last.getIndex();
			renderer.renderStandardBlock(block, x, y, z);
		}

		BlockFluidPipe.axis = -1;

		if (center || count == 0) {
			float size2 = 0.2499F;
			BlockFluidPipe.pass = 1;
			block.setBlockBounds(0.5F - size2, 0.5F - size2, 0.5F - size2, 0.5F + size2, 0.5F + size2, 0.5F + size2);
			renderer.renderStandardBlock(block, x, y, z);
			BlockFluidPipe.pass = 0;
		}

		block.setBlockBoundsBasedOnState(world, x, y, z);
		return true;
	}
}
