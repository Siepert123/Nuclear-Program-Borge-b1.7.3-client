package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipe;
import dev.siepert.nuclearprogram.util.ChancedStack;
import dev.siepert.nuclearprogram.world.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.Ingredient;

import java.util.Map;
import java.util.stream.Collectors;

public class RecipeCategoryWorkbench implements IRecipeCategory<WorkbenchRecipe> {
	private final ItemStack icon = new ItemStack(BlockInit.workbench, 1, BlockWorkbench.STEEL);
	private final ItemStack[] machines = new ItemStack[]{
			new ItemStack(BlockInit.workbench, 1, BlockWorkbench.IRON),
			new ItemStack(BlockInit.workbench, 1, BlockWorkbench.STEEL),
	};

	public RecipeCategoryWorkbench() {}

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
		return "assets/gui/nuclear_program/workbenchBEI.png";
	}
	@Override
	public int getWidth() {
		return 92;
	}
	@Override
	public int getHeight() {
		return 64;
	}
	@Override
	public String getTitle() {
		return "Workbench Crafting";
	}

	@Override
	public void getItems(IIngredients ingredients, WorkbenchRecipe recipe) {
		Map<Ingredient, Integer> in = recipe.ingredients();
		if (in.size() > 10) {
			System.err.println("Workbench recipe with " + in.size() + " ingredients; will not display correctly!");
		}
		int next = 0;
		for (Map.Entry<Ingredient, Integer> entry : in.entrySet()) {
			Ingredient input = entry.getKey();
			int count = entry.getValue();
			int x = next % 5;
			int y = next / 5;
			ingredients.addInput(2 + 18*x, 9 + 18*y, input, count);
			next++;
		}
		ingredients.addResults(56, 46, recipe.results().stream().map(ChancedStack::stack).collect(Collectors.toList()));
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, WorkbenchRecipe recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 0, 0, this.getWidth(), this.getHeight());
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, WorkbenchRecipe recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, WorkbenchRecipe recipe, float pt) {
		font.drawStringWithShadow("Tier " + (recipe.tier()+1) + " workbench", x, y-1, 0x00FFFF);
	}

	public void drawTexturedModalRect(Tessellator tes, int x, int y, int srcX, int srcY, int w, int h) {
		float scaleX = 0.0078125f;
		float scaleY = 0.015625f;
		tes.addVertexWithUV(x, y + h, 0, (float)(srcX) * scaleX, (float)(srcY + h) * scaleY);
		tes.addVertexWithUV(x + w, y + h, 0, (float)(srcX + w) * scaleX, (float)(srcY + h) * scaleY);
		tes.addVertexWithUV(x + w, y, 0, (float)(srcX + w) * scaleX, (float)(srcY) * scaleY);
		tes.addVertexWithUV(x, y, 0, (float)(srcX) * scaleX, (float)(srcY) * scaleY);
	}
}
