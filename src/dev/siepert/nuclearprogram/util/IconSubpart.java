package dev.siepert.nuclearprogram.util;

import net.minecraftborge.loader.Icon;

public class IconSubpart implements Icon {
	protected final Icon source;
	protected final double minU, maxU, minV, maxV;

	public IconSubpart(Icon source, double minU, double minV, double maxU, double maxV) {
		this.source = source;
		this.minU = minU;
		this.maxU = maxU;
		this.minV = minV;
		this.maxV = maxV;
	}

	@Override
	public double getU(double lerp) {
		return this.source.getU(this.minU + (this.maxU - this.minU) * lerp);
	}
	@Override
	public double getV(double lerp) {
		return this.source.getV(this.minV + (this.maxV - this.minV) * lerp);
	}

	public static Icon create(Icon source, double srcW, double srcH, double x, double y, double w, double h) {
		return new IconSubpart(source, x / srcW, y / srcH, (x+w) / srcW, (y+h) / srcH);
	}
}
