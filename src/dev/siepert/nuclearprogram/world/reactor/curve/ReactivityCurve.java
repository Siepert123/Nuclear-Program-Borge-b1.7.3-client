package dev.siepert.nuclearprogram.world.reactor.curve;

public interface ReactivityCurve {
	double getFluxOut(double fluxIn);
	String getDisplaySpecification();
}
