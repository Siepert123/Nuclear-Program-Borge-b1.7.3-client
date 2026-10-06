package dev.siepert.nuclearprogram.recipe.template;

import java.util.*;

public abstract class RecipesGeneric<T extends RecipeGeneric> {
	public final List<T> recipes = new ArrayList<>();
	public final Map<String, T> recipesMap = new HashMap<>();

	public RecipesGeneric() {}

	@SuppressWarnings("unchecked")
	protected void add(RecipeGeneric recipe) {
		if (this.recipesMap.containsKey(recipe.name)) throw new IllegalArgumentException("Duplicate recipe ID: " + recipe.name);
		recipe.recipeID = this.recipes.size();
		this.recipes.add((T)recipe);
		this.recipesMap.put(recipe.name, (T)recipe);
	}

	public abstract void initialize();

	public T getRecipe(String name) {
		return this.recipesMap.get(name);
	}
	public T getRecipeOrThrow(String name) {
		return Objects.requireNonNull(this.getRecipe(name), "recipe");
	}

	public abstract String getName();

	public boolean includesEnergy() {
		return true;
	}
	public boolean includesTime() {
		return true;
	}
	public void addAdditionalData(T recipe, List<String> tooltip) {

	}
}
