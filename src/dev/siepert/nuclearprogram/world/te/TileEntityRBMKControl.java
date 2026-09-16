package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;

import java.util.List;

public class TileEntityRBMKControl extends TileEntityRBMKColumn {
	public TileEntityRBMKControl() {

	}

	public static final float RAISE_SPEED = 0.01F;
	public static final float LOWER_SPEED = 0.025F;
	public float controlOld = 0.0F;
	public float control = 0.0F;
	public float target = 0.0F;

	@Override
	protected void logicTick() {
		this.controlOld = this.control;

		if (this.control < this.target) {
			this.control = Math.min(this.control + RAISE_SPEED, this.target);
		} else if (this.control > this.target) {
			this.control = Math.max(this.control - LOWER_SPEED, this.target);
		}
	}

	@Override
	public boolean blockActivated(EntityPlayer player) {
		this.target = this.worldObj.rand.nextFloat();
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setFloat("control", this.control);
		nbt.setFloat("targetControl", this.target);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.control = nbt.getFloat("control");
		this.target = nbt.getFloat("targetControl");
	}

	@Override
	public void debug(List<String> props) {
		super.debug(props);
		props.add("Control: " + (this.control * 100) + "%");
		props.add("Target: " + (this.target * 100) + "%");
	}
}
