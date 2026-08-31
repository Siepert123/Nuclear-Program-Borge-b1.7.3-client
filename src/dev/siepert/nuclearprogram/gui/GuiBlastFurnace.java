package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.TileEntityBlastFurnace;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiBlastFurnace extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("blastFurnace.png");

	private final TileEntityBlastFurnace furnace;
	public GuiBlastFurnace(InventoryPlayer inventory, TileEntityBlastFurnace furnace) {
		super(new ContainerBlastFurnace(inventory, furnace));
		this.furnace = furnace;

		this.xSize = 176;
		this.ySize = 150;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick) {
		int textureID = this.mc.renderEngine.getTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureID);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		this.fontRenderer.drawString(this.furnace.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.furnace.getInvName()) / 2), y + 4, 0xFFFFFF);
		this.fontRenderer.drawString("Inventory", x + 8, y + this.ySize - 92, 0x000000);
	}
}
