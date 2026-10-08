package dev.siepert.nuclearprogram.util;

import java.text.DecimalFormat;

public class NumFormat {
	private static final DecimalFormat SI_FORMAT = new DecimalFormat("#.#");
	private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.##");
	static {
		SI_FORMAT.setMinimumFractionDigits(1);
		SI_FORMAT.setMaximumFractionDigits(3);
	}

	public static String format(int num) {
		if (num >= 1_000_000_000) {
			return SI_FORMAT.format(num * 0.001 * 0.001 * 0.001) + "G";
		}
		if (num >= 1_000_000) {
			return SI_FORMAT.format(num * 0.001 * 0.001) + "M";
		}
		if (num >= 1_000) {
			return SI_FORMAT.format(num * 0.001) + "k";
		}
		return String.valueOf(num);
	}
	public static String format(long num) {
		if (num >= 1_000_000_000_000_000_000L) {
			return SI_FORMAT.format(num * 0.001 * 0.001 * 0.001 * 0.001 * 0.001 * 0.001) + "E";
		}
		if (num >= 1_000_000_000_000_000L) {
			return SI_FORMAT.format(num * 0.001 * 0.001 * 0.001 * 0.001 * 0.001) + "P";
		}
		if (num >= 1_000_000_000_000L) {
			return SI_FORMAT.format(num * 0.001 * 0.001 * 0.001 * 0.001) + "T";
		}
		if (num >= 1_000_000_000) {
			return SI_FORMAT.format(num * 0.001 * 0.001 * 0.001) + "G";
		}
		if (num >= 1_000_000) {
			return SI_FORMAT.format(num * 0.001 * 0.001) + "M";
		}
		if (num >= 1_000) {
			return SI_FORMAT.format(num * 0.001) + "k";
		}
		return String.valueOf(num);
	}

	public static String percentage(long value, long max) {
		return PERCENT_FORMAT.format((value * 100.0) / max) + "%";
	}
}
