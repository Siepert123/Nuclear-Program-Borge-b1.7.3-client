package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.*;

public class TileEntityGasCentrifuge extends TileEntity implements IInventory {
	public static final String WORKSTATION = NuclearProgram.path("GasCentrifuge");
	public static final int MAX_ENERGY_STORED = 10_000;
	public static final int TANK_CAPACITY = 8000;
	public static final int TICKS_PER_CENTRIFUGE = 200;

	public TileEntityGasCentrifuge() {

	}

	public final ItemStack[] inventory = new ItemStack[4];
	public int fluidToProcess = 0;
	public int fluidProcessed = 0;
	public int progress = 0;
	public int energy = 0;
	public Enrichment setting = Enrichment.NATURAL;
	public boolean push = true;

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			this.fluidToProcess = Math.min(this.fluidToProcess + 50, TANK_CAPACITY);

			if (this.fluidProcessed + 800 >= TANK_CAPACITY) {
				this.progress = 0;
			} else if (this.fluidToProcess >= 1000) {
				update = true;
				if (++this.progress >= TICKS_PER_CENTRIFUGE) {
					this.progress = 0;
					this.fluidToProcess -= 1000;
					if (this.push) this.fluidProcessed += 800;
					else {
						if (this.inventory[0] == null) {
							switch (this.setting) {
								case LOW:
									this.inventory[0] = new ItemStack(ItemInit.ingotUraniumLE);
									break;
								case MEDIUM:
									this.inventory[0] = new ItemStack(ItemInit.ingotUraniumME);
									break;
								case HIGH:
									this.inventory[0] = new ItemStack(ItemInit.ingotUraniumHE);
									break;
							}
						} else this.inventory[0].stackSize++;
					}
				}
			}
		}

		if (update) this.onInventoryChanged();
	}

	public int getFluidToProcessScaled(int h) {
		return (this.fluidToProcess * h / (TANK_CAPACITY+1))+1;
	}
	public int getFluidProcessedScaled(int h) {
		return (this.fluidProcessed * h / (TANK_CAPACITY+1))+1;
	}

	public void setEnrichment(Enrichment setting) {
		this.setting = setting;
		if (setting == Enrichment.HIGH) this.push = false;
		if (setting == Enrichment.NATURAL) this.push = true;
	}
	public void setPush(boolean push) {
		this.push = push && this.setting != Enrichment.HIGH || this.setting == Enrichment.NATURAL;
	}

	public int getFluidIn() {
		return this.setting.fluidID;
	}
	public int getFluidOut() {
		return this.push ? this.setting.fluidID + 1 : 0;
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
		return "Gas Centrifuge";
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
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setInteger("fluidIn", this.fluidToProcess);
		nbt.setInteger("fluidOut", this.fluidProcessed);
		nbt.setInteger("energy", this.energy);
		nbt.setString("setting", this.setting.name());
		nbt.setBoolean("push", this.push);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		this.fluidToProcess = nbt.getInteger("fluidIn");
		this.fluidProcessed = nbt.getInteger("fluidOut");
		this.energy = nbt.getInteger("energy");
		this.setting = Enrichment.valueOf(nbt.getString("setting"));
		this.push = nbt.getBoolean("push") && this.setting != Enrichment.HIGH;
	}

	public enum Enrichment {
		NATURAL(FluidInit.uraniumHexafluoride.fluidID),
		LOW(FluidInit.uraniumHexafluorideLE.fluidID),
		MEDIUM(FluidInit.uraniumHexafluorideME.fluidID),
		HIGH(FluidInit.uraniumHexafluorideHE.fluidID);

		public final int fluidID;
		Enrichment(int fluidID) {
			this.fluidID = fluidID;
		}
	}
}
