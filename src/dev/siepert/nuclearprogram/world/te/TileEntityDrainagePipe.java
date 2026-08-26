package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityDrainagePipe extends TileEntity implements IFluidReceiverTE {
	public static final long DRAIN_RATE = 50L;
	public int animation = 0;
	public int fluidType = FluidInit.heavyOil.fluidID;

	public static final long TANK_CAPACITY = DRAIN_RATE * 20L; // Stores a second of drain time
	public long tank = 0L;

	public TileEntityDrainagePipe() {

	}

	@Override
	public void updateEntity() {
		if (this.tank > 0L) {
			this.tank = Math.max(0L, this.tank - DRAIN_RATE);
			this.animation = 10;
		}
		if (this.animation > 0) {
			this.animation--;
			this.effects();
		}
	}
	private void effects() {
		if ((this.worldObj.getWorldTime() & 1) == 0) {
			EnumFacing side = EnumFacing.VALUES[this.getBlockMetadata() - BlockMulti.OFFSET].getOpposite();
			this.worldObj.spawnParticle("nuclear_program/drainage",
					this.xCoord + 0.5 + side.getOffsetX() * 2.5, this.yCoord + 0.5, this.zCoord + 0.5 + side.getOffsetZ() * 2.5,
					0.0, this.fluidType, 0.0);
		}
	}

	public void setDrainedFluid(int fluidType, boolean client) {
		if (fluidType == this.fluidType || client) return;
		if (fluidType == FluidInit.heavyOil.fluidID) {
			this.setFluidType(fluidType);
		}
		if (fluidType == FluidInit.diesel.fluidID) {
			this.setFluidType(fluidType);
		}
		if (fluidType == FluidInit.kerosene.fluidID) {
			this.setFluidType(fluidType);
		}
		if (fluidType == FluidInit.naphtha.fluidID) {
			this.setFluidType(fluidType);
		}
		if (fluidType == FluidInit.gasoline.fluidID) {
			this.setFluidType(fluidType);
		}
		if (fluidType == FluidInit.lpg.fluidID) {
			this.setFluidType(fluidType);
		}
	}
	private void setFluidType(int fluidType) {
		this.fluidType = fluidType;
		this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("fluidType", this.fluidType);
		nbt.setLong("tank", this.tank);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.fluidType = nbt.getInteger("fluidType");
		this.tank = nbt.getLong("tank");
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return fluidType == this.fluidType ? TANK_CAPACITY : 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return fluidType == this.fluidType ? TANK_CAPACITY - this.tank : 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType != this.fluidType || this.tank == TANK_CAPACITY || amount == 0) return amount;
		long remain = amount - (TANK_CAPACITY - this.tank);
		if (remain <= 0) {
			this.tank += amount;
			return 0L;
		} else {
			this.tank = TANK_CAPACITY;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.VOID_PRIORITY;
	}
}
