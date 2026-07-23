package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.bei.api.IRecipesPlugin;
import dev.siepert.bei.api.RecipesPlugin;
import dev.siepert.bei.api.reg.ICategoryRegistration;
import dev.siepert.bei.api.reg.IRecipeRegistration;
import dev.siepert.bei.api.reg.IScreenRegistration;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.gui.GuiBloomery;
import dev.siepert.nuclearprogram.gui.GuiFurnaceBuilder;
import dev.siepert.nuclearprogram.gui.GuiGasCentrifuge;
import dev.siepert.nuclearprogram.gui.GuiRTG;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.recipe.*;
import dev.siepert.nuclearprogram.recipe.crafting.CraftingRecycleFuelRod;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RecipesPlugin(NuclearProgram.MODID)
public class NuclearProgramBEI implements IRecipesPlugin {
	@Override
	public String getPluginUID() {
		return NuclearProgram.MODID;
	}

	@Override
	public void registerCategories(ICategoryRegistration registration) {
		registration.registerCategory(NPRecipeCategories.RECYCLE_FUEL_ROD, new CraftingCategoryRecycleFuelRod());
		registration.registerCategory(NPRecipeCategories.SMELTING_BUILDER, new RecipeCategoryFurnaceBuilder());
		registration.registerCategory(NPRecipeCategories.WORKBENCH, new RecipeCategoryWorkbench());
		registration.registerCategory(NPRecipeCategories.BLOOMERY, new RecipeCategoryBloomery());
		registration.registerCategory(NPRecipeCategories.GAS_CENTRIFUGE, new RecipeCategoryGasCentrifuge());
		registration.registerCategory(NPRecipeCategories.RTG_FUEL, new RecipeCategoryRTGFuel());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		CraftingManager crafting = CraftingManager.getInstance();
		int skip;

		// Fuel Rod Recycling category
		IRecipeCategory<CraftingRecycleFuelRod> recycleFuelRod = registration.getCategoryByUID(NPRecipeCategories.RECYCLE_FUEL_ROD);
		List<CraftingRecycleFuelRod> recycleFuelRodRecipes = new ArrayList<>();
		for (IRecipe recipe : crafting.getRecipeList()) {
			if (recipe instanceof CraftingRecycleFuelRod) {
				recycleFuelRodRecipes.add((CraftingRecycleFuelRod) recipe);
			}
		}
		registration.addRecipes(recycleFuelRod, recycleFuelRodRecipes);
		System.out.println(recycleFuelRodRecipes.size() + " fuel rod recycling recipes");

		// Builder's Smelting category
		BuilderFurnaceRecipes smelting = BuilderFurnaceRecipes.smelting();
		IRecipeCategory<RecipeFurnaceBuilder> smeltingBuilder = registration.getCategoryByUID(NPRecipeCategories.SMELTING_BUILDER);
		List<RecipeFurnaceBuilder> smeltingBuilderRecipes = new ArrayList<>();
		skip = 0;
		for (Map.Entry<Integer, BuilderFurnaceRecipes.Result> entry : smelting.getSmeltingList().entrySet()) {
			ItemStack in = unpack(entry.getKey());
			ItemStack result = entry.getValue().item;
			if (in == null || result == null) {
				skip++;
				continue;
			}
			smeltingBuilderRecipes.add(new RecipeFurnaceBuilder(in, result, entry.getValue().recipeTime));
		}
		registration.addRecipes(smeltingBuilder, smeltingBuilderRecipes);
		System.out.println(smeltingBuilderRecipes.size() + " builder's smelting recipes" + (skip != 0 ? " (" + skip + " skipped)" : ""));

		// Workbench category
		IRecipeCategory<WorkbenchRecipe> workbench = registration.getCategoryByUID(NPRecipeCategories.WORKBENCH);
		List<WorkbenchRecipe> workbenchRecipes = new ArrayList<>(WorkbenchRecipes.crafting().getRecipes().values());
		registration.addRecipes(workbench, workbenchRecipes);
		System.out.println(workbenchRecipes.size() + " workbench recipes");

		// Blooming category
		BloomeryRecipes blooming = BloomeryRecipes.blooming();
		IRecipeCategory<RecipeBloomery> bloomery = registration.getCategoryByUID(NPRecipeCategories.BLOOMERY);
		List<RecipeBloomery> bloomingRecipes = new ArrayList<>();
		skip = 0;
		for (Map.Entry<Integer, BloomeryRecipes.Result> entry : blooming.getSmeltingList().entrySet()) {
			ItemStack in = unpack(entry.getKey());
			ItemStack result = entry.getValue().item;
			ItemStack byproduct = entry.getValue().byproduct;
			if (in == null || result == null) {
				skip++;
				continue;
			}
			bloomingRecipes.add(new RecipeBloomery(in, result, byproduct, entry.getValue().recipeTime));
		}
		registration.addRecipes(bloomery, bloomingRecipes);
		System.out.println(bloomingRecipes.size() + " blooming recipes" + (skip != 0 ? " (" + skip + " skipped)" : ""));

		// Gas Centrifuging category
		IRecipeCategory<RecipeGasCentrifuge> gasCentrifuge = registration.getCategoryByUID(NPRecipeCategories.GAS_CENTRIFUGE);
		List<RecipeGasCentrifuge> gasCentrifugeRecipes = new ArrayList<>();
		gasCentrifugeRecipes.add(new RecipeGasCentrifuge(FluidInit.uraniumHexafluoride.fluidID, 1000, FluidInit.uraniumHexafluorideLE.fluidID, 800));
		gasCentrifugeRecipes.add(new RecipeGasCentrifuge(FluidInit.uraniumHexafluorideLE.fluidID, 1000, FluidInit.uraniumHexafluorideME.fluidID, 800));
		gasCentrifugeRecipes.add(new RecipeGasCentrifuge(FluidInit.uraniumHexafluorideME.fluidID, 1000, FluidInit.uraniumHexafluorideHE.fluidID, 800));
		registration.addRecipes(gasCentrifuge, gasCentrifugeRecipes);
		System.out.println(gasCentrifugeRecipes.size() + " gas centrifuge recipes");

		// RTG Fuel category
		RTGFuelRecipes rtgFueling = RTGFuelRecipes.instance();
		IRecipeCategory<RecipeRTGFuel> rtgFuel = registration.getCategoryByUID(NPRecipeCategories.RTG_FUEL);
		List<RecipeRTGFuel> rtgFuelRecipes = new ArrayList<>();
		skip = 0;
		for (Map.Entry<Integer, RTGFuelRecipes.Recipe> entry : rtgFueling.recipes.entrySet()) {
			ItemStack in = unpack(entry.getKey());
			ItemStack result = entry.getValue().product;
			int ticks = entry.getValue().duration;
			int watts = entry.getValue().production;
			if (in == null || result == null) {
				skip++;
				continue;
			}
			rtgFuelRecipes.add(new RecipeRTGFuel(in, ticks, watts, result));
		}
		registration.addRecipes(rtgFuel, rtgFuelRecipes);
		System.out.println(rtgFuelRecipes.size() + " RTG fuel recipes" + (skip != 0 ? " (" + skip + " skipped)" : ""));
	}

	@Override
	public void registerScreenHandlers(IScreenRegistration registration) {
		registration.addCraftingCategoryUIDs(NPRecipeCategories.RECYCLE_FUEL_ROD);

		registration.addScreenHandler(GuiFurnaceBuilder.class, 79, 34, 24, 17, NPRecipeCategories.SMELTING_BUILDER, "furnaceFuel");
		registration.addScreenHandler(GuiBloomery.class, 97, 13, 22, 15, NPRecipeCategories.BLOOMERY);

		registration.addScreenHandler(GuiGasCentrifuge.class, 49, 17, 42, 56, NPRecipeCategories.GAS_CENTRIFUGE);
		registration.addScreenHandler(GuiRTG.class, 70, 26, 36, 36, NPRecipeCategories.RTG_FUEL);
	}

	public static ItemStack unpack(int packed) {
		int itemID = packed & 0xFFFF;
		if (Item.itemsList[itemID] == null) return null;
		int meta = (packed >> 16) & 0xFFFF;
		return new ItemStack(itemID, 1, meta == 0xFFFF ? -1 : meta);
	}
}
