package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.TileEntityRTG;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiRTG extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("rtg.png");

	private final TileEntityRTG te;

	public GuiRTG(InventoryPlayer inventory, TileEntityRTG te) {
		super(new ContainerRTG(inventory, te));
		this.te = te;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick) {
		int textureID = this.mc.renderEngine.getTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureID);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.te.maxDepletion > 0) {
			int scaled = this.te.getScaledDepletion(83);
			if (scaled > 0) {
				this.drawTexturedModalRect(x+45, y+25, 0, 166, scaled, 37);
			}
		}

		if (this.te.inventory[0] == null) {
			int index = Math.toIntExact((System.currentTimeMillis() / 1000) & 1);
			this.drawTexturedModalRect(x+26, y+36, 176, index*16, 16, 16);
		}

		this.fontRenderer.drawString(this.te.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.te.getInvName()) / 2), y + 6, 0x404040);
		this.fontRenderer.drawString("Inventory", x + 8, y + this.ySize - 96 + 2, 0x404040);
	}
}
