package dev.siepert.nuclearprogram.world.fluid;

public class FluidTraitHeatable {
	public final int heatsTo;
	public final int thermalCapacity;

	public FluidTraitHeatable(int heatsTo, int thermalCapacity) {
		this.heatsTo = heatsTo;
		this.thermalCapacity = thermalCapacity;
	}
}
