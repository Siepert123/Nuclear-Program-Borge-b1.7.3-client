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
	public static final Ingredient ingotAluminium = Ingredient.of("ingotAluminium");
	public static final Ingredient ingotLead = Ingredient.of("ingotLead");
	public static final Ingredient ingotTitanium = Ingredient.of("ingotTitanium");
	public static final Ingredient ingotTungsten = Ingredient.of("ingotTungsten");
	public static final Ingredient ingotSteel = Ingredient.of("ingotSteel");
	public static final Ingredient ingotElectrum = Ingredient.of("ingotElectrum");
	public static final Ingredient ingotUranium = Ingredient.of("ingotUranium");
	public static final Ingredient ingotUraniumLE = Ingredient.of("ingotUraniumLE");
	public static final Ingredient ingotUraniumME = Ingredient.of("ingotUraniumME");
	public static final Ingredient ingotUraniumHE = Ingredient.of("ingotUraniumHE");
	public static final Ingredient ingotThorium = Ingredient.of("ingotThorium");
	public static final Ingredient ingotKaupium = Ingredient.of("ingotKaupium");
	public static final Ingredient ingotYanoizedKaupium = Ingredient.of("ingotYanoizedKaupium");
}
