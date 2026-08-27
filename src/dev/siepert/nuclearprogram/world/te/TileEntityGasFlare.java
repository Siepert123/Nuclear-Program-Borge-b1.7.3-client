package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import net.minecraft.src.NBTTagCompound;

public class TileEntityGasFlare extends TileEntityMachineBase implements IFluidReceiverTE {
	public static final long FLARE_RATE = 50L;
	public int animation = 0;
	public int fluidType = FluidInit.naturalGas.fluidID;

	public static final long TANK_CAPACITY = FLARE_RATE * 20L; // Stores a second of burn time
	public long tank = 0L;

	public TileEntityGasFlare() {

	}

	@Override
	public void updateEntity() {
		if (this.tank > 0L) {
			this.tank = Math.max(0L, this.tank - FLARE_RATE);
			this.animation = 10;
		}
		if (this.animation > 0) {
			this.animation--;
			this.effects();
		}
	}
	private void effects() {
		if ((this.worldObj.getWorldTime() & 1) == 0) {
			this.worldObj.spawnParticle("nuclear_program/flame",
					this.xCoord + 0.5, this.yCoord + 11.0, this.zCoord + 0.5,
					0.0, 1.0, 0.0
			);
		}
	}

	public boolean setFlaredGas(int fluidType, boolean client) {
		if (fluidType == this.fluidType) return true;
		if (fluidType == FluidInit.naturalGas.fluidID) {
			if (!client) {
				this.setFluidType(fluidType);
			}
			return true;
		}
		if (fluidType == FluidInit.petroleumGas.fluidID) {
			if (!client) {
				this.setFluidType(fluidType);
			}
			return true;
		}
		if (fluidType == FluidInit.ethane.fluidID) {
			if (!client) {
				this.setFluidType(fluidType);
			}
			return true;
		}
		if (fluidType == FluidInit.propane.fluidID) {
			if (!client) {
				this.setFluidType(fluidType);
			}
			return true;
		}
		return false;
	}
	private void setFluidType(int fluidType) {
		this.fluidType = fluidType;
		this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("fluidType", this.fluidType);
		nbt.setLong("tank", this.tank);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.fluidType = nbt.getInteger("fluidType");
		this.tank = nbt.getLong("tank");
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return fluidType == this.fluidType ? TANK_CAPACITY : 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return fluidType == this.fluidType ? TANK_CAPACITY - this.tank : 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType != this.fluidType || this.tank == TANK_CAPACITY || amount <= 0) return amount;
		this.onInventoryChanged();
		long remain = amount - (TANK_CAPACITY - this.tank);
		if (remain <= 0) {
			this.tank += amount;
			return 0L;
		} else {
			this.tank = TANK_CAPACITY;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.VOID_PRIORITY;
	}
}
