package dev.siepert.nuclearprogram.world.gen;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockCrudeDeposit;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraftborge.loader.IChunkDecorator;

import java.util.Random;

public class ChunkDecoratorCrudes implements IChunkDecorator {
	private static final int OIL_CHANCE = 64;
	private static final int OIL_CHANCE_DESERT = 16;
	private static final int GAS_CHANCE = 32;

	public ChunkDecoratorCrudes() {

	}

	@Override
	public void decorate(World world, int chunkX, int chunkZ, BiomeGenBase biome, Random random) {
		if (random.nextInt(biome == BiomeGenBase.desert ? OIL_CHANCE_DESERT : OIL_CHANCE) == 0) {
			int x = chunkX * 16 + random.nextInt(16) + 8;
			int z = chunkZ * 16 + random.nextInt(16) + 8;
			world.setBlockAndMetadata(x, 0, z, BlockInit.depositBedrockCrude.blockID, BlockCrudeDeposit.OIL);
			for (int i = 1; i < 5; i++) {
				if (world.getBlockId(x, i, z) == Block.bedrock.blockID) world.setBlock(x, i, z, Block.stone.blockID);
			}
			int h = world.getHeightValue(x, z);
			for (int y = h; y < h + 4; y++) {
				world.setBlock(x, y, z, Block.cobblestoneMossy.blockID);
			}
			world.setBlockAndMetadata(x, h + 4, z, BlockInit.depositCrude.blockID, BlockCrudeDeposit.OIL);
		}
		if (random.nextInt(GAS_CHANCE) == 0) {
			int x = chunkX * 16 + random.nextInt(16) + 8;
			int z = chunkZ * 16 + random.nextInt(16) + 8;
			world.setBlockAndMetadata(x, 0, z, BlockInit.depositBedrockCrude.blockID, BlockCrudeDeposit.GAS);
			for (int i = 1; i < 5; i++) {
				if (world.getBlockId(x, i, z) == Block.bedrock.blockID) world.setBlock(x, i, z, Block.stone.blockID);
			}
			int h = world.getHeightValue(x, z);
			for (int y = h; y < h + 4; y++) {
				world.setBlock(x, y, z, Block.cobblestoneMossy.blockID);
			}
			world.setBlockAndMetadata(x, h + 4, z, BlockInit.depositCrude.blockID, BlockCrudeDeposit.GAS);
		}
	}
}
