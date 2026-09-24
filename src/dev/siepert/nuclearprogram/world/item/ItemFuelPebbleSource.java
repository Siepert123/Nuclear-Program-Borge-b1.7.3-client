package dev.siepert.nuclearprogram.world.item;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;
import java.util.List;

public class ItemFuelPebbleSource extends Item {
	public Icon itemTextureDepleted;

	public final int flux;
	public final int lifetime;

	public ItemFuelPebbleSource(int itemID, int flux, int lifetime) {
		super(itemID);
		this.flux = flux;
		this.lifetime = lifetime;
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return super.getItemNameIS(stack) + (stack.getItemDamage() != 0 ? "_depleted" : "");
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.itemTexture = register.getTexture(this.getSimpleName(), 16, 16);
		this.itemTextureDepleted = register.getTexture(this.getSimpleName() + "_depleted", 16, 16);
	}
	@Override
	public Icon getTextureFromDamage(int damage) {
		return damage != 0 ? this.itemTextureDepleted : this.itemTexture;
	}

	@Override
	public void getTooltip(ItemStack stack, List<String> tooltip, boolean isAdvanced) {
		if (stack.getItemDamage() == 0) {
			tooltip.add("Adds " + this.flux + " base flux to the reactor");
		}
	}
}
