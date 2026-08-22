package dev.siepert.nuclearprogram.util;

import net.minecraft.src.ChunkCoordinates;

import java.util.ArrayList;
import java.util.List;

public class BlockPos {
	private static final int PACKED_X_LENGTH = 1 + NPMth.log2(NPMth.smallestEncompassingPowerOfTwo(30000000));
	private static final int PACKED_Z_LENGTH = PACKED_X_LENGTH;
	public static final int PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
	private static final long PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
	private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
	private static final long PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
	private static final int Y_OFFSET = 0;
	private static final int Z_OFFSET = PACKED_Y_LENGTH;
	private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;

	public static int getX(long packedPos) {
		return (int)(packedPos << 64 - X_OFFSET - PACKED_X_LENGTH >> 64 - PACKED_X_LENGTH);
	}

	public static int getY(long packedPos) {
		return (int)(packedPos << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH);
	}

	public static int getZ(long packedPos) {
		return (int)(packedPos << 64 - Z_OFFSET - PACKED_Z_LENGTH >> 64 - PACKED_Z_LENGTH);
	}

	public static long pack(int x, int y, int z) {
		long i = 0L;
		i |= ((long)x & PACKED_X_MASK) << X_OFFSET;
		i |= ((long)y & PACKED_Y_MASK) << Y_OFFSET;
		return i | ((long)z & PACKED_Z_MASK) << Z_OFFSET;
	}

	private static final List<ChunkCoordinates> pool = new ArrayList<>();
	private static int poolIndex = 0;

	public static ChunkCoordinates pooled(int x, int y, int z) {
		if (pool.size() == poolIndex) pool.add(new ChunkCoordinates());
		ChunkCoordinates pos = pool.get(poolIndex++);
		pos.x = x;
		pos.y = y;
		pos.z = z;
		return pos;
	}
	public static void resetPool() {
		poolIndex = 0;
	}
	public static void drainPool() {
		resetPool();
		pool.clear();
	}
}
