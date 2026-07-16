package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.Ingredient;

public class RecipeCategoryBloomery implements IRecipeCategory<RecipeBloomery> {
	private final ItemStack icon = new ItemStack(BlockInit.bloomeryIdle);
	private final ItemStack[] machines = new ItemStack[]{this.icon};

	public RecipeCategoryBloomery() {}

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
		return "assets/gui/nuclear_program/bloomery.png";
	}
	@Override
	public int getWidth() {
		return 66;
	}
	@Override
	public int getHeight() {
		return 45;
	}
	@Override
	public String getTitle() {
		return "Blooming";
	}

	@Override
	public void getItems(IIngredients ingredients, RecipeBloomery recipe) {
		ingredients.addInput(8, 2, Ingredient.of(recipe.in));
		ingredients.addResult(48, 2, recipe.out.copy());
		if (recipe.byproduct != null) ingredients.addResult(48, 23, recipe.byproduct.copy());
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, RecipeBloomery recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 72, 10, this.getWidth(), this.getHeight());
		int scaled = ((Minecraft.getTicksRan() % recipe.ticks) * 22) / recipe.ticks;
		this.drawTexturedModalRect(tes, x+25, y+2, 208, 0, scaled, 16);
		this.drawTexturedModalRect(tes, x+2, y+29, 176, 36, 28, 14);
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, RecipeBloomery recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, RecipeBloomery recipe, float pt) {
		String seconds = (recipe.ticks * 0.05F) + "s";
		font.drawString(seconds, x + this.getWidth() - font.getStringWidth(seconds) - 2, y + this.getHeight() - 10, 0xFFFFFF);
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
