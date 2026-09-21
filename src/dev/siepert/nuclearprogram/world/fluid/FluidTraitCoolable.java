package dev.siepert.nuclearprogram.world.fluid;

public class FluidTraitCoolable {
	public final int coolsTo;
	public final int thermalCapacity;

	public FluidTraitCoolable(int coolsTo, int thermalCapacity) {
		this.coolsTo = coolsTo;
		this.thermalCapacity = thermalCapacity;
	}
}
