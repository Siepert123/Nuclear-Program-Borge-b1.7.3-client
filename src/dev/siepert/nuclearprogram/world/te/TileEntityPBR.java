package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.NBTTagCompound;

public class TileEntityPBR extends TileEntityMachineBase implements IFluidReceiverTE {
	public TileEntityPBR() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {

		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		return amount;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.ENGINE_PRIORITY;
	}
}
