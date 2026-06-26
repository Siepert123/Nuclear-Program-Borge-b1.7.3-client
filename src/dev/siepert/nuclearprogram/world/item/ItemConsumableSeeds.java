package dev.siepert.nuclearprogram.world.item;

import net.minecraft.src.*;
import net.minecraftborge.loader.Side;

public class ItemConsumableSeeds extends ItemFood {
	private final int blockID;

	public ItemConsumableSeeds(int itemID, int blockID, int healFactor, boolean wolfFood) {
		super(itemID, healFactor, wolfFood);
		this.blockID = blockID;
		this.setMaxStackSize(64);
	}

	@Override
	public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
		if (var7 != Side.UP) return false;
		int placeOn = var3.getBlockId(var4, var5, var6);
		if (placeOn == Block.tilledField.blockID && var3.isAirBlock(var4, var5+1, var6)) {
			var3.setBlockWithNotify(var4, var5+1, var6, this.blockID);
			--var1.stackSize;
			return true;
		} else return false;
	}
}
