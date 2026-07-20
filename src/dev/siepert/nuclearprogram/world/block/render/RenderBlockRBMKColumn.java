package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.BlockRenderType;
import org.lwjgl.opengl.GL11;

public class RenderBlockRBMKColumn implements BlockRenderType {
	public static final RenderBlockRBMKColumn INSTANCE = new RenderBlockRBMKColumn();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		if (renderer.overrideBlockIcon == null && block == BlockInit.rbmkControl && world.getBlockMetadata(x, y, z) == 6) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.875F, 1.0F);
			renderer.renderStandardBlock(block, x, y, z);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			renderer.renderStandardBlock(block, x, y, z);
		}
		return true;
	}

	@Override
	public void renderOnInventory(Block block, int meta, float brightness, RenderBlocks renderer) {
		Tessellator tes = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		tes.startDrawingQuads();
		tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderBottomFace(block, -0.5, -0.5, -0.5, block.getBlockIconFromSideAndMetadata(0, meta));
		tes.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderTopFace(block, -0.5, -0.5, -0.5, block.getBlockIconFromSideAndMetadata(1, meta));
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderEastFace(block, -0.5, -0.5, -0.5, block.getBlockIconFromSideAndMetadata(2, meta));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace(block, -0.5, -0.5, -0.5, block.getBlockIconFromSideAndMetadata(3, meta));
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderNorthFace(block, -0.5, -0.5, -0.5, block.getBlockIconFromSideAndMetadata(4, meta));
		tes.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderSouthFace(block, -0.5, -0.5, -0.5, block.getBlockIconFromSideAndMetadata(5, meta));
		tes.draw();
	}

	@Override
	public boolean renderIn3D() {
		return true;
	}
}
