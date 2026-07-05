package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.src.*;
import net.minecraftborge.loader.IFurnace;
import net.minecraftborge.loader.ITickingTile;

import java.util.Arrays;

public class TileEntityBloomery extends TileEntity implements IInventory, IFurnace, ITickingTile {
	private final ItemStack[] inventory = new ItemStack[4];

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		if (this.inventory[slot] != null) {
			ItemStack stack;
			if (this.inventory[slot].stackSize <= count) {
				stack = this.inventory[slot];
				this.inventory[slot] = null;
			} else {
				stack = this.inventory[slot].splitStack(count);
				if (this.inventory[slot].stackSize == 0) {
					this.inventory[slot] = null;
				}
			}
			return stack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
		if (stack != null && stack.stackSize > Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit())) {
			stack.stackSize = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
		}
	}

	@Override
	public String getInvName() {
		return "Bloomery";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	private boolean fx = true;
	@Override
	public void updateEntity() {
		if (this.getBlockType() == BlockInit.bloomeryLit && (this.fx = !this.fx)) {
			this.worldObj.spawnParticle("nuclear_program/pollution",
					this.xCoord + 0.5, this.yCoord + 2.875, this.zCoord + 0.5,
					0.25, 0.0, 0.0
			);
		}
	}

	public int getBurnTime(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(this, stack);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		Arrays.fill(this.inventory, null);
		NBTTagList items = nbt.getTagList("Inventory");
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound compound = (NBTTagCompound) items.tagAt(i);
			byte slot = compound.getByte("slot");
			if (slot >= 0 && slot < this.getSizeInventory()) {
				this.inventory[slot] = new ItemStack(compound);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); i++) {
			if (this.inventory[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("slot", (byte) i);
				this.inventory[i].writeToNBT(compound);
				items.setTag(compound);
			}
		}
		nbt.setTag("Inventory", items);
	}

	@Override
	public World getFurnaceWorld() {
		return this.worldObj;
	}
	@Override
	public int getFurnaceX() {
		return this.xCoord;
	}
	@Override
	public int getFurnaceY() {
		return this.yCoord;
	}
	@Override
	public int getFurnaceZ() {
		return this.zCoord;
	}
}
