package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.recipe.template.MachineRecipesManager;
import dev.siepert.nuclearprogram.recipe.template.RecipeGeneric;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.util.collect.SizedIntArrayList;
import dev.siepert.nuclearprogram.util.math.MouseArea;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityMachineBase;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;

public class GuiSelectRecipe extends GuiScreen {
	public static final RenderItem itemRenderer = new RenderItem();
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("selectRecipe.png");

	private static final int COLUMNS = 5;
	private static final int ROWS = 10;

	private final GuiScreen parent;
	private final TileEntityMachineBase te;
	private final MachineRecipesManager<?, ?> manager;
	private int selection;
	private int page;
	private String searching = "";
	private static final IntList options = new SizedIntArrayList(256);

	private static final MouseArea prevPageArea = new MouseArea(4, 195, 18, 18);
	private static final MouseArea nextPageArea = new MouseArea(76, 195, 18, 18);
	private static final MouseArea doneArea = new MouseArea(40, 195, 18, 18);
	private static final MouseArea[] recipeAreas = new MouseArea[COLUMNS*ROWS];

	static {
		for (int column = 0; column < COLUMNS; column++) {
			for (int row = 0; row < ROWS; row++) {
				recipeAreas[column+row*5] = new MouseArea(5+column*18, 13+row*18, 16, 16);
			}
		}
	}

	public GuiSelectRecipe(GuiScreen parent, TileEntityMachineBase te) {
		this.parent = parent;
		this.te = te;
		this.manager = Objects.requireNonNull(te.getRecipesManager(), "manager");

		this.selection = -1;
		this.page = 0;

		this.gatherRecipes(recipe -> true);
	}

	private void gatherRecipes(Predicate<? super RecipeGeneric> filter) {
		options.clear();
		for (RecipeGeneric recipe : this.manager.recipes.recipes) {
			if (filter.test(recipe)) {
				options.add(recipe.recipeID);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		if (this.te.isInvalid()) this.mc.displayGuiScreen(null);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		super.drawScreen(mouseX, mouseY, partialTick);

		int textureID = this.mc.renderEngine.getTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureID);

		int x = (this.width - 98) / 2;
		int y = (this.height - 226) / 2;
		int mx = mouseX-x;
		int my = mouseY-y;

		this.drawTexturedModalRect(x, y, 0, 0, 109, 226);
		if (this.selection >= this.getStartIdx() && this.selection < Math.min(options.size(), this.getStartIdx() + COLUMNS*ROWS)) {
			this.drawTexturedModalRect(x+3+(this.selection%COLUMNS)*18, y+11+(this.selection/COLUMNS)*18,
					0, 226, 20, 20);
		}

		this.drawCenteredString(this.fontRenderer, "Recipe Selection", x + 49, y + 2, 0xFFFFFFFF);
		this.drawCenteredString(this.fontRenderer, this.searching, x + 49, y + 216, 0xFFFFFFFF);

		this.mc.renderEngine.bindTerrainTexture();
		GL11.glPushMatrix();
		GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		for (int i = this.getStartIdx(); i < Math.min(options.size(), this.getStartIdx() + COLUMNS*ROWS); i++) {
			ItemStack icon = this.manager.getRecipe(options.get(i)).icon;
			itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, icon, x+5+(i%COLUMNS)*18, y+13+(i/COLUMNS)*18);
		}
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();

		if (prevPageArea.isInArea(mx, my)) {
			drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12, "Previous page", Collections.emptyList());
		} else if (nextPageArea.isInArea(mx, my)) {
			drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12, "Next page", Collections.emptyList());
		} else if (doneArea.isInArea(mx, my)) {
			drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12, "Apply", Collections.emptyList());
		} else {
			for (int i = 0; i < recipeAreas.length; i++) {
				if (recipeAreas[i].isInArea(mx, my)) {
					if (this.getStartIdx()+i < options.size()) {
						RecipeGeneric recipe = this.manager.getRecipe(options.get(this.getStartIdx()+i));
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12,
								recipe.name, Collections.emptyList());
					}
					break;
				}
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int type) {
		if (type != 0) return;

		int x = (this.width - 98) / 2;
		int y = (this.height - 226) / 2;

		int mx = mouseX-x;
		int my = mouseY-y;

		if (prevPageArea.isInArea(mx, my)) {
			if (this.tryPreviousPage()) {
				this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			}
		} else if (nextPageArea.isInArea(mx, my)) {
			if (this.tryNextPage()) {
				this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			}
		} else if (doneArea.isInArea(mx, my)) {
			this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			if (this.selection != -1) {
				this.te.setRecipeID(options.get(this.selection));
			} else this.te.setRecipeID(-1);
			this.mc.displayGuiScreen(this.parent);
		} else {
			for (int i = 0; i < recipeAreas.length; i++) {
				if (recipeAreas[i].isInArea(mx, my)) {
					if (this.getStartIdx()+i < options.size()) {
						this.selection = this.getStartIdx()+i;
						this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					}
					return;
				}
			}
		}
	}

	@Override
	protected void keyTyped(char character, int code) {
		if (code == 1) {
			this.mc.displayGuiScreen(this.parent);
		} else {
			if (ChatAllowedCharacters.allowedCharacters.indexOf(character) != -1) {
				if (this.searching.length() < 16) {
					this.selection = -1;
					this.searching += character;
					this.gatherRecipes(recipe -> recipe.name.toLowerCase().contains(this.searching.toLowerCase()));
				}
			} else {
				if (code == Keyboard.KEY_BACK && !this.searching.isEmpty()) {
					this.selection = -1;
					this.searching = this.searching.substring(0, this.searching.length() - 1);
					if (this.searching.isEmpty()) {
						this.gatherRecipes(recipe -> true);
					} else {
						this.gatherRecipes(recipe -> recipe.name.toLowerCase().contains(this.searching.toLowerCase()));
					}
				}
			}
		}
	}

	private int getStartIdx() {
		return this.page * COLUMNS * ROWS;
	}
	private int calculatePages() {
		return options.size() / (COLUMNS * ROWS);
	}

	private boolean tryPreviousPage() {
		if (this.page == 0) return false;
		this.page--;
		return true;
	}
	private boolean tryNextPage() {
		if (this.page < this.calculatePages()) {
			this.page++;
			return true;
		}
		return false;
	}
}
