package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.world.te.TileEntityManufactory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Collections;
import java.util.List;

public class GuiManufactory extends GuiContainer {
	private static final RenderItem itemRenderer = new RenderItem();
	public static final String TEXTURE = "assets/gui/" + NuclearProgram.path("manufactory.png");

	private final TileEntityManufactory te;
	private final InventoryPlayer inventory;
	private int mouseX, mouseY;

	public GuiManufactory(InventoryPlayer inventory, TileEntityManufactory te) {
		super(new ContainerManufactory(inventory, te));
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

		if (this.te.energy > 0) {
			int scaled = this.te.getEnergyScaled(141);
			this.drawTexturedModalRect(x+184, y+17+141-scaled, 205, 141-scaled, 16, scaled);
		}
		if (this.te.progress > 0) {
			int scaled = this.te.getProgressScaled(45);
			this.drawTexturedModalRect(x+88, y+33, 0, 166, scaled, 20);
		}

		this.fontRenderer.drawString(this.te.getInvName(), x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(this.te.getInvName()) / 2), y + 6, 0x404040);
		this.fontRenderer.drawString("Inventory", x + 8, y + this.ySize - 96 + 2, 0x404040);

		if (this.te.recipe != null) {
			this.mc.renderEngine.bindTerrainTexture();
			GL11.glPushAttrib(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glPushMatrix();
			GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, this.te.recipe.icon, x+8, y+35);

			float a = (MathHelper.sin((Minecraft.getTicksRan() + partialTick) * 0.1F)+1.0F) * 0.2F + 0.2F;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, a);

			itemRenderer.field_27004_a = false;
			for (int i = 0; i < this.te.recipe.itemsIn.size(); i++) {
				List<ItemStack> displays = this.te.recipe.itemsIn.get(i).getDisplayItems();
				ItemStack display = displays.get((int) ((System.currentTimeMillis() / 500) % displays.size()));
				itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, display, x + 35 + (i%3)*18, y + 17 + (i/3)*18);
			}
			itemRenderer.field_27004_a = true;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glPopAttrib();
		}
	}
	@Override
	protected void drawGuiContainerForegroundLayer() {
		if (this.inventory.getItemStack() == null) {
			StringTranslate translate = StringTranslate.getInstance();
			int x = (this.width - this.xSize) / 2;
			int y = (this.height - this.ySize) / 2;
			int mx = this.mouseX - x;
			int my = this.mouseY - y;
			if (mx >= 183 && my >= 16 && mx < 183+18 && my < 16+143) {
				String amount = NumFormat.format(this.te.energy) + "/" + NumFormat.format(TileEntityManufactory.MAX_ENERGY) + "RF";
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

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int type) {
		super.mouseClicked(mouseX, mouseY, type);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int mx = mouseX - x;
		int my = mouseY - y;

		if (mx >= 7 && my >= 34 && mx < 7+18 && my < 34+18) {
			this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			this.mc.displayGuiScreen(new GuiSelectRecipe(this, this.te));
		}
	}
}
