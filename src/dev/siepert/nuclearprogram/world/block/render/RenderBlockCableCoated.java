package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.world.te.TileEntityCableCoated;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.BlockRenderType;

public class RenderBlockCableCoated implements BlockRenderType {
	public static final RenderBlockCableCoated INSTANCE = new RenderBlockCableCoated();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	private RenderBlockCableCoated() {}

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		TileEntityCableCoated te = (TileEntityCableCoated) world.getBlockTileEntity(x, y, z);
		if (te.modelBlockID > 0) {
			renderer.renderStandardBlock(Block.blocksList[te.modelBlockID], x, y, z);
			float size = (0.0625F*1.25F);
			block.setBlockBounds(0.5F-size, -0.001F, 0.5F-size, 0.5F+size, 1.001F, 0.5F+size);
			renderer.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(-0.001F, 0.5F-size, 0.5F-size, 1.001F, 0.5F+size, 0.5F+size);
			renderer.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.5F-size, 0.5F-size, -0.001F, 0.5F+size, 0.5F+size, 1.001F);
			renderer.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			renderer.renderStandardBlock(block, x, y, z);
		}

		return true;
	}

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		RenderBlockSealedDoor.INSTANCE.brightness = brightness;
		RenderBlockSealedDoor.INSTANCE.automaticShading = false;
		Tessellator.instance.startDrawingQuads();
		block.setBlockBoundsForItemRender();
		RenderBlockSealedDoor.INSTANCE.renderFaces(renderer, Tessellator.instance, block, -0.5, -0.5, -0.5);
		Tessellator.instance.draw();
	}

	@Override
	public boolean renderIn3D() {
		return true;
	}
}
