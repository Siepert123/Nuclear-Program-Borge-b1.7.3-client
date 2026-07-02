package dev.siepert.nuclearprogram.util;

import java.util.Random;

public class RandomTextures {
	public static int[] generateIndexArray(Long seed, int count) {
		Random rnd = seed != null ? new Random(seed) : new Random();
		int[] array = new int[16*16*16];
		for (int i = 0; i < array.length; i++) {
			array[i] = rnd.nextInt(count);
		}
		return array;
	}
	public static int getIndex(int x, int y, int z) {
		return (x & 15) | ((z & 15) << 4) | ((y & 15) << 8);
	}
}
