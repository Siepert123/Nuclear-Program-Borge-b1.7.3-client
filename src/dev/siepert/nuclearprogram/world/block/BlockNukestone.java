package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.util.RandomTextures;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Random;

public class BlockNukestone extends Block {
	private static final int[] textureIndices = RandomTextures.generateIndexArray(2137L, 8);

	public final Icon[] blockTextures = new Icon[8];

	public BlockNukestone(int blockID) {
		super(blockID, Material.rock);
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		for (int i = 0; i < 8; i++) {
			this.blockTextures[i] = register.getTexture(this.getSimpleName() + i, 16, 16);
		}
	}

	@Override
	public Icon getBlockIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.blockTextures[textureIndices[RandomTextures.getIndex(x, y, z)]];
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		this.tryToFall(world, x, y, z);
	}

	@Override
	public int tickRate() {
		return 3;
	}

	private void tryToFall(World world, int x, int y, int z) {
		if(BlockSand.canFallBelow(world, x, y - 1, z) && y >= 0) {
			byte var8 = 32;
			if(!BlockSand.fallInstantly && world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
				EntityFallingSand entity = new EntityFallingSand(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.blockID);
				entity.lastTickPosX = entity.posX;
				entity.lastTickPosY = entity.posY;
				entity.lastTickPosZ = entity.posZ;
				entity.blockMeta = world.getBlockMetadata(x, y, z);
				world.entityJoinedWorld(entity);
			} else {
				world.setBlockWithNotify(x, y, z, 0);

				while(BlockSand.canFallBelow(world, x, y - 1, z) && y > 0) {
					--y;
				}

				if(y > 0) {
					world.setBlockWithNotify(x, y, z, this.blockID);
				}
			}
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		if (world == null) return 0xFFFFFF;
		int meta = world.getBlockMetadata(x, y, z);
		switch (meta) {
			case 1: return 0xDDDDDD;
			case 2: return 0xBBBBBB;
			case 3: return 0x999999;
			default:return 0xFFFFFF;
		}
	}
}
