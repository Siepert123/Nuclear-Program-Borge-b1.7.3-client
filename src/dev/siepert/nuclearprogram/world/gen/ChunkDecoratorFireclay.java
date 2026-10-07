package dev.siepert.nuclearprogram.world.gen;

import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.src.*;
import net.minecraftborge.loader.IChunkDecorator;

import java.util.Random;

public class ChunkDecoratorFireclay implements IChunkDecorator {
	public ChunkDecoratorFireclay(int ground) {
		this.fireclay = new WorldGenMinable(BlockInit.fireclay.blockID, 64, ground);
	}

	private final WorldGenerator fireclay;

	@Override
	public void decorate(World world, int chunkX, int chunkZ, BiomeGenBase biome, Random random) {
		if (random.nextFloat() < 0.1F) {
			this.fireclay.generate(world, random, chunkX * 16 + 16, random.nextInt(64) + 32, chunkZ * 16 + 16);
		}
	}
}
