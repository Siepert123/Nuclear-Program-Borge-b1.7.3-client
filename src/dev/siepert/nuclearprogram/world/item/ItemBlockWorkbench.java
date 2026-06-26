package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.block.BlockWorkbench;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockWorkbench extends ItemBlock {
	public ItemBlockWorkbench(BlockWorkbench block) {
		super(block.blockID - 256);
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
}
