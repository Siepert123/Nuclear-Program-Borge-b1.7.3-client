package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.world.gen.*;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.ChunkProviderHell;
import net.minecraftborge.loader.ChunkDecoratorList;

public class WorldGenInit {
	public static void register() {
		ChunkProviderGenerate.DECORATORS[ChunkDecoratorList.ORES].addDecorator(new ChunkDecoratorMetalOre(), 0);
		ChunkProviderGenerate.DECORATORS[ChunkDecoratorList.ORES].addDecorator(new ChunkDecoratorDustOre(), 0);
		ChunkProviderGenerate.DECORATORS[ChunkDecoratorList.SOIL].addDecorator(new ChunkDecoratorFireclay(Block.stone.blockID), 0);
		ChunkProviderGenerate.DECORATORS[ChunkDecoratorList.ORES].addDecorator(new ChunkDecoratorDeposit(), -64);
		ChunkProviderGenerate.DECORATORS[ChunkDecoratorList.POST].addDecorator(new ChunkDecoratorCrudes(), 0);
		ChunkProviderHell.DECORATORS[ChunkDecoratorList.SOIL].addDecorator(new ChunkDecoratorFireclay(Block.netherrack.blockID), 0);
	}
}
