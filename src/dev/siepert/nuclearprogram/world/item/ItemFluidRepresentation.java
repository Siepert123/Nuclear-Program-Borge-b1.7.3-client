package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.ChatFormat;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;
import java.util.List;

public class ItemFluidRepresentation extends Item {
	public ItemFluidRepresentation(int itemID) {
		super(itemID);
		this.setMaxStackSize(1);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.itemTexture = register.getTexture(NuclearProgram.path("droplet"), 16, 16);
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		for (int i = 1; i < Fluid.ID_SIZE; i++) {
			if (Fluid.fluidsList[i] != null) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public String getItemNameIS(ItemStack stack) {
		return Fluid.getUnlocalizedName(Fluid.fluidsList[stack.getItemDamage()]);
	}

	@Override
	public int getColorFromDamage(int damage) {
		return Fluid.colorLookup[damage];
	}

	@Override
	public void getTooltip(ItemStack stack, List<String> tooltip, boolean isAdvanced) {
		NBTTagCompound props = stack.itemNBT;
		if (props != null) {
			if (props.hasKey("fluidAmount", NBTBase.LONG)) {
				tooltip.add(props.getLong("fluidAmount") + " mB");
			}
			if (props.hasKey("fluidPressure", NBTBase.BYTE)) {
				tooltip.add(props.getByte("fluidPressure") + " bar");
			}
		}
		int fluidID = stack.getItemDamage();
		if (fluidID > 0) {
			tooltip.add(String.format(ChatFormat.RED + "%sºC", Fluid.temperatureLookup[fluidID]));
		}
	}
}
