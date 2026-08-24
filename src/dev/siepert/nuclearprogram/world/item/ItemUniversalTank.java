package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.fluid.FluidContainers;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;
import java.util.List;

public class ItemUniversalTank extends Item {
	public Icon itemTextureOverlay;

	public final long fluidCapacity;
	public ItemUniversalTank(int itemID, long fluidCapacity) {
		super(itemID);
		this.fluidCapacity = fluidCapacity;

		FluidContainers.isUniversalTank[itemID] = true;
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.itemTexture = register.getTexture(this.getSimpleName(), 16, 16);
		this.itemTextureOverlay = register.getTexture(this.getSimpleName() + "_overlay", 16, 16);
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		for (int i = 0; i < Fluid.ID_SIZE; i++) {
			if (Fluid.hasFluidIdentifier[i]) items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public void getTooltip(ItemStack stack, List<String> tooltip, boolean isAdvanced) {
		Fluid fluid = Fluid.fluidsList[stack.getItemDamage()];
		if (fluid != null) {
			tooltip.add("Fluid: " + StringTranslate.getInstance().translateNamedKey(fluid.getUnlocalizedName()));
		}
	}
}
