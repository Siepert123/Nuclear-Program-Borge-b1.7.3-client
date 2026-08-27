package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.capability.Capability;

public abstract class TileEntityProxy extends TileEntity {
	public static final IFluidReceiverTE DUMMY_FLUID_RECEIVER = new IFluidReceiverTE() {
		@Override
		public long getFluidCapacity(int fluidType, int bar) {
			return 0;
		}
		@Override
		public long getRemainingFluidCapacity(int fluidType, int bar) {
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
	public static final IEnergyReceiverTE DUMMY_ENERGY_RECEIVER = new IEnergyReceiverTE() {
		@Override
		public long getEnergyCapacity() {
			return 0;
		}
		@Override
		public long getRemainingEnergyCapacity() {
			return 0;
		}
		@Override
		public long addEnergy(long amount) {
			return amount;
		}
		@Override
		public int getPriority() {
			return Integer.MAX_VALUE;
		}
	};

	public static final int VOID_PRIORITY = 1000;
	public static final int BUFFER_PRIORITY = 100;
	public static final int ENGINE_PRIORITY = 10;
	public static final int MACHINE_PRIORITY = 0;


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
	protected IEnergyReceiverTE getEnergyReceiverTE() {
		TileEntity te = this.getCore();
		return te != null ? (IEnergyReceiverTE) te : DUMMY_ENERGY_RECEIVER;
	}

	private TileEntityProxy() {}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing context) {
		return this.getCore().hasCapability(capability, context);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing context) {
		return this.getCore().getCapability(capability, context);
	}

	public static final class Proxy00 extends TileEntityProxy {
		public Proxy00() {}
	}
	public static final class Proxy01 extends TileEntityProxy implements IEnergyReceiverTE {
		public Proxy01() {}

		@Override
		public long getEnergyCapacity() {
			return this.getEnergyReceiverTE().getEnergyCapacity();
		}
		@Override
		public long getRemainingEnergyCapacity() {
			return this.getEnergyReceiverTE().getRemainingEnergyCapacity();
		}
		@Override
		public long addEnergy(long amount) {
			return this.getEnergyReceiverTE().addEnergy(amount);
		}
		@Override
		public int getPriority() {
			return this.getEnergyReceiverTE().getPriority();
		}
	}
	public static final class Proxy10 extends TileEntityProxy implements IFluidReceiverTE {
		public Proxy10() {}

		@Override
		public long getFluidCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getFluidCapacity(fluidType, bar);
		}
		@Override
		public long getRemainingFluidCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getRemainingFluidCapacity(fluidType, bar);
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
	public static final class Proxy11 extends TileEntityProxy implements IFluidReceiverTE, IEnergyReceiverTE {
		public Proxy11() {}

		@Override
		public long getFluidCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getFluidCapacity(fluidType, bar);
		}
		@Override
		public long getRemainingFluidCapacity(int fluidType, int bar) {
			return this.getFluidReceiverTE().getRemainingFluidCapacity(fluidType, bar);
		}
		@Override
		public long addFluid(int fluidType, long amount, int bar) {
			return this.getFluidReceiverTE().addFluid(fluidType, amount, bar);
		}
		@Override
		public long getEnergyCapacity() {
			return this.getEnergyReceiverTE().getEnergyCapacity();
		}
		@Override
		public long getRemainingEnergyCapacity() {
			return this.getEnergyReceiverTE().getRemainingEnergyCapacity();
		}
		@Override
		public long addEnergy(long amount) {
			return this.getEnergyReceiverTE().addEnergy(amount);
		}
		@Override
		public int getPriority() {
			return this.getEnergyReceiverTE().getPriority();
		}
	}
}
