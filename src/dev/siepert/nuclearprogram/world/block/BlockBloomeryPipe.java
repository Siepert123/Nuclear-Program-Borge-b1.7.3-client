package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockBloomeryPipe;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.Side;

public class BlockBloomeryPipe extends Block {
	public BlockBloomeryPipe(int blockID) {
		super(blockID, Material.rock);
		this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
		this.setLightOpacity(0);
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		return side == Side.UP || side == Side.DOWN ? BlockInit.bloomeryIdle.blockTextureTop : BlockInit.bloomeryIdle.blockTextureSide;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
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
	public int getRenderType() {
		return RenderBlockBloomeryPipe.RENDER_TYPE;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		this.propagateAdded(world, x, y, z);
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		this.propagateRemoval(world, x, y, z);
	}

	protected void propagateAdded(World world, int x, int y, int z) {
		int blockBelow = world.getBlockId(x, y-1, z);
		if (blockBelow > 0) {
			if (blockBelow == this.blockID) {
				this.propagateAdded(world, x, y - 1, z);
			} else {
				Block below = Block.blocksList[blockBelow];
				if (below instanceof BlockBloomery) {
					((BlockBloomery)below).notifyPipeUpdate(world, x, y-1, z);
				}
			}
		}
	}
	protected void propagateRemoval(World world, int x, int y, int z) {
		int blockBelow = world.getBlockId(x, y-1, z);
		if (blockBelow > 0) {
			if (blockBelow == this.blockID) {
				this.propagateRemoval(world, x, y - 1, z);
			} else {
				Block below = Block.blocksList[blockBelow];
				if (below instanceof BlockBloomery) {
					((BlockBloomery)below).notifyPipeUpdate(world, x, y-1, z);
				}
			}
		}
	}
}
