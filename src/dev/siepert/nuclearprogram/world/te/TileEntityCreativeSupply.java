package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityCreativeSupply extends TileEntity {
	public TileEntityCreativeSupply() {}

	public int fluidType = 0;
	@Override
	public void updateEntity() {
		if (this.fluidType != 0) {
			for (EnumFacing side : EnumFacing.VALUES) {
				PipeNetNode node = PipeNet.getNode(this.worldObj,
						this.xCoord + side.getOffsetX(),
						this.yCoord + side.getOffsetY(),
						this.zCoord + side.getOffsetZ()
				);
				if (node != null) {
					node.pushFluid(this.fluidType, Long.MAX_VALUE, 1);
				}
			}
		} else {
			for (EnumFacing side : EnumFacing.VALUES) {
				CableNetNode node = CableNet.getNode(this.worldObj,
						this.xCoord + side.getOffsetX(),
						this.yCoord + side.getOffsetY(),
						this.zCoord + side.getOffsetZ()
				);
				if (node != null) {
					node.pushEnergy(Long.MAX_VALUE);
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("fluidType", this.fluidType);
		nbt.setInteger("fluidPressure", 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.fluidType = nbt.getInteger("fluidType");
	}
}
