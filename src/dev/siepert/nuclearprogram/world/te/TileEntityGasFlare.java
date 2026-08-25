package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import net.minecraft.src.TileEntity;

public class TileEntityGasFlare extends TileEntity implements IFluidReceiverTE {
	public static final long LIMITER = 1000L;
	public long limiter = LIMITER;
	public int animation = 0;

	public TileEntityGasFlare() {

	}

	@Override
	public void updateEntity() {
		if (this.limiter < LIMITER) {
			this.limiter = LIMITER;
			this.animation = 10;
		}
		if (this.animation > 0) {
			this.animation--;
			this.effects();
		}
	}
	private void effects() {
		this.worldObj.spawnParticle("nuclear_program/pollution",
				this.xCoord + 0.5, this.yCoord + 11.0, this.zCoord + 0.5,
				0.0, 0.0, 0.0
		);
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return fluidType == FluidInit.naturalGas.fluidID ? LIMITER : 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return fluidType == FluidInit.naturalGas.fluidID ? this.limiter : 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType != FluidInit.naturalGas.fluidID || this.limiter == 0L) return amount;
		long remain = amount - Math.min(amount, this.limiter);
		this.limiter -= remain;
		return remain;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.VOID_PRIORITY;
	}
}
