package dev.siepert.nuclearprogram.util;

public class NPMth {
	/**
	 * Though it looks like an array, this is really more like a mapping. Key (index of this array) is the upper 5 bits of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531. Value (value stored in the array) is the unique index (from the right) of the leftmo
	 */
	private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{
			0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
	};

	/**
	 * {@return the input value rounded up to the next highest power of two}
	 */
	public static int smallestEncompassingPowerOfTwo(int value) {
		int i = value - 1;
		i |= i >> 1;
		i |= i >> 2;
		i |= i >> 4;
		i |= i >> 8;
		i |= i >> 16;
		return i + 1;
	}

	/**
	 * Is the given value a power of two?  (1, 2, 4, 8, 16, ...)
	 */
	public static boolean isPowerOfTwo(int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	/**
	 * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two, then subtract 1 from the return value.
	 */
	public static int ceillog2(int value) {
		value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
		return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)value * 125613361L >> 27) & 31];
	}

	/**
	 * Efficiently calculates the floor of the base-2 log of an integer value.  This is effectively the index of the highest bit that is set.  For example, if the number in binary is 0...100101, this will return 5.
	 */
	public static int log2(int value) {
		return ceillog2(value) - (isPowerOfTwo(value) ? 0 : 1);
	}

	public static boolean blink() {
		return (System.currentTimeMillis() % 1000) < 500;
	}
}
