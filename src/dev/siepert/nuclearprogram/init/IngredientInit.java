package dev.siepert.nuclearprogram.init;

import net.minecraftborge.loader.Ingredient;

/**
 * A lot of equal ItemTag ingredients will be used,
 * as such this class holds commonly used instances of those
 * to reduce memory strain and hopefully increase performance as well.
 */
public class IngredientInit {
	public static final Ingredient ingotIron = Ingredient.of("ingotIron");
	public static final Ingredient ingotGold = Ingredient.of("ingotGold");
	public static final Ingredient ingotCopper = Ingredient.of("ingotCopper");
	public static final Ingredient ingotUranium = Ingredient.of("ingotUranium");
	public static final Ingredient ingotThorium = Ingredient.of("ingotThorium");
}
