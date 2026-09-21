package dev.siepert.nuclearprogram.world.reactor.curve;

public class ReactivityCurveLinear implements ReactivityCurve {
	private final float scalar;
	private final String specification;

	public ReactivityCurveLinear(float scalar) {
		this.scalar = scalar;
		this.specification = "y = x * " + this.scalar;
	}

	@Override
	public double getFluxOut(double fluxIn) {
		return fluxIn * this.scalar;
	}
	@Override
	public String getDisplaySpecification() {
		return this.specification;
	}
}
