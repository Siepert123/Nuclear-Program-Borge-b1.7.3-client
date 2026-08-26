package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.TileEntity;

public abstract class TileEntityMachineBase extends TileEntity {
	public TileEntityMachineBase() {

	}

	public double getMaxRenderDistanceSq() {
		return Double.POSITIVE_INFINITY;
	}
}
