package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.capability.Capability;
import net.minecraftborge.loader.capability.CapabilityItemHandler;
import net.minecraftborge.loader.capability.IItemHandler;
import net.minecraftborge.loader.capability.IItemHandlerModifiable;

import java.util.Arrays;

public class TileEntityCokeOven extends TileEntityMachineBase implements IInventory, IItemHandlerModifiable {
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

					if (this.worldObj.rand.nextInt(64) == 0) {
						this.worldObj.playSoundEffect(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, "fire.fire", 1.0F, 0.7F);
					}

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
						this.tankCreosote++;
						if (--this.inventory[0].stackSize <= 0) this.inventory[0] = null;
					} else this.isOpen = false;
				} else {
					this.isOpen = false;
				}
			}
			update |= this.transferCokes();

			if (this.tankCreosote > 0L) {
				EnumFacing facing = EnumFacing.VALUES[this.getBlockMetadata() - BlockMulti.OFFSET].getOpposite();
				PipeNetNode node = PipeNet.getNode(this.worldObj, this.xCoord + facing.getOffsetX() * 2, this.yCoord, this.zCoord + facing.getOffsetZ() * 2);
				if (node != null) {
					long rem = node.pushFluid(FluidInit.creosote.fluidID, this.tankCreosote);
					if (rem != this.tankCreosote) {
						update = true;
						this.tankCreosote = rem;
					}
				}
			}
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
				for (int i = 0; i < inv.getSlots() && this.inventory[1] != null; i++) {
					this.inventory[1] = inv.insertItem(i, this.inventory[1], false);
				}
				update = true;
			}
		}
		if (this.inventory[0] == null) {
			TileEntity from = this.worldObj.getBlockTileEntity(this.xCoord - movement.getOffsetX(), this.yCoord, this.zCoord - movement.getOffsetZ());
			if (from instanceof TileEntityCokeOven) {
				TileEntityCokeOven oven = (TileEntityCokeOven) from;
				if (oven.inventory[0] != null) {
					update = true;
					this.inventory[0] = oven.inventory[0];
					oven.inventory[0] = null;
					oven.onInventoryChanged();
				}
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
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tankCreosote = nbt.getLong("tankCreosote");
		this.progress = nbt.getInteger("progress");

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
	public void setStackInSlot(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
	}
	@Override
	public int getSlots() {
		return 2;
	}

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
	}

	@Override
	public String getInvName() {
		return "Coke Oven";
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
