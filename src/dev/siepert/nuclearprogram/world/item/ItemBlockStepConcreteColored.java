package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.BlockStepConcreteColored;
import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.DyeHelper;
import net.minecraftborge.loader.Icon;

public class ItemBlockStepConcreteColored extends ItemBlock {
	private final BlockStepConcreteColored block;
	public ItemBlockStepConcreteColored(BlockStepConcreteColored block) {
		super(block.blockID - Block.ID_SIZE);
		this.block = block;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public Icon getTextureFromDamage(int damage) {
		return BlockInit.slabConcreteColoredSingle.blockTextures[damage & 15];
	}

	@Override
	public int getPlacedBlockMetadata(int damage) {
		return damage;
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return this.block.getBlockName() + DyeHelper.COLOR_NAMES[stack.getItemDamage() & 15];
	}
}
