package dev.siepert.nuclearprogram.world.block.render;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.BlockRenderType;
import net.minecraftborge.loader.Icon;
import org.lwjgl.opengl.GL11;

public class RenderBlockHatch implements BlockRenderType {
	public static final RenderBlockHatch INSTANCE = new RenderBlockHatch();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		if (renderer.overrideBlockIcon != null) {
			block.setBlockBoundsBasedOnState(world, x, y, z);
			renderer.renderStandardBlock(block, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public void renderOnInventory(Block block, int metadata, float brightness, RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
		Tessellator tes = Tessellator.instance;
		Icon texture = block.blockTexture;
		tes.startDrawingQuads();
		tes.setColorOpaque_F(brightness, brightness, brightness);
		this.renderLid(block, tes, renderer, texture);
		this.renderValve(block, tes, renderer, texture);
		tes.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public void renderFaces(RenderBlocks renderer, Tessellator tes, Block block, double x, double y, double z, Icon texture) {
		tes.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderBottomFace(block, x, y, z, texture);
		tes.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderTopFace(block, x, y, z, texture);
		tes.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderEastFace(block, x, y, z, texture);
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace(block, x, y, z, texture);
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderNorthFace(block, x, y, z, texture);
		tes.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderSouthFace(block, x, y, z, texture);
	}
	public void renderFaces(RenderBlocks renderer, Tessellator tes, Block block, Icon texture) {
		this.renderFaces(renderer, tes, block, 0.0, 0.0, 0.0, texture);
	}
	public void renderFaces(
			RenderBlocks renderer, Tessellator tes, Block block, Icon texture,
			float minX, float minY, float minZ, float maxX, float maxY, float maxZ
	) {
		block.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		this.renderFaces(renderer, tes, block, texture);
	}

	public void renderLid(Block block, Tessellator tes, RenderBlocks renderer, Icon texture) {
		float p = 1.0F / 16.0F;
		block.setBlockBounds(p*2, p*1, p*2, p*14, p*3, p*14);
		this.renderFaces(renderer, tes, block, 0, 0, 0, texture);
		block.setBlockBounds(p*0, p*0, p*2, p*2, p*2, p*14);
		this.renderFaces(renderer, tes, block, 0, 0, 0, texture);
		block.setBlockBounds(p*14, p*0, p*2, p*16, p*2, p*14);
		this.renderFaces(renderer, tes, block, 0, 0, 0, texture);
		block.setBlockBounds(p*0, p*0, p*0, p*16, p*2, p*2);
		this.renderFaces(renderer, tes, block, 0, 0, 0, texture);
		block.setBlockBounds(p*0, p*0, p*14, p*16, p*2, p*16);
		this.renderFaces(renderer, tes, block, 0, 0, 0, texture);
	}

	public void renderValve(Block block, Tessellator tes, RenderBlocks renderer, Icon texture) {
		float p = 1.0F / 16.0F;
		tes.setTranslationD(0.0, -0.5 + p*2, 0.0);

		this.renderFaces(
				renderer, tes, block, texture,
				p*7.5F, p*6.0F, p*7.5F, p*8.5F, p*10.0F, p*8.5F
		);


		this.renderFaces(
				renderer, tes, block, texture,
				p*6.5F, p*10.0F, p*7.5F, p*9.5F, p*11.0F, p*8.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*7.5F, p*10.0F, p*6.5F, p*8.5F, p*11.0F, p*9.5F
		);

		this.renderFaces(
				renderer, tes, block, texture,
				p*5.5F, p*10.5F, p*6.5F, p*6.5F, p*11.5F, p*9.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*9.5F, p*10.5F, p*6.5F, p*10.5F, p*11.5F, p*9.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*5.5F, p*10.5F, p*5.5F, p*10.5F, p*11.5F, p*6.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*5.5F, p*10.5F, p*9.5F, p*10.5F, p*11.5F, p*10.5F
		);


		this.renderFaces(
				renderer, tes, block, texture,
				p*6.5F, p*5.0F, p*7.5F, p*9.5F, p*6.0F, p*8.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*7.5F, p*5.0F, p*6.5F, p*8.5F, p*6.0F, p*9.5F
		);

		this.renderFaces(
				renderer, tes, block, texture,
				p*5.5F, p*4.5F, p*6.5F, p*6.5F, p*5.5F, p*9.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*9.5F, p*4.5F, p*6.5F, p*10.5F, p*5.5F, p*9.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*5.5F, p*4.5F, p*5.5F, p*10.5F, p*5.5F, p*6.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*5.5F, p*4.5F, p*9.5F, p*10.5F, p*5.5F, p*10.5F
		);

		tes.setTranslationD(0.0, 0.0, 0.0);
	}

	@Override
	public boolean renderIn3D() {
		return true;
	}
}
