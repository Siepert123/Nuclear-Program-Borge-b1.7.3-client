package dev.siepert.nuclearprogram.util;

import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.Side;

public class MultiblockHelper {
	private static final int[] rot = new int[6];
	public static boolean isSpaceUnoccupied(World world, int x, int y, int z, int[] dims, EnumFacing facing) {
		rotate(dims, facing, rot);

		for (int a = x - rot[4]; a <= x + rot[5]; a++) {
			for (int b = y - rot[1]; b <= y + rot[0]; b++) {
				for (int c = z - rot[2]; c <= z + rot[3]; c++) {
					if (!world.getBlockMaterial(a, b, c).isReplaceable()) {
						System.out.println("Collision at " + a + ", " + b + ", " + c);
						return false;
					}
				}
			}
		}

		return true;
	}

	public static void fill(World world, int x, int y, int z, int[] dims, int blockID, EnumFacing facing) {
		rotate(dims, facing, rot);

		BlockMulti.keepInventory = true;

		for (int a = x - rot[4]; a <= x + rot[5]; a++) {
			for (int b = y - rot[1]; b <= y + rot[0]; b++) {
				for (int c = z - rot[2]; c <= z + rot[3]; c++) {
					int meta;

					if (b < y) {
						meta = Side.NEG_Y;
					} else if (b > y) {
						meta = Side.POS_Y;
					} else if (a < x) {
						meta = Side.NEG_X;
					} else if (a > x) {
						meta = Side.POS_X;
					} else if (c < z) {
						meta = Side.NEG_Z;
					} else if (c > z) {
						meta = Side.POS_Z;
					} else {
						continue;
					}

					world.setBlockAndMetadataWithNotify(a, b, c, blockID, meta);
				}
			}
		}

		BlockMulti.keepInventory = false;
	}

	public static void rotate(int[] dims, EnumFacing facing, int[] dimsRot) {
		switch (facing) {
			case WEST:
				dimsRot[0] = dims[0];
				dimsRot[1] = dims[1];
				dimsRot[2] = dims[2];
				dimsRot[3] = dims[3];
				dimsRot[4] = dims[4];
				dimsRot[5] = dims[5];
				break;
			case EAST:
				dimsRot[0] = dims[0];
				dimsRot[1] = dims[1];
				dimsRot[2] = dims[3];
				dimsRot[3] = dims[2];
				dimsRot[4] = dims[5];
				dimsRot[5] = dims[4];
				break;
			case SOUTH:
				dimsRot[0] = dims[0];
				dimsRot[1] = dims[1];
				dimsRot[2] = dims[5];
				dimsRot[3] = dims[4];
				dimsRot[4] = dims[2];
				dimsRot[5] = dims[3];
				break;
			case NORTH:
				dimsRot[0] = dims[0];
				dimsRot[1] = dims[1];
				dimsRot[2] = dims[4];
				dimsRot[3] = dims[5];
				dimsRot[4] = dims[3];
				dimsRot[5] = dims[2];
				break;
		}
	}
}
