package dev.siepert.nuclearprogram.world.gen;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockResourceRock;
import net.minecraft.client.Minecraft;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraftborge.loader.IChunkDecorator;

import java.util.Random;

public class ChunkDecoratorDeposit implements IChunkDecorator {
	public ChunkDecoratorDeposit() {}

	@Override
	public void decorate(World world, int chunkX, int chunkZ, BiomeGenBase biome, Random random) {
		if (random.nextInt(64) == 0) {
			int type = random.nextInt(BlockResourceRock.VARIANTS.length);
			int x = chunkX * 16 + 12;
			int z = chunkZ * 16 + 12;
			Minecraft.getTheMinecraft().ingameGUI.addChatMessage("Deposit at X:" + x + ", Z:" + z + " of type " + BlockResourceRock.VARIANTS[type]);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					for (int k = 0; k < 5; k++) {
						if (world.getBlockId(x + i, k, z + j) == Block.bedrock.blockID) {
							if (k == 0) {
								world.setBlockAndMetadata(x + i, k, z + j, BlockInit.resourceDeposit.blockID, type);
							} else {
								world.setBlock(x + i, k, z + j, Block.stone.blockID);
							}
						}
					}
				}
			}
			for (int y = 1; y < 80; y++) {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						int k = x + i;
						int l = z + j;
						if (world.getBlockId(k, y, l) == Block.stone.blockID && random.nextFloat() < 0.42F) {
							world.setBlockAndMetadata(k, y, l, BlockInit.resourceRock.blockID, type);
						}
					}
				}
			}
		}
	}
}
