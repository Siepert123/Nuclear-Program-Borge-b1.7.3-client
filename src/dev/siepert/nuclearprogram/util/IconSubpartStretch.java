package dev.siepert.nuclearprogram.util;

import net.minecraftborge.loader.Icon;

public class IconSubpartStretch extends IconSubpart {
	public IconSubpartStretch(Icon source, double minU, double minV, double maxU, double maxV) {
		super(source, minU, minV, maxU, maxV);
	}

	@Override
	public double getU(double lerp) {
		return super.getU(lerp < 0.0 ? 0.0 : Math.min(lerp, 1.0));
	}

	@Override
	public double getV(double lerp) {
		return super.getV(lerp < 0.0 ? 0.0 : Math.min(lerp, 1.0));
	}

	public static Icon create(Icon source, double srcW, double srcH, double x, double y, double w, double h) {
		return new IconSubpartStretch(source, x / srcW, y / srcH, (x+w) / srcW, (y+h) / srcH);
	}
}
