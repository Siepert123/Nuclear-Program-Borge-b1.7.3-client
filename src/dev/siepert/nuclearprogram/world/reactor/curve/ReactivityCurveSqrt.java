package dev.siepert.nuclearprogram.world.reactor.curve;

public class ReactivityCurveSqrt implements ReactivityCurve {
	private final float scalar1;
	private final float scalar2;
	private final String specification;

	public ReactivityCurveSqrt(float scalar1, float scalar2) {
		this.scalar1 = scalar1;
		this.scalar2 = scalar2;
		this.specification = "y = sqrt(x * " + this.scalar1 + ") * " + this.scalar2;
	}

	@Override
	public double getFluxOut(double fluxIn) {
		return Math.sqrt(fluxIn * this.scalar1) * this.scalar2;
	}
	@Override
	public String getDisplaySpecification() {
		return this.specification;
	}
}
