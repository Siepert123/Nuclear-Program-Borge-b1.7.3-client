package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileEntityAccumulator extends TileEntity implements IEnergyReceiverTE {
	public long maxEnergy = 0L;
	public long energy = 0L;
	public boolean discharge = true;
	private boolean discharging = false;

	public TileEntityAccumulator(long maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	public TileEntityAccumulator() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.discharge && this.energy > 0L) {
				CableNetNode node = CableNet.getNode(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord);
				if (node != null) {
					update = true;
					this.discharging = true;
					this.energy = node.pushEnergy(this.energy);
					this.discharging = false;
				}
			}
			this.discharge = true;
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("maxEnergy", this.maxEnergy);
		nbt.setLong("energy", this.energy);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.maxEnergy = nbt.getLong("maxEnergy");
		this.energy = nbt.getLong("energy");
	}

	@Override
	public long getEnergyCapacity() {
		return this.discharging ? 0L : this.maxEnergy;
	}
	@Override
	public long getRemainingEnergyCapacity() {
		return this.discharging ? 0L : this.maxEnergy - this.energy;
	}
	@Override
	public long addEnergy(long amount) {
		if (this.discharging) return amount;
		this.onInventoryChanged();
		this.discharge = false;
		long remain = amount - (this.maxEnergy - this.energy);
		if (remain <= 0L) {
			this.energy += amount;
			return 0L;
		} else {
			this.energy = this.maxEnergy;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.BUFFER_PRIORITY;
	}
}
