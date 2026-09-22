package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockDrum;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

import java.util.Collection;

public class ItemBlockDrum extends ItemBlock {
	public ItemBlockDrum(BlockDrum block) {
		super(block.blockID - Block.ID_SIZE);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
		items.add(new ItemStack(this, 1, 2));
	}
}
