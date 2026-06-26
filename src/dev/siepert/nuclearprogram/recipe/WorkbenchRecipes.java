package dev.siepert.nuclearprogram.recipe;

import net.minecraftborge.loader.Namespace;

import java.util.*;

public class WorkbenchRecipes {
	private static final WorkbenchRecipes instance = new WorkbenchRecipes();
	public static WorkbenchRecipes crafting() {
		return instance;
	}

	private Map<String, Integer> remote = null;
	private final Map<String, Integer> index = new HashMap<>();
	private final Map<String, WorkbenchRecipe> lookup = new HashMap<>();
	private final List<WorkbenchRecipe> recipes = new ArrayList<>();

	private WorkbenchRecipes() {

	}

	public void addRecipe(String name, WorkbenchRecipe recipe) {
		Namespace.validate(name);
		this.lookup.put(name, recipe);
		if (this.index.containsKey(name)) {
			this.recipes.set(this.index.get(name), recipe);
		} else {
			int index = this.recipes.size();
			this.recipes.add(recipe);
			this.index.put(name, index);
		}
	}
	public void removeRecipe(String name) {
		Namespace.validate(name);
		Integer index = this.index.remove(name);
		if (index != null) {
			this.lookup.remove(name);
			this.recipes.remove(index.intValue());
			this.reindex();
		}
	}

	public void reindex() {
		this.index.clear();
		for (String key : this.lookup.keySet()) {
			WorkbenchRecipe recipe = this.lookup.get(key);
			this.index.put(key, this.recipes.indexOf(recipe));
		}
	}

	private void validateRemoteOrThrow(Map<String, Integer> remote) {
		for (String local : this.index.keySet()) {
			if (!remote.containsKey(local)) throw new IllegalStateException("Recipe missing on server: " + local);
		}
	}
	public void setRemote(Map<String, Integer> index) {
		this.validateRemoteOrThrow(index);
		this.remote = index;
	}

	public int getIndex(String recipeID) {
		return this.index.getOrDefault(recipeID, -1);
	}
	public int getRemoteIndex(String recipeID) {
		if (this.remote == null) throw new IllegalStateException("Remote index not available");
		return this.remote.getOrDefault(recipeID, -1);
	}

	public WorkbenchRecipe getRecipe(String recipeID) {
		return this.lookup.get(recipeID);
	}

	public List<WorkbenchRecipe> getRecipeList() {
		return this.recipes;
	}
	public Map<String, Integer> getLookupMap() {
		return this.index;
	}
}
