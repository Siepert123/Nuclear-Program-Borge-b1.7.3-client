package dev.siepert.nuclearprogram.world.te;

public interface IFluidReceiverTE {
	long getCapacity(int fluidType, int bar);
	long getRemainingCapacity(int fluidType, int bar);
	long addFluid(int fluidType, long amount, int bar);
	int getPriority();
}
