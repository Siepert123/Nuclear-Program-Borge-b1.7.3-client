package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.network.NuclearProgramNetHandler;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipe;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipes;
import net.minecraft.src.*;
import net.minecraftborge.loader.Ingredient;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiWorkbench extends GuiContainer {
	private static final RenderItem itemRenderer = new RenderItem();

	private final World world;
	private final int x, y, z;
	private final EntityPlayer interact;
	private final ItemStack translateStack = new ItemStack(BlockInit.workbench, 1);
	private int selectedRecipeSlot = -1;
	private WorkbenchRecipe cachedSelectedRecipe = null;
	private final WorkbenchRecipe[] cachedDisplayRecipes = new WorkbenchRecipe[10];
	private int page = 0;
	private final Map<Integer, String> recipeSlotMapping = new HashMap<>();
	public GuiWorkbench(InventoryPlayer inventory, World world, int x, int y, int z) {
		super(new ContainerWorkbench(inventory, world, x, y, z));
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.interact = inventory.player;
	}

	@Override
	public void initGui() {
		super.initGui();
		int tier = this.world.getBlockMetadata(this.x, this.y, this.z);
		this.translateStack.setItemDamage(tier);
		this.selectedRecipeSlot = -1;
		this.cachedSelectedRecipe = null;
		this.page = 0;
		this.recipeSlotMapping.clear();

		Map<String, Integer> lookup = WorkbenchRecipes.crafting().getLookupMap();
		List<WorkbenchRecipe> list = WorkbenchRecipes.crafting().getRecipeList();
		int counter = 0;
		for (Map.Entry<String, Integer> entry : lookup.entrySet()) {
			String key = entry.getKey();
			int index = entry.getValue();
			WorkbenchRecipe recipe = list.get(index);
			if (tier < recipe.tier()) continue;
			this.recipeSlotMapping.put(counter++, key);
		}
		this.reindexCache();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick) {
		int textureID = this.mc.renderEngine.getTexture("assets/gui/nuclear_program/workbench.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureID);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.page == 0) {
			this.drawTexturedModalRect(x + 7, y + 24, 176, 0, 6, 36);
		}
		if ((this.page * 2) + 10 >= this.recipeSlotMapping.size()) {
			this.drawTexturedModalRect(x + 105, y + 24, 182, 0, 6, 36);
		}
		if (this.selectedRecipeSlot == -1) {
			this.drawTexturedModalRect(x + 50, y + 61, 188, 0, 18, 18);
			int i = this.selectedRecipeSlot - this.page * 2;
			if (i >= 0 && i < 10) {
				int slotX = 14 + (i / 2) * 18 + 1;
				int slotY = 24 + (i & 1) * 18 + 1;
				this.drawTexturedModalRect(x + slotX, y + slotY, 188, 18, 18, 18);
			}
		}

		StringTranslate translate = StringTranslate.getInstance();
		String title = translate.translateNamedKey(this.translateStack.getItemName());
		this.fontRenderer.drawString(title, x + (this.xSize / 2) - (this.fontRenderer.getStringWidth(title) / 2), y + 4, 0x404040);

		if (this.cachedSelectedRecipe != null) {
			String name = this.cachedSelectedRecipe.display().getItemName();
			this.fontRenderer.drawString(translate.translateNamedKey(name), x + 8, y + 14, 0xFFFFFF);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableStandardItemLighting();
		for (int i = 0; i < 10; i++) {
			WorkbenchRecipe recipe = this.cachedDisplayRecipes[i];
			if (recipe != null) {
				int slotX = 14 + (i / 2) * 18 + 1;
				int slotY = 24 + (i & 1) * 18 + 1;
				this.mc.renderEngine.bindTerrainTexture();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, recipe.display(), x + slotX, y + slotY);
			}
		}
		RenderHelper.disableStandardItemLighting();
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		if (this.cachedSelectedRecipe != null) {
			GL11.glPushMatrix();
			GL11.glScalef(0.5F, 0.5F, 1.0F);
			int x = 113 * 2;
			int y = 13 * 2;
			StringTranslate translate = StringTranslate.getInstance();
			int line = 0;
			Map<Ingredient, Integer> map = this.cachedSelectedRecipe.ingredients();
			for (Map.Entry<Ingredient, Integer> entry : map.entrySet()) {
				int amount = entry.getValue();
				if (amount == 0) continue;
				String name = amount + "x " + translate.translateNamedKey(this.getIngredientName(entry.getKey()));
				this.fontRenderer.drawString(name, x + 1, y + 1 + (line * 9), 0xFFFFFF);
				line++;
			}
			GL11.glPopMatrix();
		}
	}

	private int counter = 0;
	private String getIngredientName(Ingredient in) {
		List<ItemStack> display = in.getDisplayItems();
		if (display.isEmpty()) return "null";
		return display.get((this.counter / 20) % display.size()).getItemName();
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.counter++;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int type) {
		super.mouseClicked(mouseX, mouseY, type);
		int x = mouseX - (this.width - this.xSize) / 2;
		int y = mouseY - (this.height - this.ySize) / 2;
		if (type == 0) {
			if (x >= 50 && x < 50 + 18) {
				if (y >= 61 && y < 61 + 18) {
					if (this.selectedRecipeSlot != -1) {
						boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
						if (this.interact instanceof EntityClientPlayerMP) {
							((EntityClientPlayerMP)this.interact).sendQueue.addToSendQueue(NuclearProgramNetHandler.instance.createWorkbenchPacket(
									this.recipeSlotMapping.get(this.selectedRecipeSlot), shift
							));
						} else {
							this.cachedSelectedRecipe.process(this.interact, shift ? 64 : 1);
						}
						this.playClickSound();
					}
					return;
				}
			}
			for (int i = 0; i < 10; i++) {
				int slotX = 14 + (i / 2) * 18;
				int slotY = 24 + (i & 1) * 18;
				if (x >= slotX && x < slotX + 18) {
					if (y >= slotY && y < slotY + 18) {
						int recipeSlot = this.page * 2 + i;
						if (recipeSlot < this.recipeSlotMapping.size()) {
							this.selectedRecipeSlot = recipeSlot;
							this.cachedSelectedRecipe = WorkbenchRecipes.crafting().getRecipe(this.recipeSlotMapping.get(this.selectedRecipeSlot));
						}
						this.playClickSound();
						return;
					}
				}
			}
			if (y >= 24 && y < 24 + 36) {
				if (x >= 7 && x < 7 + 6) {
					if (this.page > 0) {
						this.page--;
						this.reindexCache();
						this.playClickSound();
					}
					return;
				}
				if (x >= 105 && x < 105 + 6) {
					if (this.page * 2 + 10 < this.recipeSlotMapping.size()) {
						this.page++;
						this.reindexCache();
						this.playClickSound();
					}
					return;
				}
			}
			this.doNothing();
		}
	}

	private void playClickSound() {
		this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	}

	private void reindexCache() {
		Arrays.fill(this.cachedDisplayRecipes, null);
		for (int i = 0; i < 10; i++) {
			if (this.page * 2 + i < this.recipeSlotMapping.size()) {
				this.cachedDisplayRecipes[i] = WorkbenchRecipes.crafting().getRecipe(this.recipeSlotMapping.get(this.page * 2 + i));
			}
		}
	}

	private void doNothing() {}
}
