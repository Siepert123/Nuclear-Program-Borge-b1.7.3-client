package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.Nothing;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityPBR;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraftborge.loader.Icon;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiPBR extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("pbr.png");
	private static final DecimalFormat FLUX_FORMAT = new DecimalFormat("#.#");

	private final TileEntityPBR te;
	private final InventoryPlayer inventory;
	private int mouseX, mouseY;

	public GuiPBR(InventoryPlayer inventory, TileEntityPBR te) {
		super(new ContainerPBR(inventory, te));
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
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.te.tankCO2 > 0L) {
			int scaled = this.te.getTankCO2Scaled(62);
			this.drawTexturedModalRect(x+134, y+14+62-scaled, 196, 0, 16, scaled);
		}
		if (this.te.tankCO2Hot > 0L) {
			int scaled = this.te.getTankCO2HotScaled(62);
			this.drawTexturedModalRect(x+152, y+14+62-scaled, 212, 0, 16, scaled);
		}
		if (this.te.pebbleSource != null) {
			int scaled = this.te.getSourceDepletionScaled(28);
			if (scaled > 0) {
				this.drawTexturedModalRect(x+10, y+31, 176, 0, 12, scaled);
			}
		}
		if (this.te.pebbleFuel != null) {
			int scaled = this.te.getFuelDepletionScaled(28);
			if (scaled > 0) {
				this.drawTexturedModalRect(x+64, y+31, 176, 0, 12, scaled);
			}
		}

		this.mc.renderEngine.bindTerrainTexture();
		if (this.te.pebbleSource != null) {
			Icon texture = this.te.pebbleSource.itemTexture;
			this.drawTexturedModalRect(x+26, y+37, 16, 16,
					texture.getU(0), texture.getV(0), texture.getU(1), texture.getV(1)
			);
		}
		if (this.te.pebbleFuel != null) {
			Icon texture = this.te.pebbleFuel.itemTexture;
			this.drawTexturedModalRect(x+88, y+37, 16, 16,
					texture.getU(0), texture.getV(0), texture.getU(1), texture.getV(1)
			);
		}

		this.fontRenderer.drawString(this.te.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.te.getInvName()) / 2), y + 4, 0x404040);

		GL11.glPushMatrix();
		GL11.glScalef(0.5F, 0.5F, 1.0F);
		String temp = NumFormat.format(this.te.heat) + "TF";
		this.fontRenderer.drawString(temp, (x + 119)*2-(this.fontRenderer.getStringWidth(temp) / 2), (y + 41)*2, 0x88FF88);
		String flux = FLUX_FORMAT.format(this.te.getCoreFlux());
		this.fontRenderer.drawString(flux, (x + 119)*2-(this.fontRenderer.getStringWidth(flux) / 2), (y + 45)*2, 0x88FF88);
		GL11.glPopMatrix();
	}

	private final ItemStack tempStack = new ItemStack(0, 1, 0);
	private final List<String> tooltip = new ArrayList<>(4);
	@Override
	protected void drawGuiContainerForegroundLayer() {
		this.tooltip.clear();
		if (this.inventory.getItemStack() == null) {
			StringTranslate translate = StringTranslate.getInstance();
			int x = (this.width - this.xSize) / 2;
			int y = (this.height - this.ySize) / 2;
			int mx = this.mouseX - x;
			int my = this.mouseY - y;

			if (mx >= 26 && my >= 37 && mx < 26+16 && my < 37+16) {
				if (this.te.pebbleSource != null) {
					this.tempStack.itemID = this.te.pebbleSource.shiftedIndex;
					this.te.pebbleSource.getTooltip(this.tempStack, this.tooltip, false);
					drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
							translate.translateNamedKey(this.te.pebbleSource.getItemNameIS(this.tempStack)), this.tooltip
					);
				}
				return;
			}
			if (mx >= 88 && my >= 37 && mx < 88+16 && my < 37+16) {
				if (this.te.pebbleFuel != null) {
					this.tempStack.itemID = this.te.pebbleFuel.shiftedIndex;
					this.te.pebbleFuel.getTooltip(this.tempStack, this.tooltip, false);
					drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
							translate.translateNamedKey(this.te.pebbleFuel.getItemNameIS(this.tempStack)), this.tooltip
					);
				}
				return;
			}
			if (mx >= 79 && my >= 13 && mx < 79+8 && my < 13+64) {
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						"Fuel Pebbles: " + this.te.fuelCount + "/16", Collections.emptyList()
				);
				return;
			}
			if (mx >= 105 && my >= 33 && mx < 105+27 && my < 33+24) {
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						"Reactor Statistics", Arrays.asList(
								"Core temperature: " + NumFormat.format(this.te.heat) + "TF",
								"Core flux: " + FLUX_FORMAT.format(this.te.getCoreFlux()))
				);
				return;
			}
			if (mx >= 133 & my >= 13 && mx < 133+18 && my < 13+64) {
				String amount = this.te.tankCO2 + "/" + TileEntityPBR.CAPACITY + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(FluidInit.carbonDioxide.getUnlocalizedName()), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[FluidInit.carbonDioxide_Id] | 0xC0000000
				);
				return;
			}
			if (mx >= 151 & my >= 13 && mx < 151+18 && my < 13+64) {
				String amount = this.te.tankCO2Hot + "/" + TileEntityPBR.CAPACITY + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(FluidInit.carbonDioxideHot.getUnlocalizedName()), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[FluidInit.carbonDioxideHot_Id] | 0xC0000000
				);
				return;
			}
			Nothing.none();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		super.drawScreen(mouseX, mouseY, partialTick);
	}
}
