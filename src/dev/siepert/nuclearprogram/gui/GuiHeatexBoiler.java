package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityHeatexBoiler;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistilleryController;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistillerySegment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiHeatexBoiler extends GuiContainer {
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("heatExchangingBoiler.png");

	private final TileEntityHeatexBoiler te;
	private final InventoryPlayer inventory;
	private int mouseX, mouseY;

	public GuiHeatexBoiler(InventoryPlayer inventory, TileEntityHeatexBoiler te) {
		super(new ContainerHeatexBoiler(inventory, te));
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

		if (this.te.tankWater > 0L) {
			int scaled = this.te.getTankWaterScaled(106);
			this.drawTexturedModalRect(x+35, y+42, 0, 182, scaled, 16);
		}
		if (this.te.tankSteam > 0L) {
			int scaled = this.te.getTankSteamScaled(106);
			this.drawTexturedModalRect(x+35, y+14, 0, 166, scaled, 16);
		}

		if (this.te.tankCoolantIn > 0L && Fluid.fluidsList[this.te.fluidType] != null) {
			this.mc.renderEngine.bindTexture(Fluid.fluidsList[this.te.fluidType].getStandaloneTexture(this.mc.renderEngine));
			int scaled = this.te.getTankInScaled(62);
			this.drawTexturedModalRect(x+8, y+14+62-scaled, 16, scaled, 0.0, 0.0, 1.0, scaled / 16.0);
		}
		if (this.te.tankCoolantOut > 0L && Fluid.fluidsList[this.te.fluidTypeOut] != null) {
			this.mc.renderEngine.bindTexture(Fluid.fluidsList[this.te.fluidTypeOut].getStandaloneTexture(this.mc.renderEngine));
			int scaled = this.te.getTankOutScaled(62);
			this.drawTexturedModalRect(x+152, y+14+62-scaled, 16, scaled, 0.0, 0.0, 1.0, scaled / 16.0);
		}

		this.fontRenderer.drawString(this.te.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.te.getInvName()) / 2), y + 4, 0x404040);
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		if (this.inventory.getItemStack() == null) {
			StringTranslate translate = StringTranslate.getInstance();
			int x = (this.width - this.xSize) / 2;
			int y = (this.height - this.ySize) / 2;
			int mx = this.mouseX - x;
			int my = this.mouseY - y;

			if (mx >= 43 && my >= 41 && mx < 43 + 108 && my < 41 + 18) {
				String amount = this.te.tankWater + "/" + TileEntityHeatexBoiler.TANK_CAPACITY_WATER + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(FluidInit.water.getUnlocalizedName()), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[FluidInit.water_Id] | 0xC0000000
				);
			}
			if (mx >= 43 && my >= 13 && mx < 43 + 108 && my < 13 + 18) {
				String amount = this.te.tankSteam + "/" + TileEntityHeatexBoiler.TANK_CAPACITY_STEAM + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(FluidInit.steam.getUnlocalizedName()), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[FluidInit.steam_Id] | 0xC0000000
				);
			}
			if (mx >= 7 && my >= 13 && mx < 7 + 18 && my < 13 + 64) {
				String amount = this.te.tankCoolantIn + "/" + TileEntityHeatexBoiler.TANK_CAPACITY_COOLANT + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[this.te.fluidType])), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[this.te.fluidType] | 0xC0000000
				);
			}
			if (mx >= 151 && my >= 13 && mx < 151 + 18 && my < 13 + 64) {
				String amount = this.te.tankCoolantOut + "/" + TileEntityHeatexBoiler.TANK_CAPACITY_COOLANT + "mB";
				drawTooltipWithGradientBackdrop(this, this.fontRenderer, mx + 12, my - 12,
						translate.translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[this.te.fluidTypeOut])), Collections.singletonList(amount),
						-1, -1,
						0xC0000000, Fluid.colorLookup[this.te.fluidTypeOut] | 0xC0000000
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
