package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.BorgeMath;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityEngineV8 extends TileEntityMachineBase implements IFluidReceiverTE {
	public TileEntityEngineV8() {

	}

	public int animation = 0;
	public boolean running = true;
	public float getAnimationSpeed() {
		return 25.0F * this.burnRate;
	}

	public int fluidType = FluidInit.diesel.fluidID;
	public static final long COMBUSTION_RATE = 5L;
	public static final long TANK_CAPACITY = COMBUSTION_RATE * 20 * 60;
	public long tank = 0L;
	public long energy = 0L;
	public int burnRate = 5;

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld && this.energy > 0L) {
			update = true;
			EnumFacing out1 = EnumFacing.VALUES[this.getBlockMetadata() - BlockMulti.OFFSET];
			EnumFacing out2 = out1.getOpposite();

			CableNetNode node1 = CableNet.getNode(this.worldObj, this.xCoord + out1.getOffsetX()*2, this.yCoord, this.zCoord + out1.getOffsetZ()*2);
			CableNetNode node2 = CableNet.getNode(this.worldObj, this.xCoord + out2.getOffsetX()*2, this.yCoord, this.zCoord + out2.getOffsetZ()*2);

			if (node1 != null) this.energy = node1.pushEnergy(this.energy);
			if (node2 != null) node2.pushEnergy(this.energy);

			this.energy = 0L;
		}

		if (this.tank < this.burnRate) {
			this.running = false;
		} else {
			this.running = true;
			this.animation++;
			update = true;
			this.tank -= this.burnRate;
			this.energy += 200L * this.burnRate;
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("fluidType", this.fluidType);
		nbt.setLong("tank", this.tank);
		nbt.setLong("energy", this.energy);
		nbt.setInteger("burnRate", this.burnRate);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.fluidType = nbt.getInteger("fluidType");
		this.tank = nbt.getLong("tank");
		this.energy = nbt.getLong("energy");
		this.burnRate = NPMth.clamp(nbt.getInteger("burnRate"), 1, 10);
	}

	public void setBurnedFluid(int fluidID) {
		if (this.fluidType == fluidID) return;
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return bar == 1 && fluidType == this.fluidType ? TANK_CAPACITY : 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return bar == 1 && fluidType == this.fluidType ? TANK_CAPACITY - this.tank : 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (bar == 1 && fluidType == this.fluidType && amount != 0L && this.tank < TANK_CAPACITY) {
			this.onInventoryChanged();
			long remain = amount - (TANK_CAPACITY - this.tank);
			if (remain <= 0L) {
				this.tank += amount;
				return 0L;
			} else {
				this.tank = TANK_CAPACITY;
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
