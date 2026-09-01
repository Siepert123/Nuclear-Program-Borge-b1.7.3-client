package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.capability.Capability;
import net.minecraftborge.loader.capability.CapabilityItemHandler;
import net.minecraftborge.loader.capability.IItemHandler;
import net.minecraftborge.loader.capability.IItemHandlerModifiable;

public class TileEntityCokeOven extends TileEntityMachineBase implements IItemHandlerModifiable {
	public final ItemStack[] inventory = new ItemStack[2];
	public static final long TANK_CAPACITY = 16000L;
	public long tankCreosote = 0L;
	public int progress = 0;

	public boolean wasOpen = false;
	public boolean isOpen = false;

	public TileEntityCokeOven() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		this.wasOpen = this.isOpen;
		if (!this.worldObj.multiplayerWorld) {
			if (this.progress > 0) {
				if (this.tankCreosote < TANK_CAPACITY && (this.inventory[1] == null || this.inventory[1].stackSize < 64)) {
					update = true;
					this.isOpen = true;
					this.progress++;
					this.tankCreosote++;
					if (this.progress > 200) {
						this.progress = 0;
						if (this.inventory[1] != null) this.inventory[1].stackSize++;
						else this.inventory[1] = new ItemStack(ItemInit.cokeCoal, 1);
					}
				} else this.isOpen = false;
			} else {
				if (this.inventory[0] != null && (this.inventory[1] == null || this.inventory[1].stackSize < 64)) {
					if (this.inventory[0].itemID == Item.coal.shiftedIndex) {
						update = true;
						this.isOpen = true;
						this.progress = 1;
						if (--this.inventory[0].stackSize <= 0) this.inventory[0] = null;
					} else this.isOpen = false;
				} else {
					this.isOpen = false;
				}
			}
			update |= this.transferCokes();
		}

		if (update) this.onInventoryChanged();
	}

	private boolean transferCokes() {
		boolean update = false;
		EnumFacing movement = this.rotate(EnumFacing.VALUES[this.getBlockMetadata() - BlockMulti.OFFSET]);
		if (this.inventory[1] != null) {
			TileEntity to = this.worldObj.getBlockTileEntity(this.xCoord + movement.getOffsetX(), this.yCoord, this.zCoord + movement.getOffsetZ());
			if (to != null && to.hasCapability(CapabilityItemHandler.CAPABILITY, movement.getOpposite())) {
				IItemHandler inv = to.getCapability(CapabilityItemHandler.CAPABILITY, movement.getOpposite());
				this.inventory[1] = inv.insertItem(1, this.inventory[1], false);
				update = true;
			}
		}
		if (this.inventory[0] == null || this.inventory[0].stackSize < 64) {
			TileEntity from = this.worldObj.getBlockTileEntity(this.xCoord - movement.getOffsetX(), this.yCoord, this.zCoord - movement.getOffsetZ());
			if (from != null && from.hasCapability(CapabilityItemHandler.CAPABILITY, movement)) {
				IItemHandler inv = from.getCapability(CapabilityItemHandler.CAPABILITY, movement);
				if (this.inventory[0] == null) {
					this.inventory[0] = inv.extractItem(0, 64, false);
				} else {
					ItemStack ret = inv.extractItem(0, 64, false);
					if (ret != null) this.inventory[0].stackSize += ret.stackSize;
				}
				update = true;
			}
		}
		return update;
	}
	private EnumFacing rotate(EnumFacing side) {
		switch (side) {
			case NORTH: return EnumFacing.EAST;
			case EAST: return EnumFacing.SOUTH;
			case SOUTH: return EnumFacing.WEST;
			case WEST: return EnumFacing.NORTH;
			default: return side;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("tankCreosote", this.tankCreosote);
		nbt.setInteger("progress", this.progress);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tankCreosote = nbt.getLong("tankCreosote");
		this.progress = nbt.getInteger("progress");
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
	}
	@Override
	public int getSlots() {
		return 2;
	}
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!this.isItemValid(slot, stack)) return stack;
		int space = this.inventory[slot] == null ? 64 : 64 - this.inventory[slot].stackSize;
		if (space <= 0) return stack;
		if (stack.stackSize <= space) {
			if (!simulate) {
				this.onInventoryChanged();
				if (this.inventory[slot] == null) {
					this.inventory[slot] = stack.copy();
				} else this.inventory[slot].stackSize += stack.stackSize;
			}
			return null;
		} else {
			if (!simulate) {
				this.onInventoryChanged();
				if (this.inventory[slot] == null) {
					this.inventory[slot] = stack.copy();
				} else this.inventory[slot].stackSize = 64;
			}
			ItemStack ret = stack.copy();
			ret.stackSize -= space;
			return ret;
		}
	}
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot != 1 || amount <= 0 || this.inventory[1] == null) return null;
		if (simulate) return new ItemStack(this.inventory[1].itemID, Math.min(amount, this.inventory[1].stackSize), this.inventory[1].getItemDamage());
		if (amount >= this.inventory[1].stackSize) {
			ItemStack ret = this.inventory[1].copy();
			this.inventory[1] = null;
			this.onInventoryChanged();
			return ret;
		} else {
			this.inventory[1].stackSize -= amount;
			this.onInventoryChanged();
			return new ItemStack(this.inventory[1].itemID, amount, this.inventory[1].getItemDamage());
		}
	}
	@Override
	public int getMaxStackSize(int slot) {
		return 64;
	}
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return slot == 0 && (stack != null && stack.itemID == Item.coal.shiftedIndex) || slot == 1 && (stack != null && stack.itemID == ItemInit.cokeCoal.shiftedIndex);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing context) {
		if (this.rotate(EnumFacing.VALUES[this.getBlockMetadata() - BlockMulti.OFFSET]).getOpposite() == context) {
			if (capability == CapabilityItemHandler.CAPABILITY) return true;
		}
		return super.hasCapability(capability, context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing context) {
		if (this.rotate(EnumFacing.VALUES[this.getBlockMetadata() - BlockMulti.OFFSET]).getOpposite() == context) {
			if (capability == CapabilityItemHandler.CAPABILITY) return (T) this;
		}
		return super.getCapability(capability, context);
	}
}
