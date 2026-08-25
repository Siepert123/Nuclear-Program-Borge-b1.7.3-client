package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityOilDistillerySegment extends TileEntity {
	public TileEntityOilDistillerySegment() {

	}

	public static final long TANK_CAPACITY = 8_000L;
	public int fluidType = 0;
	public long tank = 0L;

	private int age = 0;
	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.age++ % 10 == 0 && this.fluidType != 0 && this.tank > 0L) {
				for (int i = 0; i < 4 && this.tank > 0L; i++) {
					EnumFacing side = EnumFacing.HORIZONTALS[i];
					PipeNetNode node = PipeNet.getNode(this.worldObj, this.xCoord + side.getOffsetX() * 2, this.yCoord, this.zCoord + side.getOffsetZ() * 2);
					if (node != null) {
						update = true;
						this.tank = node.pushFluid(this.fluidType, this.tank, 1);
					}
				}
			}
		}

		if (update) this.onInventoryChanged();
	}

	public int getFluidFillScaled(int w) {
		if (this.tank == 0L) return 0;
		return Math.toIntExact(this.tank * w / (TANK_CAPACITY+1))+1;
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
}
