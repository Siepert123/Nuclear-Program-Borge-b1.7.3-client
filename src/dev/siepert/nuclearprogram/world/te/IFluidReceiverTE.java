package dev.siepert.nuclearprogram.world.te;

public interface IFluidReceiverTE {
	long getFluidCapacity(int fluidType, int bar);
	long getRemainingFluidCapacity(int fluidType, int bar);
	long addFluid(int fluidType, long amount, int bar);
	int getPriority();
}
