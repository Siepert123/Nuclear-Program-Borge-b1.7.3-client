package dev.siepert.nuclearprogram.world.gen;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockDustOre;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import net.minecraftborge.loader.IChunkDecorator;
import net.minecraftborge.loader.WorldGenMetaMinable;

import java.util.Random;

public class ChunkDecoratorDustOre implements IChunkDecorator {
	public ChunkDecoratorDustOre() {
		this.sulphur = new WorldGenMetaMinable(BlockInit.oreDust.blockID, BlockDustOre.SULPHUR, 8);
		this.saltpeter = new WorldGenMetaMinable(BlockInit.oreDust.blockID, BlockDustOre.SALTPETER, 8);
	}

	private final WorldGenerator sulphur, saltpeter;

	@Override
	public void decorate(World world, int chunkX, int chunkZ, BiomeGenBase biome, Random random) {
		int x = chunkX * 16 + 8;
		int z = chunkZ * 16 + 8;
		for (int i = 0; i < 10; i++) {
			this.sulphur.generate(world, random, x + random.nextInt(16), random.nextInt(24), z + random.nextInt(16));
			this.saltpeter.generate(world, random, x + random.nextInt(16), random.nextInt(24)+8, z + random.nextInt(16));
		}
	}
}
