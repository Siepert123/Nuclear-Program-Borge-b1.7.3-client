package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.recipe.BloomeryRecipes;
import dev.siepert.nuclearprogram.recipe.BuilderFurnaceRecipes;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipe;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipes;
import dev.siepert.nuclearprogram.world.block.BlockMetal;
import dev.siepert.nuclearprogram.world.block.BlockMetalOre;
import dev.siepert.nuclearprogram.world.block.BlockWorkbench;
import dev.siepert.nuclearprogram.world.item.ItemFuelRod;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.DyeHelper;
import net.minecraftborge.loader.FurnaceRecipesFix;
import net.minecraftborge.loader.Ingredient;
import net.minecraftborge.loader.event.register.AddRecipesEvent;

public class RecipeInit {
	public static void crafting(AddRecipesEvent recipes) {
		//region Compacting recipes
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.COPPER),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotCopper"),
				'X', ItemInit.ingotCopper
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotCopper, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.COPPER)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.ALUMINIUM),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotAluminium"),
				'X', ItemInit.ingotAluminium
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotAluminium, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.ALUMINIUM)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.LEAD),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotLead"),
				'X', ItemInit.ingotLead
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotLead, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.LEAD)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.TITANIUM),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotTitanium"),
				'X', ItemInit.ingotTitanium
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotTitanium, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.TITANIUM)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.TUNGSTEN),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotTungsten"),
				'X', ItemInit.ingotTungsten
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotTungsten, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.TUNGSTEN)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.STEEL),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotSteel"),
				'X', ItemInit.ingotSteel
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotSteel, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.STEEL)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.ELECTRUM),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotElectrum"),
				'X', ItemInit.ingotElectrum
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotElectrum, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.ELECTRUM)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.KAUPIUM),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotKaupium"),
				'X', ItemInit.ingotKaupium
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotKaupium, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.KAUPIUM)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.blockMetal, 1, BlockMetal.YANOIZED_KAUPIUM),
				"###", "#X#", "###",
				'#', Ingredient.of("ingotYanoizedKaupium"),
				'X', ItemInit.ingotYanoizedKaupium
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotYanoizedKaupium, 9),
				"#",
				'#', Ingredient.of(BlockInit.blockMetal.blockID, BlockMetal.YANOIZED_KAUPIUM)
		);
		//endregion


		// Blocks

		recipes.addShapedRecipe(new ItemStack(BlockInit.fireclay, 1),
				"##", "##",
				'#', ItemInit.ballFireclay
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.firebricks, 1),
				"##", "##",
				'#', ItemInit.firebrick
		);

		recipes.addShapedRecipe(new ItemStack(BlockInit.concreteBrick, 4),
				" # ", "#X#", " # ",
				'#', BlockInit.concrete,
				'X', Item.clay
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.concreteFoundation, 1),
				"#", "#",
				'#', Ingredient.of(BlockInit.slabConcreteSingle.blockID, 0)
		);
		for (int i = 0; i < 16; i++) {
			recipes.addShapedRecipe(new ItemStack(BlockInit.concreteColored, 8, i),
					"###", "#X#", "###",
					'#', BlockInit.concrete,
					'X', Ingredient.of("dye" + DyeHelper.COLOR_NAMES[i])
			);
		}

		recipes.addShapedRecipe(new ItemStack(BlockInit.stairsConcrete, 6),
				"#  ", "## ", "###",
				'#', BlockInit.concrete
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.stairsConcreteBrick, 6),
				"#  ", "## ", "###",
				'#', BlockInit.concreteBrick
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.stairsConcreteFoundation, 6),
				"#  ", "## ", "###",
				'#', BlockInit.concreteFoundation
		);

		recipes.addShapedRecipe(new ItemStack(BlockInit.slabConcreteSingle, 6, 0),
				"###",
				'#', BlockInit.concrete
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.slabConcreteSingle, 6, 1),
				"###",
				'#', BlockInit.concreteBrick
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.slabConcreteSingle, 6, 2),
				"###",
				'#', BlockInit.concreteFoundation
		);
		for (int i = 0; i < 16; i++) {
			recipes.addShapedRecipe(new ItemStack(BlockInit.slabConcreteColoredSingle, 6, i),
					"###",
					'#', Ingredient.of(BlockInit.concreteColored.blockID, i)
			);
		}

		recipes.addShapedRecipe(new ItemStack(BlockInit.workbench, 1, BlockWorkbench.IRON),
				"#X#", "#C#", "###",
				'#', Ingredient.of("ingotIron"),
				'X', Ingredient.of("blockIron"),
				'C', Block.workbench
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.workbench, 1, BlockWorkbench.STEEL),
				"#X#", "#C#", "###",
				'#', Ingredient.of("ingotSteel"),
				'X', Ingredient.of("blockSteel"),
				'C', Ingredient.of(BlockInit.workbench.blockID, BlockWorkbench.IRON)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.furnaceBuilderIdle, 1),
				"###", "# #", "XXX",
				'#', Ingredient.of("stone"),
				'X', Ingredient.of(Block.stairSingle.blockID, 0)
		);
		recipes.addShapedRecipe(new ItemStack(BlockInit.bloomeryPipe, 1),
				"#X#", "#X#", "#X#",
				'#', Block.brick,
				'X', Ingredient.of("plateCopper")
		);

		recipes.addShapedRecipe(new ItemStack(BlockInit.hatch, 1),
				" X ", "###", " X ",
				'#', Ingredient.of("ingotSteel"),
				'X', ItemInit.valve
		);


		// Items

		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateIron, 1),
				ItemInit.hammer,
				Ingredient.of("ingotIron")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateGold, 1),
				ItemInit.hammer,
				Ingredient.of("ingotGold")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateCopper, 1),
				ItemInit.hammer,
				Ingredient.of("ingotCopper")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateAluminium, 1),
				ItemInit.hammer,
				Ingredient.of("ingotAluminium")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateLead, 1),
				ItemInit.hammer,
				Ingredient.of("ingotLead")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateTitanium, 1),
				ItemInit.hammer,
				Ingredient.of("ingotTitanium")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateTungsten, 1),
				ItemInit.hammer,
				Ingredient.of("ingotTungsten")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.plateSteel, 1),
				ItemInit.hammer,
				Ingredient.of("ingotSteel")
		);

		recipes.addShapelessRecipe(new ItemStack(ItemInit.ballFireclay, 4),
				Item.clay, Item.clay, Item.clay,
				ItemInit.resourceBrickBauxite
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.ballFireclay, 4),
				Item.clay, Item.clay,
				Block.netherrack, Block.netherrack
		);

		recipes.addShapedRecipe(new ItemStack(ItemInit.hammer, 1),
				"## ", "##X", "## ",
				'#', Ingredient.of("ingotIron"),
				'X', Item.stick
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.cutters, 1),
				"X#", " X",
				'#', Ingredient.of("ingotIron"),
				'X', Item.stick
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.screwdriver, 1),
				"  X", "D# ", "#D ",
				'#', Ingredient.of("ingotIron"),
				'X', Ingredient.of("plateIron"),
				'D', Ingredient.of("dyeAny")
		);

		// Fuel rod filling

		recipes.addShapelessRecipe(new ItemStack(ItemInit.fuelRod, 1, ItemFuelRod.NATURAL_URANIUM),
				Ingredient.of(ItemInit.fuelRodEmpty.shiftedIndex),
				Ingredient.of("ingotLead")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.fuelRod, 1, ItemFuelRod.NATURAL_THORIUM),
				Ingredient.of(ItemInit.fuelRodEmpty.shiftedIndex),
				Ingredient.of("ingotLead")
		);

		recipes.addShapelessRecipe(new ItemStack(ItemInit.fuelRodArray, 1, ItemFuelRod.NATURAL_URANIUM),
				Ingredient.of(ItemInit.fuelRodArrayEmpty.shiftedIndex),
				Ingredient.of("ingotLead"),
				Ingredient.of("ingotLead"),
				Ingredient.of("ingotLead"),
				Ingredient.of("ingotLead")
		);
		recipes.addShapelessRecipe(new ItemStack(ItemInit.fuelRodArray, 1, ItemFuelRod.NATURAL_THORIUM),
				Ingredient.of(ItemInit.fuelRodArrayEmpty.shiftedIndex),
				Ingredient.of("ingotLead"),
				Ingredient.of("ingotLead"),
				Ingredient.of("ingotLead"),
				Ingredient.of("ingotLead")
		);

		// Fuel rod recycling

		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotLead, 1),
				"#",
				'#', Ingredient.of(ItemInit.fuelRod.shiftedIndex, ItemFuelRod.NATURAL_URANIUM)
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotLead, 1),
				"#",
				'#', Ingredient.of(ItemInit.fuelRod.shiftedIndex, ItemFuelRod.NATURAL_THORIUM)
		);

		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotLead, 4),
				"#",
				'#', Ingredient.of(ItemInit.fuelRodArray.shiftedIndex, ItemFuelRod.NATURAL_URANIUM)
		);
		recipes.addShapedRecipe(new ItemStack(ItemInit.ingotLead, 4),
				"#",
				'#', Ingredient.of(ItemInit.fuelRodArray.shiftedIndex, ItemFuelRod.NATURAL_THORIUM)
		);

		// Vanilla

		recipes.addShapelessRecipe(new ItemStack(Item.silk, 2),
				ItemInit.hempFibers, ItemInit.hempFibers, ItemInit.hempFibers
		);
	}

	public static void furnace(FurnaceRecipesFix recipes) {
		recipes.addSmelting(ItemInit.ballFireclay.shiftedIndex, new ItemStack(ItemInit.firebrick, 1));
		recipes.addSmelting(ItemInit.potato.shiftedIndex, new ItemStack(ItemInit.potatoCooked, 1));

		recipes.addSmelting(BlockInit.oreMetal.blockID, BlockMetalOre.COPPER, new ItemStack(ItemInit.ingotCopper, 1), 200);
		recipes.addSmelting(BlockInit.oreMetal.blockID, BlockMetalOre.LEAD, new ItemStack(ItemInit.ingotLead, 1), 200);
		recipes.addSmelting(BlockInit.oreMetal.blockID, BlockMetalOre.TITANIUM, new ItemStack(ItemInit.ingotTitanium, 1), 200);
		recipes.addSmelting(BlockInit.oreMetal.blockID, BlockMetalOre.TUNGSTEN, new ItemStack(ItemInit.ingotTungsten, 1), 200);
	}

	public static void workbench(WorkbenchRecipes recipes) {
		recipes.addRecipe(NuclearProgram.path("test"), WorkbenchRecipe.builder()
				.setResult(new ItemStack(Item.diamond))
				.addIngredient(Ingredient.of(Block.dirt.blockID), 5)
				.build());
		recipes.addRecipe(NuclearProgram.path("workbenchIron"), WorkbenchRecipe.builder()
				.setResult(new ItemStack(BlockInit.workbench, 1, BlockWorkbench.IRON))
				.addIngredient(Ingredient.of(Block.workbench.blockID), 1)
				.addIngredient(Ingredient.of("ingotIron"), 10)
				.build());
		recipes.addRecipe(NuclearProgram.path("workbenchSteel"), WorkbenchRecipe.builder()
				.setResult(new ItemStack(BlockInit.workbench, 1, BlockWorkbench.STEEL))
				.addIngredient(Ingredient.of(Block.workbench.blockID), 1)
				.addIngredient(Ingredient.of("ingotSteel"), 10)
				.build());

		recipes.addRecipe(NuclearProgram.path("bloomery"), WorkbenchRecipe.builder()
				.setResult(new ItemStack(BlockInit.bloomeryIdle))
				.addIngredient(Ingredient.of(Block.brick.blockID), 16)
				.build());

		recipes.addRecipe("hbm/valve", WorkbenchRecipe.builder()
				.setResult(new ItemStack(ItemInit.valve, 1))
				.addIngredient(Ingredient.of("ingotSteel"), 4)
				.build());
	}

	public static void builderFurnace(BuilderFurnaceRecipes recipes) {
		recipes.addSmelting(ItemInit.ballFireclay.shiftedIndex, 0, new ItemStack(ItemInit.firebrick, 1), 50);
	}

	public static void bloomery(BloomeryRecipes recipes) {
		recipes.addSmelting(Block.oreIron.blockID, new ItemStack(ItemInit.ingotSteel, 1), new ItemStack(Block.gravel, 1));
		recipes.addSmelting(ItemInit.resourceBrickHematite.shiftedIndex, 0, new ItemStack(Item.ingotIron), null, 500);
		recipes.addSmelting(ItemInit.resourceBrickMalachite.shiftedIndex, 0, new ItemStack(ItemInit.ingotCopper), null, 500);
		recipes.addSmelting(ItemInit.resourceBrickBauxite.shiftedIndex, 0, new ItemStack(ItemInit.ingotAluminium), null, 500);
	}
}
