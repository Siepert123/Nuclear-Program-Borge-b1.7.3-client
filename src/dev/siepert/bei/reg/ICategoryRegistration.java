package dev.siepert.bei.reg;

import dev.siepert.bei.api.IRecipeCategory;

public interface ICategoryRegistration {
	void registerCategory(String categoryUID, IRecipeCategory<?> category);
}
