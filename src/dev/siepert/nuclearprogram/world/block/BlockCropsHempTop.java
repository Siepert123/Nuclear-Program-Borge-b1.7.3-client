package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.World;
import net.minecraftborge.loader.IconRegister;

import java.util.Random;

public class BlockCropsHempTop extends BlockFlower {
	public BlockCropsHempTop(int blockID) {
		super(blockID, 0);
		float size = 0.5F - 0.0625F;
		this.setBlockBounds(0.5F-size, 0.0F, 0.5F-size, 0.5F+size, 0.5F, 0.5F+size);
	}

	@Override
	public String getRegistryName() {
		return this.getSimpleName() + "_top";
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = register.getTexture(this.getSimpleName() + "_top", 16, 16);
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return this.canPlaceBlockAt(world, x, y, z);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.getBlockId(x, y-1, z) == BlockInit.hemp.blockID;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		if (!this.canPlaceBlockAt(world, x, y, z)) {
			world.playEvent(2001, x, y, z, this.blockID);
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return ItemInit.hempFibers.shiftedIndex;
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(3) + 2;
	}

	@Override
	public int getRenderType() {
		return 6;
	}
}
