package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.reactor.FuelPebbleStats;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;
import java.util.List;

public class ItemFuelPebble extends Item {
	public Icon itemTextureDepleted;

	public final FuelPebbleStats fuelStats;

	public ItemFuelPebble(int itemID, FuelPebbleStats fuelStats) {
		super(itemID);
		this.fuelStats = fuelStats;
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
	public void getTooltip(ItemStack stack, List<String> tooltip, boolean isAdvanced) {
		if (stack.getItemDamage() == 0) {
			tooltip.add("Reactivity curve:");
			tooltip.add("  " + this.fuelStats.reactivity.getDisplaySpecification());
			tooltip.add("Heat per flux: " + this.fuelStats.heatPerFlux + "TF");
		}
	}
}
