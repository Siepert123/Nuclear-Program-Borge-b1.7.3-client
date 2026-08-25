package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityDerrick;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiDerrick extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("derrick.png");

	private final TileEntityDerrick te;
	private final InventoryPlayer inventory;
	private int mouseX, mouseY;

	public GuiDerrick(InventoryPlayer inventory, TileEntityDerrick te) {
		super(new ContainerDerrick(inventory, te));
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

		if (this.te.getDrillDepth() > 0) {
			int scaled = this.te.getDrillDepthScaled(55);
			this.drawTexturedModalRect(x+34, y+14, 222, 1, 8, scaled);
		}
		if (this.te.tankCrudeOil > 0) {
			int scaled = this.te.getCrudeOilFillScaled(62);
			this.drawTexturedModalRect(x+62, y+14+62-scaled, 240, 62-scaled, 16, scaled);
		}
		if (this.te.tankNaturalGas > 0) {
			int scaled = this.te.getNaturalGasFillScaled(62);
			this.drawTexturedModalRect(x+134, y+14+62-scaled, 240, 63+62-scaled, 16, scaled);
		}
		if (this.te.energy > 0) {
			int scaled = this.te.getEnergyScaled(141);
			this.drawTexturedModalRect(x+184, y+17+141-scaled, 205, 141-scaled, 16, scaled);
		}

		this.fontRenderer.drawString(this.te.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.te.getInvName()) / 2), y + 5, 0x404040);
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

			if (mx >= 33 && my >= 13 && mx < 33+10 && my < 13+57) {
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						"Depth: " + this.te.getDrillDepth() + "m", Collections.emptyList()
				);
			}
			if (mx >= 61 && my >= 13 && mx < 61+18 && my < 13+64) {
				String amount = this.te.tankCrudeOil + "/" + TileEntityDerrick.TANK_CAPACITY + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(FluidInit.crudeOil.getUnlocalizedName()), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[FluidInit.crudeOil.fluidID] | 0xC0000000);
			}
			if (mx >= 133 && my >= 13 && mx < 133+18 && my < 13+64) {
				String amount = this.te.tankNaturalGas + "/" + TileEntityDerrick.TANK_CAPACITY + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(FluidInit.naturalGas.getUnlocalizedName()), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[FluidInit.naturalGas.fluidID] | 0xC0000000);
			}
			if (mx >= 183 && my >= 16 && mx < 183+18 && my < 16+143) {
				String amount = NumFormat.format(this.te.energy) + "/" + NumFormat.format(TileEntityDerrick.MAX_ENERGY_STORED) + "RF";
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
