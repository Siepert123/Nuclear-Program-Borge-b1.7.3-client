package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityTurbineSimple extends TileEntity implements IFluidReceiverTE {
	public static final long CAPACITY_STEAM = 8000L * 128L;
	public static final long CAPACITY_DEPLETED_STEAM = 8000L;
	public static final long CAPACITY_ENERGY = 1_000_000L;
	public long tankSteam = 0L;
	public long tankDepletedSteam = 0L;
	public long processed = 0L;
	public long energy = 0L;

	public TileEntityTurbineSimple() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.tankDepletedSteam > 0L) {
				for (EnumFacing side : EnumFacing.VALUES) {
					PipeNetNode node = PipeNet.getNode(this.worldObj,
							this.xCoord + side.getOffsetX(),
							this.yCoord + side.getOffsetY(),
							this.zCoord + side.getOffsetZ()
					);
					if (node != null) {
						update = true;
						this.tankDepletedSteam = node.pushFluid(FluidInit.depletedSteam_Id, this.tankDepletedSteam);
						if (this.tankDepletedSteam == 0L) break;
					}
				}
			}
			if (this.energy > 0L) {
				for (EnumFacing side : EnumFacing.VALUES) {
					CableNetNode node = CableNet.getNode(this.worldObj,
							this.xCoord + side.getOffsetX(),
							this.yCoord + side.getOffsetY(),
							this.zCoord + side.getOffsetZ()
					);
					if (node != null) {
						update = true;
						this.energy = node.pushEnergy(this.energy);
						if (this.energy == 0L) break;
					}
				}
			}

			this.processed = Math.min(this.tankSteam / 128L, CAPACITY_DEPLETED_STEAM - this.tankDepletedSteam);
			if (this.processed > 0L) {
				update = true;
				this.tankSteam -= this.processed * 128L;
				this.tankDepletedSteam += this.processed;
				this.energy = Math.min(this.energy + this.processed * 100, CAPACITY_ENERGY);
			}
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("tankSteam", this.tankSteam);
		nbt.setLong("tankDepletedSteam", this.tankDepletedSteam);
		nbt.setLong("energy", this.energy);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tankSteam = nbt.getLong("tankSteam");
		this.tankDepletedSteam = nbt.getLong("tankDepletedSteam");
		this.energy = nbt.getLong("energy");
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		if (fluidType != FluidInit.steam_Id || bar != 1) return 0L;
		return CAPACITY_STEAM;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		if (fluidType != FluidInit.steam_Id || bar != 1) return 0L;
		return CAPACITY_STEAM - this.tankSteam;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType != FluidInit.steam_Id || bar != 1) return amount;
		this.onInventoryChanged();
		long remain = amount - (CAPACITY_STEAM - this.tankSteam);
		if (remain <= 0L) {
			this.tankSteam += amount;
			return 0L;
		} else {
			this.tankSteam = CAPACITY_STEAM;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.ENGINE_PRIORITY;
	}
}
