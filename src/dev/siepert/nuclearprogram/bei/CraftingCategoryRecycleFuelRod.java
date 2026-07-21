package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.recipe.crafting.CraftingRecycleFuelRod;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class CraftingCategoryRecycleFuelRod implements IRecipeCategory<CraftingRecycleFuelRod> {
	private final ItemStack icon = new ItemStack(Block.workbench);
	private final ItemStack[] machines = {this.icon};

	public CraftingCategoryRecycleFuelRod() {

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
		return "/gui/crafting.png";
	}
	@Override
	public int getWidth() {
		return 118;
	}
	@Override
	public int getHeight() {
		return 56;
	}

	@Override
	public String getTitle() {
		return "Fuel Rod Recycling";
	}

	@Override
	public void getItems(IIngredients ingredients, CraftingRecycleFuelRod recipe) {
		ingredients.addInput(20, 20, recipe.in);
		ingredients.addResult(96, 20, recipe.out);
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, CraftingRecycleFuelRod recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 28, 15, this.getWidth(), this.getHeight());
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, CraftingRecycleFuelRod recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, CraftingRecycleFuelRod recipe, float pt) {

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
