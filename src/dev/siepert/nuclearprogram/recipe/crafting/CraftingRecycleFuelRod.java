package dev.siepert.nuclearprogram.recipe.crafting;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.IModRecipe;
import net.minecraftborge.loader.Ingredient;
import net.minecraftborge.loader.event.register.AddRecipesEvent;

public class CraftingRecycleFuelRod implements IModRecipe {
	public final Ingredient in;
	public final ItemStack out;
	public final ItemStack container;

	public CraftingRecycleFuelRod(Ingredient in, ItemStack out, ItemStack container) {
		this.in = in;
		this.out = out;
		this.container = container;
	}

	@Override
	public boolean matches(InventoryCrafting grid) {
		boolean flag = false;
		for (int i = 0; i < grid.getSizeInventory(); i++) {
			ItemStack stack = grid.getStackInSlot(i);
			if (stack != null) {
				if (flag) return false;
				if (!this.in.test(stack)) return false;
				flag = true;
			}
		}
		return flag;
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {
		return this.out.copy();
	}
	@Override
	public int getRecipeSize() {
		return 1;
	}
	@Override
	public ItemStack getRecipeOutput() {
		return this.out;
	}

	@Override
	public void onRecipePerformed(EntityPlayer crafter) {
		if (crafter != null) {
			crafter.inventory.addItemStackToInventory(this.container.copy());
		}
	}

	public static class Helper {
		private final AddRecipesEvent event;

		public Helper(AddRecipesEvent event) {
			this.event = event;
		}

		public void add(Ingredient in, ItemStack out, ItemStack container) {
			this.event.addRecipe(new CraftingRecycleFuelRod(in, out, container));
		}
	}
}
