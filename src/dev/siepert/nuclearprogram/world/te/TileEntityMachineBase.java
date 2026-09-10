package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public abstract class TileEntityMachineBase extends TileEntity {
	public TileEntityMachineBase() {

	}

	public boolean collapsed = false;

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("collapsed", this.collapsed);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.collapsed = nbt.getBoolean("collapsed");
	}

	public double getMaxRenderDistanceSq() {
		return Double.POSITIVE_INFINITY;
	}
}
