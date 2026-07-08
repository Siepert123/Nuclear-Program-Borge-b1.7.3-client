package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.bei.api.IRecipesPlugin;
import dev.siepert.bei.api.RecipesPlugin;
import dev.siepert.bei.api.reg.ICategoryRegistration;
import dev.siepert.bei.api.reg.IRecipeRegistration;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.recipe.BuilderFurnaceRecipes;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipe;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;

import java.util.ArrayList;
import java.util.Collections;
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
		registration.registerCategory(NuclearProgram.path("smeltingBuilder"), new RecipeCategoryFurnaceBuilder());
		registration.registerCategory(NuclearProgram.path("workbench"), new RecipeCategoryWorkbench());
		registration.registerCategory(NuclearProgram.path("bloomery"), new RecipeCategoryBloomery());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		int skip;

		BuilderFurnaceRecipes smelting = BuilderFurnaceRecipes.smelting();
		IRecipeCategory<RecipeFurnaceBuilder> smeltingBuilder = registration.getCategoryByUID(NuclearProgram.path("smeltingBuilder"));
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

		IRecipeCategory<WorkbenchRecipe> workbench = registration.getCategoryByUID(NuclearProgram.path("workbench"));
		List<WorkbenchRecipe> workbenchRecipes = new ArrayList<>(WorkbenchRecipes.crafting().getRecipeList());
		registration.addRecipes(workbench, workbenchRecipes);
		System.out.println(workbenchRecipes.size() + " workbench recipes");

		registration.addRecipes(registration.getCategoryByUID(NuclearProgram.path("bloomery")), Collections.singletonList(new Object()));
		System.out.println("1 blooming recipe");
	}

	public static ItemStack unpack(int packed) {
		int itemID = packed & 0xFFFF;
		if (Item.itemsList[itemID] == null) return null;
		int meta = (packed >> 16) & 0xFFFF;
		return new ItemStack(itemID, 1, meta == 0xFFFF ? -1 : meta);
	}
}
