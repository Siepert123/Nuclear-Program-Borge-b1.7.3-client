package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.fluid.FluidTraitCoolable;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityHeatexBoiler extends TileEntity implements IFluidReceiverTE {
	public int fluidType = FluidInit.carbonDioxideHot_Id;
	public int fluidTypeOut = FluidInit.carbonDioxide_Id;

	public static final long TANK_CAPACITY_COOLANT = 8000L;
	public static final long TANK_CAPACITY_WATER = 8000L;
	public static final long TANK_CAPACITY_STEAM = TANK_CAPACITY_WATER * 128L;
	public long tankCoolantIn = 0L;
	public long tankCoolantOut = 0L;
	public long tankWater = 0L;
	public long tankSteam = 0L;

	public TileEntityHeatexBoiler() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.tankCoolantIn > 0L && this.tankWater > 0L && this.fluidTypeOut != 0) {
				long conversion = Math.min(TANK_CAPACITY_COOLANT - this.tankCoolantOut, this.tankCoolantIn);
				FluidTraitCoolable recipe = Fluid.traitCoolable[this.fluidType];
				if (conversion > 0L && recipe != null) {
					update = true;
					this.tankCoolantOut += conversion;
					this.tankCoolantIn -= conversion;
					int heat = Math.toIntExact(recipe.thermalCapacity * conversion);
					long boiled = Math.min((TANK_CAPACITY_STEAM - this.tankSteam) / 128, Math.min(this.tankWater, heat / 100));
					if (boiled > 0L) {
						this.tankSteam += boiled * 128L;
						this.tankWater -= boiled;
					}
				}
			}

			if (this.tankCoolantOut > 0L || this.tankSteam > 0L) {
				for (EnumFacing side : EnumFacing.VALUES) {
					PipeNetNode node = PipeNet.getNode(this.worldObj,
							this.xCoord + side.getOffsetX(),
							this.yCoord + side.getOffsetY(),
							this.zCoord + side.getOffsetZ()
					);
					if (node != null) {
						update = true;
						this.tankCoolantOut = node.pushFluid(this.fluidTypeOut, this.tankCoolantOut);
						this.tankSteam = node.pushFluid(FluidInit.steam_Id, this.tankSteam);
					}
				}
			}
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("fluidType", this.fluidType);
		nbt.setLong("tankCoolantIn", this.tankCoolantIn);
		nbt.setLong("tankCoolantOut", this.tankCoolantOut);
		nbt.setLong("tankWater", this.tankWater);
		nbt.setLong("tankSteam", this.tankSteam);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		if (bar != 1) return 0L;
		if (fluidType == this.fluidType) return TANK_CAPACITY_COOLANT;
		if (fluidType == FluidInit.water_Id) return TANK_CAPACITY_WATER;
		return 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		if (bar != 1) return 0L;
		if (fluidType == this.fluidType) return TANK_CAPACITY_COOLANT - this.tankCoolantIn;
		if (fluidType == FluidInit.water_Id) return TANK_CAPACITY_WATER - this.tankWater;
		return 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (bar != 1) return amount;
		if (fluidType == this.fluidType) {
			long remain = amount = (TANK_CAPACITY_COOLANT - this.tankCoolantIn);
			if (remain <= 0L) {
				this.tankCoolantIn += amount;
				return 0L;
			} else {
				this.tankCoolantIn = TANK_CAPACITY_COOLANT;
				return remain;
			}
		}
		if (fluidType == FluidInit.water_Id) {
			long remain = amount = (TANK_CAPACITY_WATER - this.tankWater);
			if (remain <= 0L) {
				this.tankWater += amount;
				return 0L;
			} else {
				this.tankWater = TANK_CAPACITY_WATER;
				return remain;
			}
		}
		return amount;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.ENGINE_PRIORITY;
	}
}
