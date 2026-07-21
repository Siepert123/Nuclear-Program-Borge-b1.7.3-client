package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.AchievementInit;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotFurnaceBuilder extends Slot {
	private final EntityPlayer player;
	public SlotFurnaceBuilder(EntityPlayer player, IInventory inventory, int id, int x, int y) {
		super(inventory, id, x, y);
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public void onPickupFromSlot(ItemStack stack) {
		stack.onCrafting(this.player.worldObj, this.player, NuclearProgram.path("FurnaceBuilder"));
		this.player.triggerAchievement(AchievementInit.instance.furnaceBuilder);
		super.onPickupFromSlot(stack);
	}
}
