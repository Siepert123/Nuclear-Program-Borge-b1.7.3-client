package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.gui.GuiManufactory;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.recipe.IngredientSized;
import dev.siepert.nuclearprogram.recipe.template.RecipeGeneric;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;

public class RecipeCategoryManufactory implements IRecipeCategory<RecipeGeneric> {
	private final ItemStack icon = new ItemStack(BlockInit.manufactory);
	private final ItemStack[] machines = {this.icon};

	public RecipeCategoryManufactory() {}

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
		return GuiManufactory.TEXTURE;
	}
	@Override
	public int getWidth() {
		return 119;
	}
	@Override
	public int getHeight() {
		return 56;
	}
	@Override
	public String getTitle() {
		return "Manufacturing";
	}

	@Override
	public void getItems(IIngredients ingredients, RecipeGeneric recipe) {
		for (int i = 0; i < recipe.itemsIn.size(); i++) {
			IngredientSized in = recipe.itemsIn.get(i);
			ingredients.addInput(2 + (i%3)*18, 2 + (i/3)*18, in, in.size);
		}
		ingredients.addResult(101, 20, recipe.itemsOut.get(0));
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, RecipeGeneric recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 33, 15, this.getWidth(), this.getHeight());
		int scaled = ((Minecraft.getTicksRan() % recipe.recipeTicks) * 45) / recipe.recipeTicks;
		this.drawTexturedModalRect(tes, x+55, y+18, 0, 166, scaled, 20);
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, RecipeGeneric recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, RecipeGeneric recipe, float pt) {

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
