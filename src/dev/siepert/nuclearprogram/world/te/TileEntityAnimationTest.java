package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.TrackedSound;

public class TileEntityAnimationTest extends TileEntityMachineBase {
	public static final int DOME_OPEN_TICKS = 8 * 20 - 15;
	public static final int DOME_CLOSE_TICKS = 20 * 6 + 10;
	public static final int DOME_SLOWDOWN_TICKS = 140;
	public static final int DOME_SPEEDUP_TICKS = 21 * 20 - DOME_CLOSE_TICKS;
	public static final float ROTATION_SPEED = 360.0F / 20.0F / 5.0F;

	public TileEntityAnimationTest() {

	}

	public int animationTicks = 0;
	public boolean isOpen = true;
	public TrackedSound soundLoop = null;
	public int rotation = 0;

	@Override
	public void updateEntity() {
		if (this.animationTicks > 0) this.animationTicks--;
		else {
			if (this.isOpen) {
				if (this.soundLoop != null) {
					this.soundLoop.getSystem().stop(this.soundLoop.getSoundID());
					this.soundLoop.invalidate();
					this.soundLoop = null;
				}
			} else {
				this.rotation++;
				if (this.soundLoop == null) {
					this.soundLoop = Minecraft.getTheMinecraft().sndManager
							.playTrackedSound("machine.hsrfs.loop",
									this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F,
									1.0F, 1.0F, true
							);
					if (this.soundLoop == null) this.soundLoop = NuclearProgram.getLastTrackedSound();
				}
			}
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (this.soundLoop != null && this.soundLoop.isValid()) {
			this.soundLoop.getSystem().stop(this.soundLoop.getSoundID());
			this.soundLoop = null;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("animationTicks", this.animationTicks);
		nbt.setBoolean("isOpen", this.isOpen);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.animationTicks = nbt.getInteger("animationTicks");
		this.isOpen = nbt.getBoolean("isOpen");
	}

	public boolean toggle() {
		if (this.animationTicks > 0) return false;
		this.isOpen = !this.isOpen;
		if (this.isOpen) {
			this.animationTicks = DOME_SLOWDOWN_TICKS + DOME_OPEN_TICKS;
			this.worldObj.playSoundEffect(
					this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5,
					"machine.hsrfs.open", 1.0F, 1.0F
			);
			if (this.soundLoop != null) {
				this.soundLoop.getSystem().stop(this.soundLoop.getSoundID());
				this.soundLoop.invalidate();
				this.soundLoop = null;
			}
		} else {
			this.animationTicks = DOME_CLOSE_TICKS + DOME_SPEEDUP_TICKS;
			this.worldObj.playSoundEffect(
					this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5,
					"machine.hsrfs.close", 1.0F, 1.0F
			);
		}
		return true;
	}

	@Override
	protected void finalize() throws Throwable {
		if (this.soundLoop != null && this.soundLoop.isValid()) {
			this.soundLoop.getSystem().stop(this.soundLoop.getSoundID());
			this.soundLoop = null;
		}
		super.finalize();
	}
}
