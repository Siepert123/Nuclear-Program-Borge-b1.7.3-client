package dev.siepert.nuclearprogram.world.item;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;

public class ItemFuelRod extends Item {
	public static final String[] VARIANTS = {
			"NaturalUranium",
			"Thorium232",
			"Lead",
			"UraniumLE",
			"UraniumME",
			"UraniumHE",
	};

	public static final int NATURAL_URANIUM = 0;
	public static final int THORIUM_232 = 1;
	public static final int LEAD = 2;
	public static final int LOW_ENRICHED_URANIUM = 3;
	public static final int MEDIUM_ENRICHED_URANIUM = 4;
	public static final int HIGH_ENRICHED_URANIUM = 5;

	public final Icon[] itemTextures = new Icon[VARIANTS.length];
	public final int variants = VARIANTS.length;

	public ItemFuelRod(int itemID) {
		super(itemID);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < this.variants; i++) {
			this.itemTextures[i] = register.getTexture(this.getSimpleName() + VARIANTS[i], 16, 16);
		}

		super.registerIcons(register);
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		for (int i = 0; i < this.variants; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public Icon getTextureFromDamage(int damage) {
		if (damage < 0 || damage >= this.variants) return this.itemTexture;
		return this.itemTextures[damage];
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		int metadata = stack.getItemDamage();
		if (metadata < 0 || metadata >= this.variants) return this.getItemName();
		else return this.getItemName() + VARIANTS[metadata];
	}

	public ItemStack create(int type) {
		return new ItemStack(this, 1, type);
	}
}
