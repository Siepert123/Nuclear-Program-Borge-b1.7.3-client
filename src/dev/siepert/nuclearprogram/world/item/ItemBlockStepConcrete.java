package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockStepConcrete;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Icon;

public class ItemBlockStepConcrete extends ItemBlock {
	public ItemBlockStepConcrete(BlockStepConcrete block) {
		super(block.blockID - Block.ID_SIZE);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public Icon getTextureFromDamage(int damage) {
		return BlockInit.slabConcreteSingle.getBlockIconFromSideAndMetadata(2, damage & 3);
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage & 3;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return "tile.nuclear_program/slab" + BlockStepConcrete.NAMES[stack.getItemDamage() & 3];
	}
}
