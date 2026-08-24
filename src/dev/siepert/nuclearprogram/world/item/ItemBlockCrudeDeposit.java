package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockCrudeDeposit;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockCrudeDeposit extends ItemBlock {
	public ItemBlockCrudeDeposit(BlockCrudeDeposit block) {
		super(block.blockID - Block.ID_SIZE);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return this.getItemName() + BlockCrudeDeposit.VARIANTS[stack.getItemDamage()];
	}
}
