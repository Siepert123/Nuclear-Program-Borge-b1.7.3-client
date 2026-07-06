package dev.siepert.nuclearprogram.recipe;

import dev.siepert.nuclearprogram.util.ChancedStack;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Ingredient;

import java.util.*;

public final class WorkbenchRecipe {
	private final ItemStack display;
	private final int tier;
	private final Map<Ingredient, Integer> ingredients;
	private final List<Ingredient> ingredientsList;
	private final List<ChancedStack> results;

	public WorkbenchRecipe(ItemStack display, int tier, Map<Ingredient, Integer> ingredients, List<ChancedStack> results) {
		this.display = display;
		this.tier = tier;
		this.ingredients = ingredients;
		this.results = results;
		ArrayList<Ingredient> list = new ArrayList<>();
		for (Map.Entry<Ingredient, Integer> entry : this.ingredients.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) list.add(entry.getKey());
		}
		list.trimToSize();
		this.ingredientsList = Collections.unmodifiableList(list);
	}

	public ItemStack display() {
		return this.display;
	}
	public int tier() {
		return this.tier;
	}
	public Map<Ingredient, Integer> ingredients() {
		return this.ingredients;
	}
	public List<ChancedStack> results() {
		return this.results;
	}

	public void process(EntityPlayer player, int max) {
		if (max <= 0) return;
		for (int i = 0; i < max && this.process(player); i++);
	}
	public boolean process(EntityPlayer player) {
		InventoryPlayer inventory = player.inventory;
		List<Ingredient> list = new ArrayList<>(this.ingredientsList);
		Iterator<Ingredient> iterator = list.iterator();

		ItemStack[] copy = new ItemStack[inventory.mainInventory.length];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = ItemStack.copyItemStack(inventory.mainInventory[i]);
		}

		while (iterator.hasNext()) {
			Ingredient in = iterator.next();

			for (int i = 0; i < copy.length; i++) {
				ItemStack stack = copy[i];
				if (stack == null || stack.stackSize == 0) continue;
				if (in.test(stack)) {
					stack.stackSize--;
					if (stack.stackSize == 0) copy[i] = null;
					iterator.remove();
					break;
				}
			}
		}

		if (list.isEmpty()) {
			list.addAll(this.ingredientsList);

			for (Ingredient in : list) {
				for (int i = 0; i < inventory.mainInventory.length; i++) {
					ItemStack stack = inventory.mainInventory[i];
					if (stack == null || stack.stackSize == 0) continue;
					if (in.test(stack)) {
						stack.stackSize--;
						if (stack.stackSize == 0) inventory.mainInventory[i] = null;
						break;
					}
				}
			}

			Random rnd = new Random();
			for (ChancedStack stack : this.results()) {
				if (stack.chance == 1.0F || rnd.nextFloat() < stack.chance) {
					ItemStack result = stack.stack.copy();
					inventory.addItemStackToInventory(result);
					result.onCrafting(player.worldObj, player);
				}
			}

			return true;
		}

		return false;
	}

	public static Builder builder() {
		return new Builder();
	}
	public static class Builder {
		private ItemStack result = null;
		private int tier = 0;
		private final Map<Ingredient, Integer> ingredients = new HashMap<>();

		private Builder() {}

		public Builder setResult(ItemStack stack) {
			this.result = stack;
			return this;
		}
		public Builder setTier(int tier) {
			this.tier = tier;
			return this;
		}
		public Builder addIngredient(Ingredient in, int count) {
			this.ingredients.put(in, count);
			return this;
		}
		public WorkbenchRecipe build() {
			if (this.result == null) throw new IllegalArgumentException("Recipe missing result");
			if (this.ingredients.isEmpty()) throw new IllegalArgumentException("Recipe missing ingredients");
			if (this.tier < 0) throw new IllegalArgumentException("Negative tier not allowed");
			return new WorkbenchRecipe(this.result, this.tier, this.ingredients, Collections.singletonList(new ChancedStack(this.result, 1.0F)));
		}
	}
}
