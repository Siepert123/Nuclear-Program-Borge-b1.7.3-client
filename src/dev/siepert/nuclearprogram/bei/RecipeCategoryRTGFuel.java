package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.gui.GuiRTG;
import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.Ingredient;

public class RecipeCategoryRTGFuel implements IRecipeCategory<RecipeRTGFuel> {
	private final ItemStack icon = new ItemStack(BlockInit.rtg);
	private final ItemStack[] machines = {this.icon};

	public RecipeCategoryRTGFuel() {

	}

	@Override
	public ItemStack[] getCategoryMachines() {
		return this.machines;
	}
	@Override
	public ItemStack getCategoryIcon() {
		return this.icon;
	}

	@Override
	public String getBackdropTexture() {
		return GuiRTG.TEXTURE;
	}
	@Override
	public int getWidth() {
		return 131;
	}
	@Override
	public int getHeight() {
		return 37;
	}
	@Override
	public String getTitle() {
		return "RTG Fuel";
	}

	@Override
	public void getItems(IIngredients ingredients, RecipeRTGFuel recipe) {
		ingredients.addInput(2, 11, Ingredient.of(recipe.in));
		ingredients.addResult(110, 11, recipe.out);
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, RecipeRTGFuel recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 24, 25, 131, 37);
		int progress = Minecraft.getTicksRan() % recipe.ticks;
		this.drawTexturedModalRect(tes, x+21, y, 0, 166, progress * 83 / recipe.ticks, 37);
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, RecipeRTGFuel recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, RecipeRTGFuel recipe, float pt) {
		font.drawString("Potency: " + recipe.watts + " watt", x, y, 0x0);
	}

	public void drawTexturedModalRect(Tessellator tes, int x, int y, int srcX, int srcY, int w, int h) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		tes.addVertexWithUV(x, y + h, 0, (float)(srcX) * var7, (float)(srcY + h) * var8);
		tes.addVertexWithUV(x + w, y + h, 0, (float)(srcX + w) * var7, (float)(srcY + h) * var8);
		tes.addVertexWithUV(x + w, y, 0, (float)(srcX + w) * var7, (float)(srcY) * var8);
		tes.addVertexWithUV(x, y, 0, (float)(srcX) * var7, (float)(srcY) * var8);
	}
}
