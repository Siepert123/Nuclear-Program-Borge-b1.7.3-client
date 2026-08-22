package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntityFluidPipe extends TileEntity implements IFilteredFluidConnection {
	public TileEntityFluidPipe() {

	}

	public int type = 0;
	@Override
	public int getFluidType() {
		return this.type;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("fluidID", (short) this.type);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.type = nbt.getShort("fluidID");
	}

	public boolean shouldTick() {
		return false;
	}
}
