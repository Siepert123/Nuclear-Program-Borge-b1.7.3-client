package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.BorgeMath;
import net.minecraftborge.loader.ITickingTile;

public class TileEntitySealedDoor extends TileEntity implements ITickingTile {
	public static final int DOOR_TRANSITION_TICKS = 40;
	public static final int DOOR_UNSCREW_TICKS = 65;
	public TileEntitySealedDoor() {

	}

	public boolean lastShowScrews = false;
	public boolean showScrews = false;

	public int unscrewTicks = -1;
	public int transitionTicks = -1;
	public int animationTicks = 0;
	public Boolean openState = null;

	public boolean isOpen() {
		if (this.openState == null) {
			this.openState = (this.getBlockMetadata() & 0b0100) != 0;
		}
		return this.openState;
	}

	@Override
	public void updateEntity() {
		this.openState = null;
		this.lastShowScrews = this.showScrews;
		if (this.animationTicks > 0) this.animationTicks--;
		if (this.unscrewTicks != -1 && this.unscrewTicks < DOOR_UNSCREW_TICKS && this.isOpen()) {
			this.unscrewTicks++;
		} else if (this.transitionTicks != -1 && this.transitionTicks < DOOR_TRANSITION_TICKS) {
			this.transitionTicks++;
			if (this.transitionTicks >= DOOR_TRANSITION_TICKS) {
				this.transitionTicks = -1;
				this.unscrewTicks = -1;
			}
		}
		this.showScrews = this.showScrews();
	}

	public boolean canSwitchState() {
		if (this.worldObj == null) return false;
		return this.animationTicks <= 0 &&
				!this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) &&
				!this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord+1, this.zCoord);
	}

	public boolean mayPass() {
		return this.isOpen() && this.unscrewTicks == -1;
	}

	public float getSlide(float pt) {
		if (transitionTicks < 0) return this.isOpen() ? 1.0F : 0.0F;
		if (this.transitionTicks == 0 && this.unscrewTicks < DOOR_UNSCREW_TICKS && this.isOpen()) return 0.0F;
		float transition = this.transitionTicks + pt;
		return this.isOpen() ? transition / DOOR_TRANSITION_TICKS : 1.0F-(transition / DOOR_TRANSITION_TICKS);
	}
	public boolean showScrews() {
		if (this.isOpen()) {
			return this.unscrewTicks > 0 && this.unscrewTicks < DOOR_UNSCREW_TICKS;
		} else {
			return this.animationTicks <= 0;
		}
	}
	// 0.0F to 1.0F
	public float getScrewRot(float pt) {
		if (this.unscrewTicks < 0 || !this.isOpen()) return 0.0F;
		float unscrew = this.unscrewTicks + pt;
		return BorgeMath.clamp(unscrew / DOOR_UNSCREW_TICKS, 0.0F, 1.0F);
	}

	public float getScrewExpansion(float pt) {
		if (this.showScrews == this.lastShowScrews) return this.showScrews ? 0.0F : 0.125F;
		return this.showScrews ? BorgeMath.clampedLerp(0.125F, 0.0F, pt) : BorgeMath.clampedLerp(0.0F, 0.125F, pt);
	}

	public void setOpen(boolean open) {
		this.unscrewTicks = 0;
		this.transitionTicks = 0;
		this.openState = open;
		this.animationTicks = open ? 200 : 70;
	}
}
