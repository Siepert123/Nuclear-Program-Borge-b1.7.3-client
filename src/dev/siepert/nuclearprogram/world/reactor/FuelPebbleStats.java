package dev.siepert.nuclearprogram.world.reactor;

import dev.siepert.nuclearprogram.world.reactor.curve.ReactivityCurve;

public class FuelPebbleStats {
	public final float heatPerFlux;
	public final ReactivityCurve reactivity;
	public final int lifetime;

	public FuelPebbleStats(float heatPerFlux, ReactivityCurve reactivity, int lifetime) {
		this.heatPerFlux = heatPerFlux;
		this.reactivity = reactivity;
		this.lifetime = lifetime;
	}
}
