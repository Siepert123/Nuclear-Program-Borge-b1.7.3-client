package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public class TileEntityGasCentrifuge extends TileEntity implements IInventory {
	public static final int MAX_ENERGY_STORED = 10_000;

	public TileEntityGasCentrifuge() {

	}

	public final ItemStack[] inventory = new ItemStack[4];
	public int energy = 0;
	public Enrichment setting = Enrichment.NATURAL;
	public boolean push = false;

	@Override
	public void updateEntity() {
		super.updateEntity();
	}

	public void setEnrichment(Enrichment setting) {
		this.setting = setting;
		if (setting == Enrichment.HIGH) this.push = false;
	}
	public void setPush(boolean push) {
		this.push = push && this.setting != Enrichment.HIGH;
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
