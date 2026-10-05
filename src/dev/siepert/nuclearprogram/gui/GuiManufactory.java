package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.te.TileEntityManufactory;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiManufactory extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("manufactory.png");

	private final TileEntityManufactory te;
	private final InventoryPlayer inventory;
	private int mouseX, mouseY;

	public GuiManufactory(InventoryPlayer inventory, TileEntityManufactory te) {
		super(new ContainerManufactory(inventory, te));
		this.te = te;
		this.inventory = inventory;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick) {
		int textureID = this.mc.renderEngine.getTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureID);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize+29, this.ySize);

		if (this.te.energy > 0) {
			int scaled = this.te.getEnergyScaled(141);
			this.drawTexturedModalRect(x+184, y+17+141-scaled, 205, 141-scaled, 16, scaled);
		}
		if (this.te.progress > 0) {
			int scaled = this.te.getProgressScaled(45);
			this.drawTexturedModalRect(x+88, y+33, 0, 166, scaled, 20);
		}

		this.fontRenderer.drawString(this.te.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.te.getInvName()) / 2), y + 6, 0x404040);
		this.fontRenderer.drawString("Inventory", x + 8, y + this.ySize - 96 + 2, 0x404040);
	}
	@Override
	protected void drawGuiContainerForegroundLayer() {
		if (this.inventory.getItemStack() == null) {
			StringTranslate translate = StringTranslate.getInstance();
			int x = (this.width - this.xSize) / 2;
			int y = (this.height - this.ySize) / 2;
			int mx = this.mouseX - x;
			int my = this.mouseY - y;
			if (mx >= 183 && my >= 16 && mx < 183+18 && my < 16+143) {
				String amount = NumFormat.format(this.te.energy) + "/" + NumFormat.format(TileEntityManufactory.MAX_ENERGY) + "RF";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						amount, Collections.emptyList(),
						-1, -1,
						0xC0FF0000, 0xC07F0000
				);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		super.drawScreen(mouseX, mouseY, partialTick);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int type) {
		super.mouseClicked(mouseX, mouseY, type);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int mx = mouseX - x;
		int my = mouseY - y;

		if (mx >= 7 && my >= 34 && mx < 7+18 && my < 34+18) {
			this.te.setRecipeID(0);
		}
	}
}
