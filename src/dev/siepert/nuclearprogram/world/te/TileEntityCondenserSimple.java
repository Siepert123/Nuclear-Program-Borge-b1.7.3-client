package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityCondenserSimple extends TileEntity implements IFluidReceiverTE {
	public static final long CAPACITY = 1000L;

	public long tankDepletedSteam = 0L;
	public long tankWater = 0L;

	public TileEntityCondenserSimple() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.tankWater > 0L) {
				for (EnumFacing side : EnumFacing.VALUES) {
					PipeNetNode node = PipeNet.getNode(this.worldObj,
							this.xCoord + side.getOffsetX(),
							this.yCoord + side.getOffsetY(),
							this.zCoord + side.getOffsetZ()
					);
					if (node != null) {
						update = true;
						this.tankWater = node.pushFluid(FluidInit.water_Id, this.tankWater);
						if (this.tankWater == 0L) break;
					}
				}
			}

			long condensed = Math.min(this.tankDepletedSteam, CAPACITY - this.tankWater);
			if (condensed > 0L) {
				update = true;
				this.tankDepletedSteam -= condensed;
				this.tankWater += condensed;
			}
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("tankDepletedSteam", this.tankDepletedSteam);
		nbt.setLong("tankWater", this.tankWater);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tankDepletedSteam = nbt.getLong("tankDepletedSteam");
		this.tankWater = nbt.getLong("tankWater");
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		if (fluidType != FluidInit.depletedSteam_Id || bar != 1) return 0L;
		return CAPACITY;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		if (fluidType != FluidInit.depletedSteam_Id || bar != 1) return 0L;
		return CAPACITY - this.tankDepletedSteam;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType != FluidInit.depletedSteam_Id || bar != 1) return amount;
		this.onInventoryChanged();
		long remain = amount - (CAPACITY - this.tankDepletedSteam);
		if (remain <= 0L) {
			this.tankDepletedSteam += amount;
			return 0L;
		} else {
			this.tankDepletedSteam = CAPACITY;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.ENGINE_PRIORITY;
	}
}
