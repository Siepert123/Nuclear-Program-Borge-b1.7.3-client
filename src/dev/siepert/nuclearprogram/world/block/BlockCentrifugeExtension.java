package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

import java.util.Random;

public class BlockCentrifugeExtension extends Block {
	public BlockCentrifugeExtension(int blockID) {
		super(blockID, NPMaterials.multiblock);
		this.disableStats();
		this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
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
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		Block below = Block.blocksList[world.getBlockId(x, y-1, z)];
		return below != null && below.blockActivated(world, x, y - 1, z, player);
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		if (world.getBlockId(x, y+1, z) == this.blockID) world.setBlockWithNotify(x, y+1, z, 0);
		if (world.getBlockId(x, y-1, z) == this.blockID) world.setBlockWithNotify(x, y-1, z, 0);
		else if (world.getBlockId(x, y-1, z) != 0) {
			int blockID = world.getBlockId(x, y-1, z);
			int metadata = world.getBlockMetadata(x, y-1, z);
			world.setBlockWithNotify(x, y-1, z, 0);
			world.playEvent(2001, x, y-1, z, blockID | (metadata << 12));
			Block.blocksList[blockID].dropBlockAsItem(world, x, y-1, z, metadata);
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return 0;
	}

	@Override
	public int getRenderType() {
		return -1;
	}
}
