package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.block.BlockWorkbench;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

import java.util.Collection;

public class ItemBlockWorkbench extends ItemBlock {
	public ItemBlockWorkbench(BlockWorkbench block) {
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
		return "tile." + NuclearProgram.MODID + "/workbench" + BlockWorkbench.VARIANTS[stack.getItemDamage()];
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		for (int i = 0; i < BlockWorkbench.VARIANTS.length; i++) items.add(new ItemStack(this, 1, i));
	}
}
