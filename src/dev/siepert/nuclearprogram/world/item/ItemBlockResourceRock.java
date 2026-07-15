package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockResourceRock;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockResourceRock extends ItemBlock {
	public ItemBlockResourceRock(BlockResourceRock block) {
		super(block.blockID - Block.ID_SIZE);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return super.getItemNameIS(stack) + BlockResourceRock.VARIANTS[stack.getItemDamage()];
	}
}
