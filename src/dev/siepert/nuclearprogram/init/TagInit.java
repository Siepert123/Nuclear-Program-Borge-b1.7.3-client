package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.world.block.BlockMetal;
import dev.siepert.nuclearprogram.world.block.BlockMetalOre;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.DyeHelper;
import net.minecraftborge.loader.tag.ItemTags;

public class TagInit {
	public static void registerBlockTags() {

	}

	public static void registerItemTags() {
		ItemTags.tag("oreCopper", new ItemStack(BlockInit.oreMetal, 1, BlockMetalOre.COPPER));
		ItemTags.tag("oreLead", new ItemStack(BlockInit.oreMetal, 1, BlockMetalOre.LEAD));
		ItemTags.tag("oreTitanium", new ItemStack(BlockInit.oreMetal, 1, BlockMetalOre.TITANIUM));
		ItemTags.tag("oreTungsten", new ItemStack(BlockInit.oreMetal, 1, BlockMetalOre.TUNGSTEN));

		ItemTags.tag("blockCopper", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.COPPER));
		ItemTags.tag("blockAluminium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.ALUMINIUM));
		ItemTags.tag("blockLead", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.LEAD));
		ItemTags.tag("blockTitanium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.TITANIUM));
		ItemTags.tag("blockTungsten", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.TUNGSTEN));
		ItemTags.tag("blockSteel", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.STEEL));
		ItemTags.tag("blockElectrum", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.ELECTRUM));
		ItemTags.tag("blockKaupium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.KAUPIUM));
		ItemTags.tag("blockYanoizedKaupium", new ItemStack(BlockInit.blockMetal, 1, BlockMetal.YANOIZED_KAUPIUM));

		ItemTags.tag("concrete", new ItemStack(BlockInit.concrete));
		ItemTags.tag("concreteColorless", new ItemStack(BlockInit.concrete));

		ItemTags.tag("concrete", new ItemStack(BlockInit.concreteColored, 1, -1));
		for (int i = 0; i < 16; i++) {
			ItemTags.tag("concreteColored" + DyeHelper.COLOR_NAMES[i], new ItemStack(BlockInit.concreteColored, 1, i));
		}

		ItemTags.tag("ingotCopper", ItemInit.ingotCopper);
		ItemTags.tag("ingotAluminium", ItemInit.ingotAluminium);
		ItemTags.tag("ingotLead", ItemInit.ingotLead);
		ItemTags.tag("ingotTitanium", ItemInit.ingotTitanium);
		ItemTags.tag("ingotTungsten", ItemInit.ingotTungsten);
		ItemTags.tag("ingotSteel", ItemInit.ingotSteel);
		ItemTags.tag("ingotElectrum", ItemInit.ingotElectrum);
		ItemTags.tag("ingotKaupium", ItemInit.ingotKaupium);
		ItemTags.tag("ingotYanoizedKaupium", ItemInit.ingotYanoizedKaupium);

		ItemTags.tag("plateIron", ItemInit.plateIron);
		ItemTags.tag("plateGold", ItemInit.plateGold);
		ItemTags.tag("plateCopper", ItemInit.plateCopper);
		ItemTags.tag("plateAluminium", ItemInit.plateAluminium);
		ItemTags.tag("plateLead", ItemInit.plateLead);
		ItemTags.tag("plateTitanium", ItemInit.plateTitanium);
		ItemTags.tag("plateTungsten", ItemInit.plateTungsten);
		ItemTags.tag("plateSteel", ItemInit.plateSteel);
	}
}
