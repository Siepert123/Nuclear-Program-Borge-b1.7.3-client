package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.TileEntity;

public class TileEntityOilDistilleryController extends TileEntity implements IEnergyReceiverTE, IFluidReceiverTE {
	public TileEntityOilDistilleryController() {

	}

	@Override
	public long getEnergyCapacity() {
		return 0;
	}
	@Override
	public long getRemainingEnergyCapacity() {
		return 0;
	}
	@Override
	public long addEnergy(long amount) {
		return 0;
	}
	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return 0;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return 0;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		return 0;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.MACHINE_PRIORITY;
	}
}
