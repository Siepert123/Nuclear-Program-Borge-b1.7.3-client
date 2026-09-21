package dev.siepert.nuclearprogram.world.reactor.curve;

public class ReactivityCurveLog10 implements ReactivityCurve {
	private final float scalar;
	private final String specification;

	public ReactivityCurveLog10(float scalar) {
		this.scalar = scalar;
		this.specification = "y = log10(x + 1) * " + this.scalar;
	}

	@Override
	public double getFluxOut(double fluxIn) {
		return Math.log10(fluxIn + 1.0) * this.scalar;
	}
	@Override
	public String getDisplaySpecification() {
		return this.specification;
	}
}
