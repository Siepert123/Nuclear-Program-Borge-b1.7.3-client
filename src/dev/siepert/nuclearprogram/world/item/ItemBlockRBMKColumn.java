package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockRBMKColumn;
import net.minecraft.src.*;

public class ItemBlockRBMKColumn extends ItemBlock {
	public ItemBlockRBMKColumn(BlockRBMKColumn block) {
		super(block.blockID - Block.ID_SIZE);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side) {
		if (Block.blocksList[world.getBlockId(x, y, z)] instanceof BlockRBMKColumn) {
			y -= world.getBlockMetadata(x, y, z);
		}

		return super.onItemUse(stack, player, world, x, y, z, side);
	}
}
