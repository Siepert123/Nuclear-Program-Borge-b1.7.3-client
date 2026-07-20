package dev.siepert.nuclearprogram.world.item;

import net.minecraft.src.Item;
import net.minecraftborge.loader.IconRegister;

public class ItemFuelRodRBMK extends Item {
	public ItemFuelRodRBMK(int itemID) {
		super(itemID);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.itemTexture = register.getTexture(this.getSimpleName(), 32, 32);
	}
}
