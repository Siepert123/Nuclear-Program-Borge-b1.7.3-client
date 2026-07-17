package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.TileEntityBloomery;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiBloomery extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("bloomery.png");

	private final TileEntityBloomery furnace;

	public GuiBloomery(InventoryPlayer inventory, TileEntityBloomery furnace) {
		super(new ContainerBloomery(inventory, furnace));
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

		if (this.furnace.fuelHeap > 0) {
			int scaled = this.furnace.fuelHeap * 14 / (TileEntityBloomery.MAX_FUEL_HEAP+1) + 1;
			this.drawTexturedModalRect(x+74, y+39+(14-scaled), 176, (this.furnace.visuallyBurning() ? 36 : 22), 28, scaled);
		}
		int maxRecipeTicks = this.furnace.lastRecipe != null ? this.furnace.lastRecipe.recipeTime : this.furnace.recipeTicksMax;
		if (this.furnace.recipeTicks > 0 && maxRecipeTicks > 0) {
			int scaled = this.furnace.recipeTicks * 22 / maxRecipeTicks;
			this.drawTexturedModalRect(x+97, y+12, 208, 0, scaled, 16);
		}

		this.drawTexturedModalRect(x+72, y+33, 176, 0, 32, 22);

		this.fontRenderer.drawString(this.furnace.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.furnace.getInvName()) / 2), y + 3, 0xFFFFFF);
		this.fontRenderer.drawString("Inventory", x + 8, y + this.ySize - 92, 0x404040);
	}
}
