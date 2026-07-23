package dev.siepert.nuclearprogram.bei;

import dev.siepert.bei.api.IIngredients;
import dev.siepert.bei.api.IRecipeCategory;
import dev.siepert.nuclearprogram.gui.GuiGasCentrifuge;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.IngredientFluid;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityGasCentrifuge;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class RecipeCategoryGasCentrifuge implements IRecipeCategory<RecipeGasCentrifuge> {
	private final ItemStack icon = new ItemStack(BlockInit.gasCentrifuge);
	private final ItemStack[] machines = {this.icon};

	public RecipeCategoryGasCentrifuge() {

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
		return GuiGasCentrifuge.TEXTURE;
	}
	@Override
	public int getWidth() {
		return 164;
	}
	@Override
	public int getHeight() {
		return 67;
	}
	@Override
	public String getTitle() {
		return "Gas Centrifuging";
	}

	@Override
	public void getItems(IIngredients ingredients, RecipeGasCentrifuge recipe) {
		ingredients.addInput(2, 2+55/2+8, IngredientFluid.create(Fluid.fluidsList[recipe.fluidIn], recipe.fluidInAmount, (byte) 1));
		if (recipe.fluidOut > 0) {
			ingredients.addResult(146, 2+55/2+8, Fluid.createItemRepresentation(Fluid.fluidsList[recipe.fluidOut], recipe.fluidOutAmount, (byte) 1));
		}
		ItemStack[] product = recipe.products;
		if (product.length > 0) {
			ingredients.addResult(101, 23, recipe.products[0]);
		}
		if (product.length > 1) {
			ingredients.addResult(119, 23, recipe.products[1]);
		}
		if (product.length > 2) {
			ingredients.addResult(101, 41, recipe.products[2]);
		}
		if (product.length > 3) {
			ingredients.addResult(119, 41, recipe.products[3]);
		}
	}

	@Override
	public void drawBackdrop(Minecraft mc, Tessellator tes, int x, int y, RecipeGasCentrifuge recipe, float pt) {
		this.drawTexturedModalRect(tes, x, y, 6, 13, this.getWidth(), this.getHeight());
		int progress = Minecraft.getTicksRan() % TileEntityGasCentrifuge.TICKS_PER_CENTRIFUGE;
		this.drawTexturedModalRect(tes, x+22, y+4, 0, 166, progress * 115 / TileEntityGasCentrifuge.TICKS_PER_CENTRIFUGE, 55);
	}
	@Override
	public void drawExtras(Minecraft mc, RenderEngine textures, int x, int y, double mouseX, double mouseY, RecipeGasCentrifuge recipe, float pt) {

	}
	@Override
	public void drawTexts(Minecraft mc, FontRenderer font, int x, int y, double mouseX, double mouseY, RecipeGasCentrifuge recipe, float pt) {
		StringTranslate translate = StringTranslate.getInstance();
		String in = translate.translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[recipe.fluidIn]));
		String out = translate.translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[recipe.fluidOut]));
		GL11.glScalef(0.5F, 0.5F, 1.0F);
		font.drawString(in + " --> " + out, (x+10)*2, (y+60)*2, 0);
		GL11.glScalef(2.0F, 2.0F, 1.0F);
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
