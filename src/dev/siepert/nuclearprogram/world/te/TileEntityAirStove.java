package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;

public class TileEntityAirStove extends TileEntityMachineBase implements IFluidReceiverTE {
	public int fluidType = FluidInit.creosote.fluidID;
	public static final long TANK_CAPACITY_FUEL = 8000L;
	public static final long TANK_CAPACITY_AIR = 64000L;
	public long tankHeatSource = 0L;
	public long tankAirIn = 0L;
	public long tankAirOut = 0L;

	public TileEntityAirStove() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.tankAirIn >= 100L && (TANK_CAPACITY_AIR - this.tankAirOut) >= 100L && this.tankHeatSource > 0L) {
				update = true;
				this.tankHeatSource--;
				this.tankAirIn -= 100L;
				this.tankAirOut += 100L;
			}
		}

		if (update) this.onInventoryChanged();
	}

	public void setFuelType(int fluidID) {
		if (this.worldObj.multiplayerWorld) {
		} else if (fluidID == FluidInit.naturalGas.fluidID) {
			this.fluidType = fluidID;
			this.onInventoryChanged();
		} else if (fluidID == FluidInit.creosote.fluidID) {
			this.fluidType = fluidID;
			this.onInventoryChanged();
		}
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		if (bar != 1) return 0L;
		if (fluidType == this.fluidType) return TANK_CAPACITY_FUEL;
		if (fluidType == FluidInit.air.fluidID) return TANK_CAPACITY_AIR;
		return 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		if (bar != 1) return 0L;
		if (fluidType == this.fluidType) return TANK_CAPACITY_FUEL - this.tankHeatSource;
		if (fluidType == FluidInit.air.fluidID) return TANK_CAPACITY_AIR - this.tankAirIn;
		return 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (bar != 1) return amount;
		if (fluidType == this.fluidType) {
			this.onInventoryChanged();
			long remain = amount - (TANK_CAPACITY_FUEL - this.tankHeatSource);
			if (remain <= 0L) {
				this.tankHeatSource += amount;
				return 0L;
			} else {
				this.tankHeatSource = TANK_CAPACITY_FUEL;
				return amount - remain;
			}
		}
		if (fluidType == FluidInit.air.fluidID) {
			this.onInventoryChanged();
			long remain = amount - (TANK_CAPACITY_AIR - this.tankAirIn);
			if (remain <= 0L) {
				this.tankAirIn += amount;
				return 0L;
			} else {
				this.tankAirIn = TANK_CAPACITY_AIR;
				return amount - remain;
			}
		}
		return amount;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.ENGINE_PRIORITY;
	}
}
