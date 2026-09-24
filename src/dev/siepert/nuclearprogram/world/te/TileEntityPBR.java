package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.item.ItemFuelPebble;
import dev.siepert.nuclearprogram.world.item.ItemFuelPebbleSource;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.GameRegistries;
import net.minecraftborge.loader.capability.IItemHandlerModifiable;
import net.minecraftborge.loader.tag.ItemTags;

import java.util.Arrays;

public class TileEntityPBR extends TileEntityMachineBase implements IInventory, IFluidReceiverTE, IItemHandlerModifiable {
	public final ItemStack[] inventory = new ItemStack[4];
	public static final long CAPACITY = 16000L;
	public long tankCO2;
	public long tankCO2Hot;

	public ItemFuelPebbleSource pebbleSource;
	public int sourceYield;

	public ItemFuelPebble pebbleFuel;
	public int fuelCount;
	public double fuelYield;

	public int heat = 0;
	public double flux = 0.0;

	public TileEntityPBR() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			// Push out hot coolant
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

			// Insert pebbles
			if (this.inventory[0] != null) {
				if (this.pebbleSource == null) {
					if (this.inventory[0].getItem() instanceof ItemFuelPebbleSource) {
						if (this.inventory[1] == null || this.inventory[1].itemID == this.inventory[0].itemID) {
							this.pebbleSource = (ItemFuelPebbleSource) this.inventory[0].getItem();
							if (--this.inventory[0].stackSize <= 0) this.inventory[0] = null;
						}
					}
				}
			}
			if (this.inventory[2] != null) {
				if (this.fuelCount < 16 && this.inventory[2].getItem() instanceof ItemFuelPebble) {
					if (this.pebbleFuel == null || this.pebbleFuel.shiftedIndex == this.inventory[2].itemID) {
						if (this.inventory[3] == null || this.inventory[3].itemID == this.inventory[2].itemID) {
							this.pebbleFuel = (ItemFuelPebble) this.inventory[2].getItem();
							this.fuelCount++;
							if (--this.inventory[2].stackSize <= 0) this.inventory[2] = null;
						}
					}
				}
			}

			// Fuel reacting
			if (this.pebbleFuel != null) {
				double bonus = this.pebbleSource != null ? this.pebbleSource.flux : 0.0;
				this.flux = this.pebbleFuel.fuelStats.reactivity.getFluxOut(this.flux + bonus) * this.fuelCount;
				this.fuelYield += this.flux;
				this.heat += (int) Math.round(this.pebbleFuel.fuelStats.heatPerFlux * this.flux);
				if (this.fuelYield > this.pebbleFuel.fuelStats.lifetime) {
					if (this.inventory[3] == null) this.inventory[3] = new ItemStack(this.pebbleFuel, 1, 1);
					else this.inventory[3].stackSize++;
					if (--this.fuelCount == 0) this.pebbleFuel = null;
					this.fuelYield = 0.0;
				}
			} else {
				this.flux = 0.0;
			}

			if (this.pebbleSource != null) {
				if (++this.sourceYield > this.pebbleSource.lifetime) {
					if (this.inventory[1] == null) this.inventory[1] = new ItemStack(this.pebbleSource, 1, 1);
					else this.inventory[1].stackSize++;
					this.pebbleSource = null;
					this.sourceYield = 0;
				}
			}

			// Cool down core
			if (this.heat >= 200) {
				int transfer = Math.min(Math.min(Math.toIntExact(this.tankCO2), Math.toIntExact(CAPACITY - this.tankCO2Hot)), this.heat / 200);
				if (transfer > 0) {
					update = true;
					this.tankCO2 -= transfer;
					this.tankCO2Hot += transfer;
					this.heat -= transfer * 100;
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

		if (this.pebbleSource != null) {
			nbt.setString("pebbleSource", GameRegistries.ITEMS.getKey(this.pebbleSource));
			nbt.setInteger("sourceYield", this.sourceYield);
		}
		if (this.pebbleFuel != null) {
			nbt.setString("pebbleFuel", GameRegistries.ITEMS.getKey(this.pebbleFuel));
			nbt.setInteger("fuelCount", this.fuelCount);
			nbt.setDouble("fuelYield", this.fuelYield);
		}

		nbt.setInteger("heat", this.heat);
		nbt.setDouble("flux", this.flux);

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
		this.tankCO2 = nbt.getLong("tankCO2");
		this.tankCO2Hot = nbt.getInteger("tankCO2Hot");

		if (nbt.hasKey("pebbleSource")) {
			this.pebbleSource = (ItemFuelPebbleSource) GameRegistries.ITEMS.getValue(nbt.getString("pebbleSource"));
			this.sourceYield = nbt.getInteger("sourceYield");
		}
		if (nbt.hasKey("pebbleFuel")) {
			this.pebbleFuel = (ItemFuelPebble) GameRegistries.ITEMS.getValue(nbt.getString("pebbleFuel"));
			this.fuelCount = nbt.getInteger("fuelCount");
			this.fuelYield = nbt.getDouble("fuelYield");
		}

		this.heat = nbt.getInteger("heat");
		this.flux = nbt.getDouble("flux");

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

	public int getTankCO2Scaled(int h) {
		return Math.toIntExact((this.tankCO2 * h / (CAPACITY+1))+1);
	}
	public int getTankCO2HotScaled(int h) {
		return Math.toIntExact((this.tankCO2Hot * h / (CAPACITY+1))+1);
	}
	public int getSourceDepletionScaled(int h) {
		return h;
	}
	public int getFuelDepletionScaled(int h) {
		return h;
	}
	public double getCoreFlux() {
		return this.flux + (this.pebbleSource != null ? this.pebbleSource.flux : 0.0);
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
			case 1:
			case 3:
				return false;
			case 0:
				return stack.getItem() instanceof ItemFuelPebbleSource;
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
		return BlockInit.pebbleBedReactor.translateBlockName();
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
