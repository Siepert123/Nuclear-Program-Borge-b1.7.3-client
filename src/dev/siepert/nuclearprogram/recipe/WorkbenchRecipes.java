package dev.siepert.nuclearprogram.recipe;

import net.minecraftborge.loader.Namespace;

import java.util.HashMap;
import java.util.LinkedHashMap;

public final class WorkbenchRecipes {
	private static final WorkbenchRecipes instance = new WorkbenchRecipes();
	public static WorkbenchRecipes crafting() {
		return instance;
	}

	private final LinkedHashMap<String, WorkbenchRecipe> recipes = new LinkedHashMap<>();
	private final HashMap<WorkbenchRecipe, String> identifiers = new HashMap<>();

	private WorkbenchRecipes() {

	}

	public void addRecipe(String name, WorkbenchRecipe recipe) {
		Namespace.validate(name);
		this.identifiers.remove(this.recipes.remove(name));
		this.recipes.put(name, recipe);
		this.identifiers.put(recipe, name);
	}
	public void removeRecipe(String name) {
		Namespace.validate(name);
		this.identifiers.remove(this.recipes.remove(name));
	}

	public WorkbenchRecipe getRecipe(String recipeID) {
		return this.recipes.get(recipeID);
	}

	public LinkedHashMap<String, WorkbenchRecipe> getRecipes() {
		return this.recipes;
	}
	public HashMap<WorkbenchRecipe, String> getIdentifiers() {
		return this.identifiers;
	}
}
