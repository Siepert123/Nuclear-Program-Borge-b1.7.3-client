package dev.siepert.nuclearprogram.world.reactor;

import dev.siepert.nuclearprogram.world.reactor.curve.ReactivityCurve;

public class FuelPebbleStats {
	public final float heatPerFlux;
	public final ReactivityCurve reactivity;

	public FuelPebbleStats(float heatPerFlux, ReactivityCurve reactivity) {
		this.heatPerFlux = heatPerFlux;
		this.reactivity = reactivity;
	}
}
