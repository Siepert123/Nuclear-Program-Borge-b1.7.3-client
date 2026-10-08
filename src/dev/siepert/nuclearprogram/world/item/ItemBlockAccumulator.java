package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockAccumulator;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;

public class ItemBlockAccumulator extends ItemBlock {
	public ItemBlockAccumulator(BlockAccumulator block) {
		super(block.blockID - Block.ID_SIZE);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}
}
