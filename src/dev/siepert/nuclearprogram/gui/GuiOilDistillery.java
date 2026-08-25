package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistilleryController;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistillerySegment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiOilDistillery extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("oilDistillery.png");

	private final TileEntityOilDistilleryController te;
	private final InventoryPlayer inventory;
	private int mouseX, mouseY;

	public GuiOilDistillery(InventoryPlayer inventory, TileEntityOilDistilleryController te) {
		super(new ContainerOilDistillery(inventory, te));
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

		if (this.te.tankCrudeOil > 0L) {
			int scaled = this.te.getCrudeOilFillScaled(55);
			this.drawTexturedModalRect(x+26, y+14+55-scaled, 188, 166+55-scaled, 16, scaled);
		}

		if (this.te.isValidSegmentCount()) {
			int segments = this.te.getSegmentCount();
			switch (segments) {
				case 3:
					this.renderFluidOutputBar(x, y, 0, 0, this.te.getSegment(0).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 1, 3, this.te.getSegment(1).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 2, 5, this.te.getSegment(2).getFluidFillScaled(88));
					break;
				case 4:
					this.renderFluidOutputBar(x, y, 0, 0, this.te.getSegment(0).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 1, 1, this.te.getSegment(1).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 2, 3, this.te.getSegment(2).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 2, 5, this.te.getSegment(3).getFluidFillScaled(88));
					break;
				case 6:
					this.renderFluidOutputBar(x, y, 0, 0, this.te.getSegment(0).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 1, 1, this.te.getSegment(1).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 2, 2, this.te.getSegment(2).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 3, 3, this.te.getSegment(3).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 4, 4, this.te.getSegment(4).getFluidFillScaled(88));
					this.renderFluidOutputBar(x, y, 5, 5, this.te.getSegment(5).getFluidFillScaled(88));
					break;
			}
		} else {
			this.drawTexturedModalRect(x+68, y+15, 0, 166, 94, 63);
		}

		if (this.te.energy > 0) {
			int scaled = this.te.getEnergyScaled(141);
			this.drawTexturedModalRect(x+184, y+17+141-scaled, 205, 141-scaled, 16, scaled);
		}


		this.fontRenderer.drawString(this.te.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.te.getInvName()) / 2), y + 4, 0x404040);
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

			if (mx >= 25 && my >= 13 && mx < 25+18 && my < 13+57) {
				String amount = this.te.tankCrudeOil + "/" + TileEntityOilDistilleryController.TANK_CAPACITY + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(FluidInit.crudeOil.getUnlocalizedName()), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[FluidInit.crudeOil.fluidID] | 0xC0000000);
			}

			if (this.te.isValidSegmentCount() && mx >= 70 && mx < 70+90) {
				if (my >= 67 && my < 67+9) {
					if (this.te.getSegmentCount() >= 1) {
						TileEntityOilDistillerySegment segment = this.te.getSegment(0);
						String amount = segment.tank + "/" + TileEntityOilDistillerySegment.TANK_CAPACITY + "mB";
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
								Fluid.getLocalizedName(Fluid.fluidsList[segment.fluidType]), Collections.singletonList(amount),
								-1, -1,
								0xC0000000, Fluid.colorLookup[segment.fluidType] | 0xC0000000
						);
					}
				} else if (my >= 57 && my < 57+9) {
					if (this.te.getSegmentCount() >= 2) {
						TileEntityOilDistillerySegment segment = this.te.getSegment(1);
						String amount = segment.tank + "/" + TileEntityOilDistillerySegment.TANK_CAPACITY + "mB";
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
								Fluid.getLocalizedName(Fluid.fluidsList[segment.fluidType]), Collections.singletonList(amount),
								-1, -1,
								0xC0000000, Fluid.colorLookup[segment.fluidType] | 0xC0000000
						);
					}
				} else if (my >= 47 && my < 47+9) {
					if (this.te.getSegmentCount() >= 3) {
						TileEntityOilDistillerySegment segment = this.te.getSegment(2);
						String amount = segment.tank + "/" + TileEntityOilDistillerySegment.TANK_CAPACITY + "mB";
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
								Fluid.getLocalizedName(Fluid.fluidsList[segment.fluidType]), Collections.singletonList(amount),
								-1, -1,
								0xC0000000, Fluid.colorLookup[segment.fluidType] | 0xC0000000
						);
					}
				} else if (my >= 37 && my < 37+9) {
					if (this.te.getSegmentCount() >= 4) {
						TileEntityOilDistillerySegment segment = this.te.getSegment(3);
						String amount = segment.tank + "/" + TileEntityOilDistillerySegment.TANK_CAPACITY + "mB";
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
								Fluid.getLocalizedName(Fluid.fluidsList[segment.fluidType]), Collections.singletonList(amount),
								-1, -1,
								0xC0000000, Fluid.colorLookup[segment.fluidType] | 0xC0000000
						);
					}
				} else if (my >= 27 && my < 27+9) {
					if (this.te.getSegmentCount() >= 5) {
						TileEntityOilDistillerySegment segment = this.te.getSegment(4);
						String amount = segment.tank + "/" + TileEntityOilDistillerySegment.TANK_CAPACITY + "mB";
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
								Fluid.getLocalizedName(Fluid.fluidsList[segment.fluidType]), Collections.singletonList(amount),
								-1, -1,
								0xC0000000, Fluid.colorLookup[segment.fluidType] | 0xC0000000
						);
					}
				} else if (my >= 17 && my < 17+9) {
					if (this.te.getSegmentCount() >= 6) {
						TileEntityOilDistillerySegment segment = this.te.getSegment(5);
						String amount = segment.tank + "/" + TileEntityOilDistillerySegment.TANK_CAPACITY + "mB";
						drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
								Fluid.getLocalizedName(Fluid.fluidsList[segment.fluidType]), Collections.singletonList(amount),
								-1, -1,
								0xC0000000, Fluid.colorLookup[segment.fluidType] | 0xC0000000
						);
					}
				}
			}

			if (mx >= 183 && my >= 16 && mx < 183+18 && my < 16+143) {
				String amount = NumFormat.format(this.te.energy) + "/" + NumFormat.format(TileEntityOilDistilleryController.MAX_ENERGY_STORED) + "RF";
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

	private void renderFluidOutputBar(int x, int y, int bar, int type, int scaled) {
		int barY = 68 - bar * 10;
		int sourceY = 219 - type * 10;
		this.drawTexturedModalRect(x + 71, y + barY, 97, sourceY, scaled, 7);
	}
}
