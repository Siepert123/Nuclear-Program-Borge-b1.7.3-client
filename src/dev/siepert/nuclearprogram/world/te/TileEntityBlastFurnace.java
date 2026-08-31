package dev.siepert.nuclearprogram.world.te;

public class TileEntityBlastFurnace extends TileEntityMachineBase implements IFluidReceiverTE {
	public TileEntityBlastFurnace() {

	}

	@Override
	public void updateEntity() {
		this.worldObj.spawnParticle("nuclear_program/pollution",
				this.xCoord + 0.5, this.yCoord + 7.0, this.zCoord + 0.5,
				0.0, 0.0, 0.0
		);
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
		return TileEntityProxy.MACHINE_PRIORITY;
	}
}
