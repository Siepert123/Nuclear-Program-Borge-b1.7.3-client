package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.Ingredient;

public class RecipeCategoryBlastFurnace implements IRecipeCategory<RecipeBlastFurnace> {
	private final ItemStack icon = new ItemStack(BlockInit.blastFurnace);
	private final ItemStack[] machines = new ItemStack[]{this.icon};

	public RecipeCategoryBlastFurnace() {}

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
		return "assets/gui/nuclear_program/blastFurnace.png";
	}
	@Override
	public int getWidth() {
		return 83;
	}
	@Override
	public int getHeight() {
		return 39;
	}
	@Override
	public String getTitle() {
		return "Blasting";
	}

	@Override
	public void getItems(IIngredients ingredients, RecipeBlastFurnace recipe) {
		ingredients.addInput(20, 2, recipe.in);
		ingredients.addResult(65, 2, recipe.out);
		if (recipe.byproduct != null) ingredients.addResult(65, 21, recipe.byproduct);
		ingredients.addCatalyst(20, 21, Ingredient.of(ItemInit.cokeCoal.shiftedIndex));
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, RecipeBlastFurnace recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 60, 11, this.getWidth(), this.getHeight());
		int scaled = ((Minecraft.getTicksRan() % 100) * 27) / 100;
		this.drawTexturedModalRect(tes, x+37, y+1, 176, 0, scaled, 18);
		this.drawTexturedModalRect(tes, x+2, y+2, 176, 18, 8, 35);
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, RecipeBlastFurnace recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, RecipeBlastFurnace recipe, float pt) {

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
