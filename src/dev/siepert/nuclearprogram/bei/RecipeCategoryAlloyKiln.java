package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.gui.GuiAlloyKiln;
import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.Ingredient;

public class RecipeCategoryAlloyKiln implements IRecipeCategory	<RecipeAlloyKiln> {
	private final ItemStack icon = new ItemStack(BlockInit.alloyKiln);
	private final ItemStack[] machines = new ItemStack[]{new ItemStack(BlockInit.alloyKiln)};

	public RecipeCategoryAlloyKiln() {}

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
		return GuiAlloyKiln.TEXTURE;
	}
	@Override
	public int getWidth() {
		return 142;
	}
	@Override
	public int getHeight() {
		return 48;
	}

	@Override
	public String getTitle() {
		return "Alloying in Kiln";
	}
	@Override
	public void getItems(IIngredients ingredients, RecipeAlloyKiln recipe) {
		ingredients.addInput(2, 2, Ingredient.of(recipe.in1));
		ingredients.addInput(58, 2, Ingredient.of(recipe.in2));
		ingredients.addResult(120, 14, recipe.out);
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, RecipeAlloyKiln recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 14, 22, this.getWidth(), this.getHeight());
		this.drawTexturedModalRect(tes, x+30, y+13, 176, 16, 14, 14);
		int progress = Minecraft.getTicksRan() % recipe.ticks;
		this.drawTexturedModalRect(tes, x+75, y+14, 176, 0, progress * 40 / recipe.ticks, 16);
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, RecipeAlloyKiln recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, RecipeAlloyKiln recipe, float pt) {
		String seconds = (recipe.ticks * 0.05F) + "s";
		font.drawString(seconds, x + this.getWidth() - font.getStringWidth(seconds) - 2, y + this.getHeight() - 10, 0x000000);
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
