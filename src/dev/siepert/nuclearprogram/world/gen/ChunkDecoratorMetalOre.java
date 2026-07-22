package dev.siepert.nuclearprogram.world.gen;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockMetalOre;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import net.minecraftborge.loader.IChunkDecorator;
import net.minecraftborge.loader.WorldGenMetaMinable;

import java.util.Random;

public class ChunkDecoratorMetalOre implements IChunkDecorator {
	public ChunkDecoratorMetalOre() {
		this.copper = new WorldGenMetaMinable(BlockInit.oreMetal.blockID, BlockMetalOre.COPPER, 8);
		this.lead = new WorldGenMetaMinable(BlockInit.oreMetal.blockID, BlockMetalOre.LEAD, 8);
		this.titanium = new WorldGenMetaMinable(BlockInit.oreMetal.blockID, BlockMetalOre.TITANIUM, 6);
		this.tungsten = new WorldGenMetaMinable(BlockInit.oreMetal.blockID, BlockMetalOre.TUNGSTEN, 6);
		this.uranium = new WorldGenMetaMinable(BlockInit.oreMetal.blockID, BlockMetalOre.URANIUM, 4);
		this.thorium = new WorldGenMetaMinable(BlockInit.oreMetal.blockID, BlockMetalOre.THORIUM, 6);
	}

	private final WorldGenerator copper, lead, titanium, tungsten, uranium, thorium;

	@Override
	public void decorate(World world, int chunkX, int chunkZ, BiomeGenBase biome, Random random) {
		int x = chunkX * 16 + 8;
		int z = chunkZ * 16 + 8;
		for (int i = 0; i < 30; i++) {
			this.copper.generate(world, random, x + random.nextInt(16), random.nextInt(64) + 32, z + random.nextInt(16));
		}
		for (int i = 0; i < 15; i++) {
			this.lead.generate(world, random, x + random.nextInt(16), random.nextInt(48), z + random.nextInt(16));
		}
		for (int i = 0; i < 15; i++) {
			this.titanium.generate(world, random, x + random.nextInt(16), random.nextInt(32), z + random.nextInt(16));
			this.tungsten.generate(world, random, x + random.nextInt(16), random.nextInt(32), z + random.nextInt(16));
		}
		for (int i = 0; i < 15; i++) {
			this.uranium.generate(world, random, x + random.nextInt(16), random.nextInt(32), z + random.nextInt(16));
		}
		for (int i = 0; i < 20; i++) {
			this.thorium.generate(world, random, x + random.nextInt(16), random.nextInt(24) + 8, z + random.nextInt(16));
		}
	}
}
