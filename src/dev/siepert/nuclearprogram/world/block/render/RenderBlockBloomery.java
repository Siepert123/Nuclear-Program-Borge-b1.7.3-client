package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.world.block.BlockBloomery;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.BlockRenderType;
import net.minecraftborge.loader.Side;
import org.lwjgl.opengl.GL11;

public class RenderBlockBloomery implements BlockRenderType {
	public static final RenderBlockBloomery INSTANCE = new RenderBlockBloomery();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	private static final float thickness = 0.125F;

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.renderStandardBlock(block, x, y, z);
		((BlockBloomery)block).renderPass = 1;

		block.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
		renderer.renderStandardBlock(block, x, y+1, z);
		block.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.875F, 0.875F);
		renderer.renderStandardBlock(block, x, y+2, z);

		block.setBlockBounds(0.125F, 0.875F, 0.125F, 0.125F+thickness, 1.0F, 0.875F);
		renderer.renderStandardBlock(block, x, y+2, z);
		block.setBlockBounds(0.875F-thickness, 0.875F, 0.125F, 0.875F, 1.0F, 0.875F);
		renderer.renderStandardBlock(block, x, y+2, z);
		block.setBlockBounds(0.125F+thickness, 0.875F, 0.125F, 0.875F-thickness, 1.0F, 0.125F+thickness);
		renderer.renderStandardBlock(block, x, y+2, z);
		block.setBlockBounds(0.125F+thickness, 0.875F, 0.875F-thickness, 0.875F-thickness, 1.0F, 0.875F);
		renderer.renderStandardBlock(block, x, y+2, z);

		((BlockBloomery)block).renderPass = 0;
		block.setBlockBoundsBasedOnState(world, x, y, z);
		return true;
	}

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderBottomFace(block, 0, 0, 0, block.getBlockIconFromSideAndMetadata(Side.DOWN, 0));
		tes.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockIconFromSideAndMetadata(Side.UP, 0));
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockIconFromSideAndMetadata(Side.EAST, 0));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockIconFromSideAndMetadata(Side.WEST, 0));
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockIconFromSideAndMetadata(Side.NORTH, 0));
		tes.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockIconFromSideAndMetadata(Side.SOUTH, 0));
		tes.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderIn3D() {
		return true;
	}
}
