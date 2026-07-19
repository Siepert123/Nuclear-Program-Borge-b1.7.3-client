package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;

public class TileEntityFluidPipeCoated extends TileEntityFluidPipe {
	public TileEntityFluidPipeCoated() {

	}

	public int modelBlockID = 0;

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("modelBlockID", (short) this.modelBlockID);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.modelBlockID = nbt.getShort("modelBlockID") & 4095;
		if (Block.blocksList[this.modelBlockID] == null) {
			this.modelBlockID = 0;
		}
	}
}
