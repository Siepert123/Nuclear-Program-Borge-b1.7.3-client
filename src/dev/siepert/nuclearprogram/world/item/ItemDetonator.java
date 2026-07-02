package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.IDetonateBehaviour;
import net.minecraft.src.*;

public class ItemDetonator extends Item {
	public ItemDetonator(int itemID) {
		super(itemID);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side) {
		if (!player.isSneaking() || stack == null) return false;
		NBTTagCompound nbt = stack.itemNBT != null ? stack.itemNBT : new NBTTagCompound(3);
		nbt.setInteger("targetX", x);
		nbt.setInteger("targetY", y);
		nbt.setInteger("targetZ", z);
		nbt.setBoolean("hasTarget", true);
		stack.itemNBT = nbt;
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (stack != null && stack.itemNBT != null && stack.itemNBT.getBoolean("hasTarget")) {
			final int x = stack.itemNBT.getInteger("targetX");
			final int y = stack.itemNBT.getInteger("targetY");
			final int z = stack.itemNBT.getInteger("targetZ");
			final Block block = Block.blocksList[world.getBlockId(x, y, z)];
			final IDetonateBehaviour.Callback callback;
			if (block instanceof IDetonateBehaviour) {
				callback = ((IDetonateBehaviour)block).detonate(world, x, y, z);
			} else callback = IDetonateBehaviour.Callback.INVALID;
			final String status;
			switch (callback) {
				case SUCCESS:
					status = "Detonated successfully!";
					break;
				case INVALID:
					status = "Target is invalid or unloaded!";
					break;
				case MISSING_COMPONENTS:
					status = "Target is missing components!";
					break;
				default:
					status = "Unknown detonation error!";
					break;
			}
			player.addChatMessage("[DETONATOR] " + status);
			ItemStack ret = stack.copy();
			ret.itemNBT = stack.itemNBT;
			return ret;
		}
		return stack;
	}
}
