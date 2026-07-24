package dev.siepert.nuclearprogram.util;

import java.text.DecimalFormat;

public class NumFormat {
	private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

	public static String format(int num) {
		if (num >= 1_000_000_000) {
			return FORMAT.format(num * 0.001 * 0.001 * 0.001) + "G";
		}
		if (num >= 1_000_000) {
			return FORMAT.format(num * 0.001 * 0.001) + "M";
		}
		if (num >= 1_000) {
			return FORMAT.format(num * 0.001) + "k";
		}
		return String.valueOf(num);
	}
	public static String format(long num) {
		if (num >= 1_000_000_000_000_000_000L) {
			return FORMAT.format(num * 0.001 * 0.001 * 0.001 * 0.001 * 0.001 * 0.001) + "E";
		}
		if (num >= 1_000_000_000_000_000L) {
			return FORMAT.format(num * 0.001 * 0.001 * 0.001 * 0.001 * 0.001) + "P";
		}
		if (num >= 1_000_000_000_000L) {
			return FORMAT.format(num * 0.001 * 0.001 * 0.001 * 0.001) + "T";
		}
		if (num >= 1_000_000_000) {
			return FORMAT.format(num * 0.001 * 0.001 * 0.001) + "G";
		}
		if (num >= 1_000_000) {
			return FORMAT.format(num * 0.001 * 0.001) + "M";
		}
		if (num >= 1_000) {
			return FORMAT.format(num * 0.001) + "k";
		}
		return String.valueOf(num);
	}
}
