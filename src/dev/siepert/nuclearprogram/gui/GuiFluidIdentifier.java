package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.util.collect.SizedIntArrayList;
import dev.siepert.nuclearprogram.util.math.MouseArea;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Icon;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class GuiFluidIdentifier extends GuiScreen {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("selectFluidIdentifier.png");

	private static final int COLUMNS = 5;
	private static final int ROWS = 10;

	private final ItemStack identifier;
	private final EntityPlayer player;
	private int selection;
	private int page;
	private String searching = "";
	private static final IntList options = new SizedIntArrayList(256);

	private static final MouseArea prevPageArea = new MouseArea(4, 195, 18, 18);
	private static final MouseArea nextPageArea = new MouseArea(76, 195, 18, 18);
	private static final MouseArea doneArea = new MouseArea(40, 195, 18, 18);
	private static final MouseArea[] fluidAreas = new MouseArea[COLUMNS*ROWS];

	static {
		for (int column = 0; column < COLUMNS; column++) {
			for (int row = 0; row < ROWS; row++) {
				fluidAreas[column+row*5] = new MouseArea(5+column*18, 13+row*18, 16, 16);
			}
		}
	}

	public GuiFluidIdentifier(ItemStack identifier, EntityPlayer player) {
		this.identifier = identifier;
		this.player = player;

		this.selection = -1;
		this.page = 0;

		this.gatherFluids(fluid -> true);
	}

	private void gatherFluids(Predicate<? super Fluid> filter) {
		options.clear();
		for (int i = 0; i < Fluid.ID_SIZE; i++) {
			if (Fluid.hasFluidIdentifier[i] && filter.test(Fluid.fluidsList[i])) {
				options.add(i);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		if (this.player.inventory.getCurrentItem() != this.identifier) this.mc.displayGuiScreen(null);
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

		this.drawCenteredString(this.fontRenderer, "Fluid Selection", x + 49, y + 2, 0xFFFFFFFF);
		this.drawCenteredString(this.fontRenderer, this.searching, x + 49, y + 216, 0xFFFFFFFF);

		this.mc.renderEngine.bindTerrainTexture();
		for (int i = this.getStartIdx(); i < Math.min(options.size(), this.getStartIdx() + COLUMNS*ROWS); i++) {
			Icon tex = ItemInit.fluid.itemTexture;
			int color = Fluid.colorLookup[options.get(i)];
			float red = ((color >> 16) & 0xFF) / 255.0F;
			float green = ((color >> 8) & 0xFF) / 255.0F;
			float blue = ((color) & 0xFF) / 255.0F;
			GL11.glColor3f(red, green, blue);
			this.drawTexturedModalRect(x+5+(i%COLUMNS)*18, y+13+(i/COLUMNS)*18, 16, 16, tex.getU(0.0), tex.getV(0.0), tex.getU(1.0), tex.getV(1.0));
		}

		if (prevPageArea.isInArea(mx, my)) {
			drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12, "Previous page", Collections.emptyList());
		} else if (nextPageArea.isInArea(mx, my)) {
			drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12, "Next page", Collections.emptyList());
		} else if (doneArea.isInArea(mx, my)) {
			drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12, "Apply", Collections.emptyList());
		} else {
			for (int i = 0; i < fluidAreas.length; i++) {
				if (fluidAreas[i].isInArea(mx, my)) {
					if (this.getStartIdx()+i < options.size()) {
						int color = Fluid.colorLookup[options.get(this.getStartIdx()+i)];
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mouseX + 12, mouseY - 12,
								Fluid.getLocalizedName(Fluid.fluidsList[options.get(this.getStartIdx()+i)]), Collections.emptyList(),
								-1, -1, 0xC0000000, color | 0xC0000000);
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
				this.identifier.setItemDamage(options.get(this.selection));
				this.player.isSwinging = true;
			}
			this.mc.displayGuiScreen(null);
		} else {
			for (int i = 0; i < fluidAreas.length; i++) {
				if (fluidAreas[i].isInArea(mx, my)) {
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
		super.keyTyped(character, code);
		if (ChatAllowedCharacters.allowedCharacters.indexOf(character) != -1) {
			if (this.searching.length() < 16) {
				this.selection = -1;
				this.searching += character;
				this.gatherFluids(fluid -> Fluid.getLocalizedName(fluid).toLowerCase().contains(this.searching.toLowerCase()));
			}
		} else {
			if (code == Keyboard.KEY_BACK && !this.searching.isEmpty()) {
				this.selection = -1;
				this.searching = this.searching.substring(0, this.searching.length() - 1);
				if (this.searching.isEmpty()) {
					this.gatherFluids(fluid -> true);
				} else {
					this.gatherFluids(fluid -> Fluid.getLocalizedName(fluid).toLowerCase().contains(this.searching.toLowerCase()));
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
