package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.block.BlockDustOre;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

import java.util.Collection;

public class ItemBlockDustOre extends ItemBlock {
	public ItemBlockDustOre(BlockDustOre block) {
		super(block.blockID - Block.ID_SIZE);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return "tile." + NuclearProgram.MODID + "/ore" + BlockDustOre.VARIANTS[stack.getItemDamage()];
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		for (int i = 0; i < BlockDustOre.VARIANTS.length; i++) items.add(new ItemStack(this, 1, i));
	}
}
