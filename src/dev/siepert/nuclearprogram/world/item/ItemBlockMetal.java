package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.block.BlockMetal;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

import java.util.Collection;

public class ItemBlockMetal extends ItemBlock {
	public ItemBlockMetal(BlockMetal block) {
		super(block.blockID - 4096);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return "tile." + NuclearProgram.MODID + "/block" + BlockMetal.VARIANTS[stack.getItemDamage()];
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		for (int i = 0; i < BlockMetal.VARIANTS.length; i++) items.add(new ItemStack(this, 1, i));
	}
}
