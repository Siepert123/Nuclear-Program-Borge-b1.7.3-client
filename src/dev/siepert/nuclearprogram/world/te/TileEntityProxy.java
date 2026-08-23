package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public abstract class TileEntityProxy extends TileEntity {
	public static final IFluidReceiverTE DUMMY_FLUID_RECEIVER = new IFluidReceiverTE() {
		@Override
		public long getCapacity(int fluidType, int bar) {
			return 0;
		}
		@Override
		public long getRemainingCapacity(int fluidType, int bar) {
			return 0;
		}
		@Override
		public long addFluid(int fluidType, long amount, int bar) {
			return amount;
		}
		@Override
		public int getPriority() {
			return Integer.MAX_VALUE;
		}
	};
	public static TileEntityProxy create(boolean fluid, boolean energy) {
		if (fluid) {
			return energy ? new Proxy11() : new Proxy10();
		} else {
			return energy ? new Proxy01() : new Proxy00();
		}
	}

	protected int[] core = null;
	protected int[] getCorePos() {
		if (this.core == null) {
			this.core = new int[]{this.xCoord, this.yCoord, this.zCoord};
			((BlockMulti)this.getBlockType()).findCore(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.core);
		}
		return this.core;
	}
	protected TileEntity coreTE = null;
	protected TileEntity getCore() {
		if (this.coreTE == null || this.coreTE.isInvalid()) {
			int[] pos = this.getCorePos();
			this.coreTE = this.worldObj.getBlockTileEntity(pos[0], pos[1], pos[2]);
		}
		return this.coreTE;
	}
	protected IFluidReceiverTE getFluidReceiverTE() {
		TileEntity te = this.getCore();
		return te != null ? (IFluidReceiverTE) te : DUMMY_FLUID_RECEIVER;
	}

	private TileEntityProxy() {}

	public static final class Proxy00 extends TileEntityProxy {
		public Proxy00() {}
	}
	public static final class Proxy01 extends TileEntityProxy {
		public Proxy01() {}
	}
	public static final class Proxy10 extends TileEntityProxy implements IFluidReceiverTE {
		public Proxy10() {}

		@Override
		public long getCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getCapacity(fluidType, bar);
		}
		@Override
		public long getRemainingCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getRemainingCapacity(fluidType, bar);
		}
		@Override
		public long addFluid(int fluidType, long amount, int bar) {
			return this.getFluidReceiverTE().addFluid(fluidType, amount, bar);
		}
		@Override
		public int getPriority() {
			return this.getFluidReceiverTE().getPriority();
		}
	}
	public static final class Proxy11 extends TileEntityProxy implements IFluidReceiverTE {
		public Proxy11() {}

		@Override
		public long getCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getCapacity(fluidType, bar);
		}
		@Override
		public long getRemainingCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getRemainingCapacity(fluidType, bar);
		}
		@Override
		public long addFluid(int fluidType, long amount, int bar) {
			return this.getFluidReceiverTE().addFluid(fluidType, amount, bar);
		}
		@Override
		public int getPriority() {
			return this.getFluidReceiverTE().getPriority();
		}
	}
}
