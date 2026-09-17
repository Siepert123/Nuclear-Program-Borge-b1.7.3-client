package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.Nothing;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.util.NPMth;
import net.minecraft.src.*;

import java.util.Arrays;

public class TileEntityBlastFurnace extends TileEntityMachineBase implements IInventory, IFluidReceiverTE {
	public static final String WORKSTATION = "BlastFurnace";

	private final ItemStack[] inventory = new ItemStack[4];

	public static final int cokesMax = 32;
	public int cokes = 0;
	public static final int MAX_PROGRESS = 200;
	public int progress = 0;
	public static final long MAX_HEATING = 1600L;
	public long heating = 0L;

	public TileEntityBlastFurnace() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (this.inventory[0] != null && this.cokes + 2 <= cokesMax) {
			if (this.inventory[0].itemID == ItemInit.cokeCoal.shiftedIndex || this.inventory[0].itemID == ItemInit.cokePetroleum.shiftedIndex) {
				update = true;
				if (--this.inventory[0].stackSize == 0) this.inventory[0] = null;
				this.cokes += 2;
			}
		}

		if (this.heating > 0L) {
			update = true;
			recipe:
			if (this.heating >= 100L) {
				int speed = NPMth.log2((int) (this.heating / 100L)) + 1;
				if (this.cokes > 0 && this.inventory[1] != null) {
					if (this.inventory[1].itemID == Item.ingotIron.shiftedIndex) {
						if (this.hasOutputCapacity(1, 0)) {
							this.progress += speed;
							if (this.progress > MAX_PROGRESS) {
								this.cokes--;
								this.progress = 0;
								this.appendOutputs(true, 1, 0);
							}
							this.effects(4);
							break recipe;
						}
					}
					if (this.inventory[1].itemID == Block.oreIron.blockID) {
						if (this.hasOutputCapacity(2, 1)) {
							this.progress += speed;
							if (this.progress > MAX_PROGRESS) {
								this.cokes--;
								this.progress = 0;
								this.appendOutputs(true, 2, 1);
							}
							this.effects(2);
							break recipe;
						}
					}
				}
				if (this.progress > 0) this.progress--;
			}
			this.heating = 0L;
		}

		if (update) this.onInventoryChanged();
	}

	private boolean hasOutputCapacity(int steel, int slag) {
		if (this.inventory[2] != null && this.inventory[2].stackSize + steel > 64) return false;
		return this.inventory[3] == null || this.inventory[3].stackSize + slag <= 64;
	}
	private void appendOutputs(boolean take, int steel, int slag) {
		if (take) {
			if (--this.inventory[1].stackSize <= 0) this.inventory[1] = null;
		}

		if (steel > 0) {
			if (this.inventory[2] == null) this.inventory[2] = new ItemStack(ItemInit.ingotSteel, steel);
			else this.inventory[2].stackSize += steel;
		}

		if (slag > 0) {
			if (this.inventory[3] == null) this.inventory[3] = new ItemStack(Block.gravel, slag);
			else this.inventory[3].stackSize += slag;
		}
	}
	private void effects(int chance) {
		if (this.worldObj.rand.nextInt(chance) == 0) {
			this.worldObj.spawnParticle("nuclear_program/pollution",
					this.xCoord + 0.5, this.yCoord + 7.0, this.zCoord + 0.5,
					0.0, 0.0, 0.0
			);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("cokes", this.cokes);
		nbt.setInteger("progress", this.progress);
		nbt.setLong("heating", this.heating);

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
		this.cokes = nbt.getInteger("cokes");
		this.progress = nbt.getInteger("progress");
		this.heating = nbt.getLong("heating");

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

	public int getCokesScaled(int scale) {
		return (this.cokes * scale / (cokesMax+1))+1;
	}
	public int getProgressScaled(int scale) {
		return (this.progress * scale / (MAX_PROGRESS+1))+1;
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return fluidType == FluidInit.airBlast.fluidID && bar == 1 ? 1000L : 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return fluidType == FluidInit.airBlast.fluidID && bar == 1 ? 1000L : 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType == FluidInit.airBlast.fluidID && bar == 1) {
			this.onInventoryChanged();
			long remain = amount - (MAX_HEATING - this.heating);
			if (remain <= 0L) {
				this.heating += amount;
				return 0L;
			} else {
				this.heating = MAX_HEATING;
				return remain;
			}
		} else return amount;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.MACHINE_PRIORITY;
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
		return "Blast Furnace";
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}
}
