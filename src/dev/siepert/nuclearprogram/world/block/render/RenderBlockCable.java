package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.world.block.BlockCable;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.BlockRenderType;
import net.minecraftborge.loader.EnumFacing;

public class RenderBlockCable implements BlockRenderType {
	public static final RenderBlockCable INSTANCE = new RenderBlockCable();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	private RenderBlockCable() {}

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		int ox, oy, oz;
		int count = 0;

		EnumFacing last = null;
		boolean center = false;
		boolean flag = false;
		for (EnumFacing side : EnumFacing.VALUES) {
			ox = x + side.getOffsetX();
			oy = y + side.getOffsetY();
			oz = z + side.getOffsetZ();
			if (BlockCable.canConnectCable(world.getBlockId(ox, oy, oz), world.getBlockMetadata(ox, oy, oz))) {
				if (last != null) {
					if (side.getOpposite() != last) {
						center = true;
					}
					last = null;
					flag = true;
				}
				if (!flag) last = side;

				count++;

				float size = 0.15615F;
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
				BlockCable.axis = side.getOpposite().getIndex();
				renderer.renderStandardBlock(block, x, y, z);
			}
		}

		if (last != null) {
			float size = 0.15615F;
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
			BlockCable.axis = last.getIndex();
			renderer.renderStandardBlock(block, x, y, z);
		}

		BlockCable.axis = -1;

		if (center || count == 0 || count > 2) {
			float size2 = 0.1874F;
			BlockCable.pass = 1;
			block.setBlockBounds(0.5F - size2, 0.5F - size2, 0.5F - size2, 0.5F + size2, 0.5F + size2, 0.5F + size2);
			renderer.renderStandardBlock(block, x, y, z);
			BlockCable.pass = 0;
		}

		block.setBlockBoundsBasedOnState(world, x, y, z);
		return true;
	}

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		Tessellator tes = Tessellator.instance;

		RenderBlockSealedDoor.INSTANCE.brightness = brightness;
		RenderBlockSealedDoor.INSTANCE.automaticShading = false;

		tes.startDrawingQuads();
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			float size = 0.15615F;
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
			BlockCable.axis = side.getOpposite().getIndex();
			RenderBlockSealedDoor.INSTANCE.renderFaces(renderer, tes, block, -0.5, -0.5, -0.5);
			BlockCable.axis = -1;
		}

		float size2 = 0.1874F;
		BlockCable.pass = 1;
		block.setBlockBounds(0.5F - size2, 0.5F - size2, 0.5F - size2, 0.5F + size2, 0.5F + size2, 0.5F + size2);
		RenderBlockSealedDoor.INSTANCE.renderFaces(renderer, tes, block, -0.5, -0.5, -0.5);
		BlockCable.pass = 0;
		tes.draw();
	}

	@Override
	public boolean renderIn3D() {
		return true;
	}
}
