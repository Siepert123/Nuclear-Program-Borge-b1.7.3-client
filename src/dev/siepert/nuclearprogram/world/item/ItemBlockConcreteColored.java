package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockConcreteColored;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.DyeHelper;

public class ItemBlockConcreteColored extends ItemBlock {
	private final BlockConcreteColored block;
	public ItemBlockConcreteColored(BlockConcreteColored block) {
		super(block.blockID - 256);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.block = block;
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return this.block.getBlockName() + DyeHelper.COLOR_NAMES[stack.getItemDamage()];
	}
}
