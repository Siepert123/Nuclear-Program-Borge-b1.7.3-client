package dev.siepert.nuclearprogram.world.item;

import net.minecraft.src.Entity;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

import java.util.List;

public class ItemWhitePhosphorus extends Item {
	public ItemWhitePhosphorus(int itemID) {
		super(itemID);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean inHand) {
		if (inHand) {
			entity.fire = 20;
		}
	}

	@Override
	public void getTooltip(ItemStack stack, List<String> tooltip, boolean isAdvanced) {
		//tooltip.add("¬ impossible to put out");
		//tooltip.add("¬ highly toxic");
		//tooltip.add("¬ wounds not stitchable");
	}
}
