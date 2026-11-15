package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityBellows extends TileEntityMachineBase {
	public int cooldown = 0;
	public boolean powered = false;

	public TileEntityBellows() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (this.cooldown > 0) {
			this.cooldown--;
			update = true;
		}

		if (update) this.onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("cooldown", this.cooldown);
		nbt.setBoolean("powered", this.powered);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.cooldown = nbt.getInteger("cooldown");
		this.powered = nbt.getBoolean("powered");
	}

	public void update() {
		boolean powered = false;
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			int ox = this.xCoord + side.getOffsetX();
			int oz = this.zCoord + side.getOffsetZ();
			powered |= (this.worldObj.isBlockGettingPowered(ox, this.yCoord, oz) || this.worldObj.isBlockIndirectlyGettingPowered(ox, this.yCoord, oz));
		}
		if (powered != this.powered) {
			this.powered = powered;
			if (this.powered) this.activate();
			this.onInventoryChanged();
		}
	}
	public void activate() {
		if (this.cooldown > 0) return;
		this.cooldown = 20;
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			int x = this.xCoord + side.getOffsetX()*2;
			int y = this.yCoord;
			int z = this.zCoord + side.getOffsetZ()*2;
			PipeNetNode node = PipeNet.getNode(this.worldObj, x, y, z);
			if (node != null) node.pushFluid(FluidInit.air_Id, 128_000L);
		}
		this.worldObj.playSoundEffect(
				this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5,
				"machine.bellows", 1.0F, 1.0F
		);
		this.onInventoryChanged();
	}
}
