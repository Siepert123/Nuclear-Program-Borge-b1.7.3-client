package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;

public class ItemFuelRod extends Item {
	public static final String[] VARIANTS = {
			"NaturalUranium",
			"NaturalThorium",
	};

	public static final int NATURAL_URANIUM = 0;
	public static final int NATURAL_THORIUM = 1;

	public final Icon[] itemTextures = new Icon[VARIANTS.length];
	public final int variants = VARIANTS.length;

	public ItemFuelRod(int itemID) {
		super(itemID);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);

		if (this == ItemInit.fuelRod) this.setContainerItem(ItemInit.fuelRodEmpty);
		if (this == ItemInit.fuelRodArray) this.setContainerItem(ItemInit.fuelRodArrayEmpty);
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
		items.add(new ItemStack(this, 1, 2137));
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
}
