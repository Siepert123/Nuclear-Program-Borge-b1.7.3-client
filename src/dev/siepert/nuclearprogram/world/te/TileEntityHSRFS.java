package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.TrackedSound;

public class TileEntityHSRFS extends TileEntityMachineBase {
	public static final int[] OPENING_ANIMATION_TICKS = {7238/50, 9355/50 - (7238/50), 14011/50 - (9355/50)};
	public static final int[] CLOSING_ANIMATION_TICKS = {3276/50, 6462/50 - (3276/50), 18000/50 - (6462/50)};
	public static final float ROTATION_MULTIPLIER = 8.0F;

	public TileEntityHSRFS() {

	}

	public int animationTicks = 0;
	public int animAcceleration = 0;
	public int animRotation = 0;
	public int animPause = 0;
	public int rotation = 0;
	public boolean open = true;
	public TrackedSound soundLoop = null;

	@Override
	public void updateEntity() {
		if (this.animationTicks > 0) {
			this.animationTicks--;
			if (this.open) {
				if (this.animAcceleration < OPENING_ANIMATION_TICKS[0]) {
					this.animAcceleration++;
				} else if (this.animPause < OPENING_ANIMATION_TICKS[1]) {
					this.animPause++;
				} else if (this.animRotation < OPENING_ANIMATION_TICKS[2]) {
					this.animRotation++;
				}
			} else {
				if (this.animPause < CLOSING_ANIMATION_TICKS[0]) {
					this.animPause++;
				} else if (this.animRotation < CLOSING_ANIMATION_TICKS[1]) {
					this.animRotation++;
				} else if (this.animAcceleration < CLOSING_ANIMATION_TICKS[2]) {
					this.animAcceleration++;
				} else this.rotation++;
			}
		} else {
			if (this.open) {
				if (this.soundLoop != null) {
					this.stopLoop();
					this.soundLoop = null;
				}
			} else {
				this.rotation++;
				if (this.soundLoop == null) {
					this.startLoop();
				}
			}
		}
	}

	private void startLoop() {
		this.soundLoop = Minecraft.getTheMinecraft().sndManager.playTrackedSound("machine.hsrfs.loop",
				this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F,
				1.0F, 1.0F, true
		);
		if (this.soundLoop == null) this.soundLoop = NuclearProgram.getLastTrackedSound();
	}
	private void stopLoop() {
		if (this.soundLoop.isValid()) {
			this.soundLoop.getSystem().stop(this.soundLoop.getSoundID());
			this.soundLoop.invalidate();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("state", this.open);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.open = nbt.getBoolean("state");
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (this.soundLoop != null) this.stopLoop();
	}

	public boolean canSwitchState() {
		return this.animationTicks == 0;
	}

	public void setState(boolean state) {
		if (this.open == state) return;
		this.open = state;
		this.animAcceleration = 0;
		this.animPause = 0;
		this.animRotation = 0;
		if (state) {
			this.worldObj.playSoundEffect(
					this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5,
					"machine.hsrfs.open", 1.0F, 1.0F
			);
			this.animationTicks = 20 * 20;
			if (this.soundLoop != null) this.stopLoop();
		} else {
			this.worldObj.playSoundEffect(
					this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5,
					"machine.hsrfs.close", 1.0F, 1.0F
			);
			this.animationTicks = 21 * 20;
		}
	}
}
