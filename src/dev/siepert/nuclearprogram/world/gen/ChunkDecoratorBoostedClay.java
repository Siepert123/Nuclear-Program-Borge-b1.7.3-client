package dev.siepert.nuclearprogram.world.gen;

import net.minecraft.src.*;
import net.minecraftborge.loader.IChunkDecorator;

import java.util.Random;

public class ChunkDecoratorBoostedClay implements IChunkDecorator {
	public ChunkDecoratorBoostedClay() {
		this.clay = new WorldGenMinable(Block.blockClay.blockID, 32, Block.dirt.blockID);
	}

	private final WorldGenerator clay;

	@Override
	public void decorate(World world, int chunkX, int chunkZ, BiomeGenBase biome, Random random) {
		int x = chunkX * 16 + 8 + random.nextInt(16);
		int z = chunkZ * 16 + 8 + random.nextInt(16);
		if (world.getBlockMaterial(x, 63, z) == Material.water) {
			int y = 62;
			for (; y > 16; y--) {
				if (world.getBlockMaterial(x, y, z) != Material.water) break;
			}
			this.clay.generate(world, random, x, y, z);
		}
	}
}
