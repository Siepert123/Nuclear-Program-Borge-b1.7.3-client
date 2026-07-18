package dev.siepert.nuclearprogram.world.block.render;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockMetal;
import dev.siepert.nuclearprogram.world.block.BlockSealedDoor;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.BlockRenderType;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.Side;

// I have a lot of regrets
public class RenderBlockSealedDoor implements BlockRenderType {
	public static final RenderBlockSealedDoor INSTANCE = new RenderBlockSealedDoor();
	public static final int RENDER_TYPE = RenderBlocks.allocateRenderType(INSTANCE);

	private static final float B_DOWN = 0.5F;
	private static final float B_UP = 1.0F;
	private static final float B_Z = 0.8F;
	private static final float B_X = 0.6F;

	@Override
	public boolean render(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {
		if (renderer.overrideBlockIcon != null) {
			block.setBlockBoundsBasedOnState(world, x, y, z);
			renderer.renderStandardBlock(block, x, y, z);
			return true;
		}
		Tessellator tes = Tessellator.instance;
		float p = 1.0F / 16.0F;
		float b = world.getLightBrightness(x, y, z);
		tes.setColorOpaque_F(b, b, b);
		int meta = world.getBlockMetadata(x, y, z);
		int facing = meta & 0b0011;
		boolean upper = BlockSealedDoor.isUpperPart(meta);
		Icon textureSteel = BlockInit.blockMetal.blockTextures[BlockMetal.STEEL];
		this.automaticShading = true;
		this.brightness = b;
		switch (facing) {
			case 0:
				block.setBlockBounds(0, 0, 0, p*0.5F, 1.0F, p*1.5F);
				this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);
				block.setBlockBounds(p*15.5F, 0, 0, 1.0F, 1.0F, p*1.5F);
				this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);

				block.setBlockBounds(0, 0, p*8, p*0.5F, 1.0F, p*8.5F);
				this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);
				block.setBlockBounds(p*15.5F, 0, p*8, 1.0F, 1.0F, p*8.5F);
				this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);

				if (upper) {
					block.setBlockBounds(p*0.5F, p*15.5F, 0.0F, p*15.5F, 1.0F, p*1.5F);
					this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);
					block.setBlockBounds(p*0.5F, p*15.5F, p*8.0F, p*15.5F, 1.0F, p*8.5F);
					this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);
				} else {
					block.setBlockBounds(p*0.5F, 0.0F, 0.0F, p*15.5F, p*0.5F, p*1.5F);
					this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);
					block.setBlockBounds(p*0.5F, 0.0F, p*8.0F, p*15.5F, p*0.5F, p*8.5F);
					this.renderFaces(renderer, tes, block, x, y, z-p*0.5F, textureSteel);
				}

				if (!upper) {
					BlockInit.sealedDoor.pass = 2;
					block.setBlockBounds(-0.125F, -0.125F, 0.0F, 1.125F, 2.125F, 0.0625F);
					this.renderFaces(renderer, tes, block, x, y, z - p * 0.5F);
					BlockInit.sealedDoor.pass = 0;
				}
				break;
			case 1:
				block.setBlockBounds(1.0F-p*1.5F, 0, 0, 1.0F, 1.0F, p*0.5F);
				this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);
				block.setBlockBounds(1.0F-p*1.5F, 0, p*15.5F, 1.0F, 1.0F, 1.0F);
				this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);

				block.setBlockBounds(1.0F-p*8.5F, 0, 0, 0.5F, 1.0F, p*0.5F);
				this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);
				block.setBlockBounds(1.0F-p*8.5F, 0, p*15.5F, 0.5F, 1.0F, 1.0F);
				this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);

				if (upper) {
					block.setBlockBounds(1.0F-p*1.5F, 1.0F-p*0.5F, p*0.5F, 1.0F, 1.0F, p*15.5F);
					this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);
					block.setBlockBounds(1.0F-p*8.5F, 1.0F-p*0.5F, p*0.5F, 0.5F, 1.0F, p*15.5F);
					this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);
				} else {
					block.setBlockBounds(1.0F-p*1.5F, 0.0F, p*0.5F, 1.0F, p*0.5F, p*15.5F);
					this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);
					block.setBlockBounds(1.0F-p*8.5F, 0.0F, p*0.5F, 0.5F, p*0.5F, p*15.5F);
					this.renderFaces(renderer, tes, block, x+p*0.5F, y, z, textureSteel);
				}

				if (!upper) {
					BlockInit.sealedDoor.pass = 3;
					block.setBlockBounds(1.0F - 0.0625F, -0.125F, -0.125F, 1.0F, 2.125F, 1.125F);
					this.renderFaces(renderer, tes, block, x + p * 0.5F, y, z);
					BlockInit.sealedDoor.pass = 0;
				}
				break;
			case 2:
				block.setBlockBounds(0, 0, 1.0F-p*1.5F, p*0.5F, 1.0F, 1.0F);
				this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);
				block.setBlockBounds(p*15.5F, 0, 1.0F-p*1.5F, 1.0F, 1.0F, 1.0F);
				this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);

				block.setBlockBounds(0, 0, 1.0F-p*8.5F, p*0.5F, 1.0F, 0.5F);
				this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);
				block.setBlockBounds(p*15.5F, 0, 1.0F-p*8.5F, 1.0F, 1.0F, 0.5F);
				this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);

				if (upper) {
					block.setBlockBounds(p*0.5F, p*15.5F, 1.0F-p*1.5F, p*15.5F, 1.0F, 1.0F);
					this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);
					block.setBlockBounds(p*0.5F, p*15.5F, 1.0F-p*8.5F, p*15.5F, 1.0F, 0.5F);
					this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);
				} else {
					block.setBlockBounds(p*0.5F, 0.0F, 1.0F-p*1.5F, p*15.5F, p*0.5F, 1.0F);
					this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);
					block.setBlockBounds(p*0.5F, 0.0F, 1.0F-p*8.5F, p*15.5F, p*0.5F, 0.5F);
					this.renderFaces(renderer, tes, block, x, y, z+p*0.5F, textureSteel);
				}

				if (!upper) {
					BlockInit.sealedDoor.pass = 2;
					block.setBlockBounds(-0.125F, -0.125F, 1.0F - 0.0625F, 1.125F, 2.125F, 1.0F);
					this.renderFaces(renderer, tes, block, x, y, z + p * 0.5F);
					BlockInit.sealedDoor.pass = 0;
				}
				break;
			case 3:
				block.setBlockBounds(0, 0, 0, p*1.5F, 1.0F, p*0.5F);
				this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);
				block.setBlockBounds(0, 0, p*15.5F, p*1.5F, 1.0F, 1.0F);
				this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);

				block.setBlockBounds(p*8, 0, 0, p*8.5F, 1.0F, p*0.5F);
				this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);
				block.setBlockBounds(p*8, 0, p*15.5F, p*8.5F, 1.0F, 1.0F);
				this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);

				if (upper) {
					block.setBlockBounds(0.0F, 1.0F-p*0.5F, p*0.5F, p*1.5F, 1.0F, p*15.5F);
					this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);
					block.setBlockBounds(p*8, 1.0F-p*0.5F, p*0.5F, p*8.5F, 1.0F, p*15.5F);
					this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);
				} else {
					block.setBlockBounds(0.0F, 0.0F, p*0.5F, p*1.5F, p*0.5F, p*15.5F);
					this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);
					block.setBlockBounds(p*8, 0.0F, p*0.5F, p*8.5F, p*0.5F, p*15.5F);
					this.renderFaces(renderer, tes, block, x-p*0.5F, y, z, textureSteel);
				}

				if (!upper) {
					BlockInit.sealedDoor.pass = 3;
					block.setBlockBounds(0.0F, -0.125F, -0.125F, 0.0625F, 2.125F, 1.125F);
					this.renderFaces(renderer, tes, block, x - p * 0.5F, y, z);
					BlockInit.sealedDoor.pass = 0;
				}
				break;
		}
		this.brightness = 1.0F;
		this.automaticShading = false;
		block.setBlockBoundsBasedOnState(world, x, y, z);
		return true;
	}

	public float brightness = 1.0F;
	public boolean automaticShading = false;
	public void renderFaces(RenderBlocks renderer, Tessellator tes, Block block, double x, double y, double z) {
		float bUp = this.brightness;
		float bDown = this.brightness * (this.automaticShading ? 0.5F : 1.0F);
		float bX = this.brightness * (this.automaticShading ? 0.6F : 1.0F);
		float bZ = this.brightness * (this.automaticShading ? 0.8F : 1.0F);

		tes.setNormal(0.0F, -1.0F, 0.0F);
		tes.setColorOpaque_F(bDown, bDown, bDown);
		renderer.renderBottomFace(block, x, y, z, block.getBlockIconFromSide(Side.DOWN));
		tes.setNormal(0.0F, 1.0F, 0.0F);
		tes.setColorOpaque_F(bUp, bUp, bUp);
		renderer.renderTopFace(block, x, y, z, block.getBlockIconFromSide(Side.UP));
		tes.setNormal(0.0F, 0.0F, -1.0F);
		tes.setColorOpaque_F(bZ, bZ, bZ);
		renderer.renderEastFace(block, x, y, z, block.getBlockIconFromSide(Side.EAST));
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace(block, x, y, z, block.getBlockIconFromSide(Side.WEST));
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		tes.setColorOpaque_F(bX, bX, bX);
		renderer.renderNorthFace(block, x, y, z, block.getBlockIconFromSide(Side.NORTH));
		tes.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderSouthFace(block, x, y, z, block.getBlockIconFromSide(Side.SOUTH));
	}
	public void renderFaces(RenderBlocks renderer, Tessellator tes, Block block, double x, double y, double z, Icon texture) {
		float bUp = this.brightness;
		float bDown = this.brightness * (this.automaticShading ? 0.5F : 1.0F);
		float bX = this.brightness * (this.automaticShading ? 0.6F : 1.0F);
		float bZ = this.brightness * (this.automaticShading ? 0.8F : 1.0F);

		tes.setNormal(0.0F, -1.0F, 0.0F);
		tes.setColorOpaque_F(bDown, bDown, bDown);
		renderer.renderBottomFace(block, x, y, z, texture);
		tes.setNormal(0.0F, 1.0F, 0.0F);
		tes.setColorOpaque_F(bUp, bUp, bUp);
		renderer.renderTopFace(block, x, y, z, texture);
		tes.setNormal(0.0F, 0.0F, -1.0F);
		tes.setColorOpaque_F(bZ, bZ, bZ);
		renderer.renderEastFace(block, x, y, z, texture);
		tes.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace(block, x, y, z, texture);
		tes.setNormal(-1.0F, 0.0F, 0.0F);
		tes.setColorOpaque_F(bX, bX, bX);
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

	public void renderScrew(Block block, Tessellator tes, RenderBlocks renderer, Icon texture) {
		float p = 1.0F / 16.0F;
		this.renderFaces(
				renderer, tes, block, texture,
				p*7.5F, p*7.5F, p*0.0F, p*8.5F, p*8.5F, p*2.5F
		);
		this.renderFaces(
				renderer, tes, block, texture,
				p*7.75F, p*7.5F, p*2.5F, p*8.25F, p*8.5F, p*3.0F
		);
	}
}
