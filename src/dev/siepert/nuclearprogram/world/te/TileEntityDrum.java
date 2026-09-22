package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityDrum extends TileEntity implements IFluidReceiverTE {
	public long fill = 0L;
	public long capacity;
	public int type = 0;
	public boolean extract = false;
	private boolean blocking = false;

	public TileEntityDrum() {

	}

	public TileEntityDrum setCapacity(long capacity) {
		this.capacity = capacity;
		return this;
	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld && this.extract) {
			this.blocking = true;
			for (EnumFacing side : EnumFacing.VALUES) {
				PipeNetNode node = PipeNet.getNode(this.worldObj,
						this.xCoord + side.getOffsetX(),
						this.yCoord + side.getOffsetY(),
						this.zCoord + side.getOffsetZ()
				);
				if (node != null) {
					update = true;
					this.fill = node.pushFluid(this.type, this.fill);
				}
			}
			this.blocking = false;
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("fill", this.fill);
		nbt.setLong("capacity", this.capacity);
		nbt.setInteger("type", this.type);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.fill = nbt.getLong("fill");
		this.capacity = nbt.getLong("capacity");
		this.type = nbt.getInteger("type");
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		if (this.blocking) return 0L;
		if (fluidType != this.type || bar != 1) return 0L;
		return this.capacity;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		if (this.blocking) return 0L;
		if (fluidType != this.type || bar != 1) return 0L;
		return this.capacity - this.fill;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (this.blocking) return amount;
		if (fluidType != this.type || bar != 1) return amount;
		this.onInventoryChanged();
		long remain = amount - (this.capacity - this.fill);
		if (remain <= 0L) {
			this.fill += amount;
			return 0L;
		} else {
			this.fill = this.capacity;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.BUFFER_PRIORITY;
	}
}
