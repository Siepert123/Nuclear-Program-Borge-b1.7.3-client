package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.world.block.BlockMetal;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.tag.ItemTags;

public class TagInit {
	public static void registerBlockTags() {

	}

	public static void registerItemTags() {
		ItemTags.tag("blockCopper", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.COPPER));
		ItemTags.tag("blockAluminium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.ALUMINIUM));
		ItemTags.tag("blockLead", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.LEAD));
		ItemTags.tag("blockTitanium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.TITANIUM));
		ItemTags.tag("blockTungsten", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.TUNGSTEN));
		ItemTags.tag("blockSteel", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.STEEL));
		ItemTags.tag("blockElectrum", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.ELECTRUM));
		ItemTags.tag("blockKaupium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.KAUPIUM));
		ItemTags.tag("blockYanoizedKaupium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.YANOIZED_KAUPIUM));

		ItemTags.tag("ingotCopper", ItemInit.ingotCopper);
		ItemTags.tag("ingotAluminium", ItemInit.ingotAluminium);
		ItemTags.tag("ingotLead", ItemInit.ingotLead);
		ItemTags.tag("ingotTitanium", ItemInit.ingotTitanium);
		ItemTags.tag("ingotTungsten", ItemInit.ingotTungsten);
		ItemTags.tag("ingotSteel", ItemInit.ingotSteel);
		ItemTags.tag("ingotElectrum", ItemInit.ingotElectrum);
		ItemTags.tag("ingotKaupium", ItemInit.ingotKaupium);
		ItemTags.tag("ingotYanoizedKaupium", ItemInit.ingotYanoizedKaupium);
	}
}
