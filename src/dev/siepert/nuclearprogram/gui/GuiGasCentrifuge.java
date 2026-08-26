package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityGasCentrifuge;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiGasCentrifuge extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("gasCentrifuge.png");

	private final TileEntityGasCentrifuge te;
	private final InventoryPlayer inventory;
	private int mouseX, mouseY;

	public GuiGasCentrifuge(InventoryPlayer inventory, TileEntityGasCentrifuge te) {
		super(new ContainerGasCentrifuge(inventory, te));
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

		if (this.te.fluidToProcess > 0) {
			int scaled = this.te.getFluidToProcessScaled(55);
			this.drawTexturedModalRect(x+8, y+15, 98, 166, 7, scaled);
			this.drawTexturedModalRect(x+17, y+15, 107, 166, 7, scaled);
		}
		if (this.te.fluidProcessed > 0) {
			int scaled = this.te.getFluidProcessedScaled(55);
			this.drawTexturedModalRect(x+134, y+15, 98, 166, 7, scaled);
			this.drawTexturedModalRect(x+143, y+15, 107, 166, 7, scaled);
		}
		if (this.te.progress > 0) {
			int scaled = this.te.progress * 98 / TileEntityGasCentrifuge.TICKS_PER_CENTRIFUGE;
			this.drawTexturedModalRect(x+28, y+17, 0, 166, scaled, 55);
		}
		if (this.te.energy > 0) {
			int scaled = this.te.getEnergyScaled(141);
			this.drawTexturedModalRect(x+184, y+17+141-scaled, 205, 141-scaled, 16, scaled);
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
			if (mx >= 7 && my >= 14 && mx < 7+18 && my < 14+57) {
				String amount = this.te.fluidToProcess + "/" + TileEntityGasCentrifuge.TANK_CAPACITY + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[this.te.getFluidIn()])), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, (Fluid.colorLookup[this.te.getFluidIn()]) | 0xC0000000
				);
			}
			if (mx >= 133 && my >= 14 && mx < 133+18 && my < 14+57) {
				String amount = this.te.fluidProcessed + "/" + TileEntityGasCentrifuge.TANK_CAPACITY + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[this.te.getFluidOut()])), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, (Fluid.colorLookup[this.te.getFluidOut()]) | 0xC0000000
				);
			}
			if (mx >= 183 && my >= 16 && mx < 183+18 && my < 16+143) {
				String amount = NumFormat.format(this.te.energy) + "/" + NumFormat.format(TileEntityGasCentrifuge.MAX_ENERGY_STORED) + "RF";
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
}
