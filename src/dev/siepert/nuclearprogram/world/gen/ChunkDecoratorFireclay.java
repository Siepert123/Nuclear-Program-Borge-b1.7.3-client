package dev.siepert.nuclearprogram.world.gen;

import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.src.*;
import net.minecraftborge.loader.IChunkDecorator;

import java.util.Random;

public class ChunkDecoratorFireclay implements IChunkDecorator {
	public ChunkDecoratorFireclay(int ground) {
		this.fireclay = new WorldGenMinable(BlockInit.fireclay.blockID, 64, ground);
		this.clay = new WorldGenMinable(Block.blockClay.blockID, 16, Block.dirt.blockID);
	}

	private final WorldGenerator fireclay;
	private final WorldGenerator clay;

	@Override
	public void decorate(World world, int chunkX, int chunkZ, BiomeGenBase biome, Random random) {
		if (random.nextFloat() < 0.1F) {
			this.fireclay.generate(world, random, chunkX * 16 + 16, random.nextInt(64) + 32, chunkZ * 16 + 16);
		}

		// Boost clay spawns
		int x = chunkX * 16 + 8 + random.nextInt(16);
		int z = chunkZ * 16 + 8 + random.nextInt(16);
		if (random.nextFloat() < 0.5F && world.getBlockId(x, 63, z) == Block.waterStill.blockID) {
			for (int y = 63; y > 0; y--) {
				if (world.getBlockId(x, y, z) != Block.waterStill.blockID) {
					this.clay.generate(world, random, x, y, z);
					break;
				}
			}
		}
	}
}
