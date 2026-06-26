package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityFurnaceBuilder;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiFurnaceBuilder extends GuiContainer {
	private final TileEntityFurnaceBuilder furnace;

	public GuiFurnaceBuilder(InventoryPlayer inventory, TileEntityFurnaceBuilder furnace) {
		super(new ContainerFurnaceBuilder(inventory, furnace));
		this.furnace = furnace;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick) {
		int textureID = this.mc.renderEngine.getTexture("/gui/furnace.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureID);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.furnace.isBurning()) {
			int scaled = this.furnace.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(x + 56, y + 36 + 12 - scaled, 176, 12 - scaled, 14, scaled + 2);
		}

		if (this.furnace.furnaceCookTime > 0) {
			int scaled = this.furnace.getCookProgressScaled(24);
			this.drawTexturedModalRect(x + 79, y + 34, 176, 14, scaled + 1, 16);
		}

		this.fontRenderer.drawString(this.furnace.getInvName(), x + (this.ySize / 2) - (this.fontRenderer.getStringWidth(this.furnace.getInvName()) / 2), y + 6, 0x404040);
		this.fontRenderer.drawString("Inventory", x + 8, y + this.ySize - 96 + 2, 0x404040);
	}
}
