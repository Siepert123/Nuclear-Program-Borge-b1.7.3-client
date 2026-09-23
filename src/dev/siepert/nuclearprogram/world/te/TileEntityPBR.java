package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.item.ItemFuelPebble;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.capability.IItemHandlerModifiable;
import net.minecraftborge.loader.tag.ItemTags;

public class TileEntityPBR extends TileEntityMachineBase implements IInventory, IFluidReceiverTE, IItemHandlerModifiable {
	public final ItemStack[] inventory = new ItemStack[4];
	public static final long CAPACITY = 16000L;
	public long tankCO2;
	public long tankCO2Hot;

	public TileEntityPBR() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.tankCO2Hot > 0L) {
				PipeNetNode node;
				for (EnumFacing side : EnumFacing.HORIZONTALS) {
					node = PipeNet.getNode(this.worldObj,
							this.xCoord + side.getOffsetX() * 2,
							this.yCoord,
							this.zCoord + side.getOffsetZ() * 2
					);
					if (node != null) {
						update = true;
						this.tankCO2Hot = node.pushFluid(FluidInit.carbonDioxideHot_Id, this.tankCO2Hot);
					}
					node = PipeNet.getNode(this.worldObj,
							this.xCoord + side.getOffsetX() * 2,
							this.yCoord + 2,
							this.zCoord + side.getOffsetZ() * 2
					);
					if (node != null) {
						update = true;
						this.tankCO2Hot = node.pushFluid(FluidInit.carbonDioxideHot_Id, this.tankCO2Hot);
					}

					if (this.tankCO2Hot == 0L) break;
				}
			}
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("tankCO2", this.tankCO2);
		nbt.setLong("tankCO2Hot", this.tankCO2Hot);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tankCO2 = nbt.getLong("tankCO2");
		this.tankCO2Hot = nbt.getInteger("tankCO2Hot");
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		if (fluidType != FluidInit.carbonDioxide_Id || bar != 1) return 0L;
		return CAPACITY;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		if (fluidType != FluidInit.carbonDioxide_Id || bar != 1) return 0L;
		return CAPACITY - this.tankCO2;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType != FluidInit.carbonDioxide_Id || bar != 1) return amount;
		this.onInventoryChanged();
		long remain = amount - (CAPACITY - this.tankCO2);
		if (remain <= 0L) {
			this.tankCO2 += amount;
			return 0L;
		} else {
			this.tankCO2 = CAPACITY;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.ENGINE_PRIORITY;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}
	@Override
	public int getSlots() {
		return this.getSizeInventory();
	}
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (ItemTags.isItemEmpty(stack) || slot != 2) return stack;
		if (this.inventory[slot] == null) {
			if (simulate) return null;
			this.inventory[slot] = stack.copy();
			this.onInventoryChanged();
			return null;
		} else if (this.inventory[slot].isItemEqual(stack)) {
			int transfer = Math.min(stack.stackSize, this.inventory[slot].getMaxStackSize() - this.inventory[slot].stackSize);
			if (transfer == 0) return stack;
			ItemStack ret = stack.copy();
			ret.stackSize -= transfer;
			if (simulate) return ret;
			this.inventory[slot].stackSize += transfer;
			this.onInventoryChanged();
			return ret;
		} else {
			return stack;
		}
	}
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount <= 0 || (slot != 1 && slot != 3)) return null;
		if (this.inventory[slot] == null) return null;
		if (simulate) {
			ItemStack ret = this.inventory[slot].copy();
			ret.stackSize = Math.min(ret.stackSize, amount);
			return ret;
		} else {
			return this.decrStackSize(slot, amount);
		}
	}
	@Override
	public int getMaxStackSize(int slot) {
		return 64;
	}
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		switch (slot) {
			case 0:
			case 1:
			case 3:
				return false;
			case 2:
				return stack.getItem() instanceof ItemFuelPebble;
		}
		return false;
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
			this.onInventoryChanged();
			return stack;
		} else {
			return null;
		}
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
		this.onInventoryChanged();
	}
	@Override
	public String getInvName() {
		return "Pebble-Bed Reactor";
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}
	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		this.setInventorySlotContents(slot, stack);
	}
}
