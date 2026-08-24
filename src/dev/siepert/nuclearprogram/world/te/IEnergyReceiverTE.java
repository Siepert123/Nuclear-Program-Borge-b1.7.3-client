package dev.siepert.nuclearprogram.world.te;

public interface IEnergyReceiverTE {
	long getEnergyCapacity();
	long getRemainingEnergyCapacity();
	long addEnergy(long amount);
	int getPriority();
}
