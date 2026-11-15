package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.TileEntityAlloyKiln;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiAlloyKiln extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("alloyKiln.png");

	private final TileEntityAlloyKiln furnace;

	public GuiAlloyKiln(InventoryPlayer inventory, TileEntityAlloyKiln furnace) {
		super(new ContainerAlloyKiln(inventory, furnace));
		this.furnace = furnace;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick) {
		int textureID = this.mc.renderEngine.getTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureID);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.furnace.isBurning()) {
			int scaled = this.furnace.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(x + 44, y + 35 + 12 - scaled, 176, 16+12 - scaled, 14, scaled + 2);
		}

		if (this.furnace.furnaceCookTime > 0) {
			int scaled = this.furnace.getCookProgressScaled(40);
			this.drawTexturedModalRect(x + 89, y + 36, 176, 0, scaled + 1, 16);
		}

		this.fontRenderer.drawString(this.furnace.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.furnace.getInvName()) / 2), y + 6, 0x404040);
		this.fontRenderer.drawString("Inventory", x + 8, y + this.ySize - 96 + 2, 0x404040);
	}
}
