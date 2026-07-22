package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockPane;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.Side;

public class BlockGrate extends Block {
	public BlockGrate(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float size = 0.0625F;
		int meta = world.getBlockMetadata(x, y, z);
		this.setBlockBounds(0.0F, meta * 0.0625F, 0.0F, 1.0F, meta * 0.0625F + size, 1.0F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		float size = 0.03125F;
		this.setBlockBounds(0.0F, 0.5F - size, 0.0F, 1.0F, 0.5F + size, 1.0F);
	}

	@Override
	public Icon getBlockIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.getBlockIconFromSide(side);
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTexture;
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		return side == Side.UP || side == Side.DOWN ? this.blockTexture : BlockInit.blockMetal.blockTextures[BlockMetal.STEEL];
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		if (side == Side.DOWN) {
			world.setBlockMetadataWithNotify(x, y, z, 15);
		} else if (side == Side.UP) {
			world.setBlockMetadataWithNotify(x, y, z, 0);
		} else {
			Minecraft mc = Minecraft.getTheMinecraft();
			MovingObjectPosition cast = mc.objectMouseOver;
			if (cast != null) {
				double hitY = cast.hitVec.yCoord - cast.blockY;
				int meta = MathHelper.floor_double(hitY * 16);
				world.setBlockMetadataWithNotify(x, y, z, meta);
			}
		}
	}

	@Override
	public int getRenderType() {
		return RenderBlockPane.RENDER_TYPE;
	}
}
