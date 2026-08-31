package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class TileEntityBlastFurnace extends TileEntityMachineBase implements IInventory, IFluidReceiverTE {
	public static final String WORKSTATION = "BlastFurnace";

	private final ItemStack[] inventory = new ItemStack[4];

	public TileEntityBlastFurnace() {

	}

	@Override
	public void updateEntity() {
		this.worldObj.spawnParticle("nuclear_program/pollution",
				this.xCoord + 0.5, this.yCoord + 7.0, this.zCoord + 0.5,
				0.0, 0.0, 0.0
		);
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
			long remain = amount - 1000L;
			if (remain <= 0L) {
				return 0L;
			} else {
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
