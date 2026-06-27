package dev.siepert.nuclearprogram.util;

import net.minecraftborge.loader.BorgeMath;

@FunctionalInterface
public interface IEasing {
	/**
	 * @param x The absolute progress defined by [0,1]
	 * @return The eased version of the progress
	 */
	float ease(float x);

	default float clampedEase(float x) {
		return BorgeMath.clamp(this.ease(x), 0.0F, 1.0F);
	}
	default float easedLerp(float delta, float start, float end) {
		return BorgeMath.lerp(this.ease(delta), start, end);
	}
	default float clampedEasedLerp(float a, float b, float x) {
		return BorgeMath.clampedLerp(a, b, this.ease(x));
	}
}
