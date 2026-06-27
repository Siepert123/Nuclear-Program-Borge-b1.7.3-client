package dev.siepert.nuclearprogram.world.te;

import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.BorgeMath;

import java.util.Random;

public class TileEntityHatch extends TileEntity {
	public static final int HATCH_VALVE_OPEN_TICKS = 50;
	public static final int HATCH_TURN_OPEN_TICKS = 27;
	public static final int HATCH_TURN_CLOSE_TICKS = 23;
	public static final int HATCH_VALVE_CLOSE_TICKS = 62;

	public TileEntityHatch() {

	}

	public float valveOffset = new Random().nextFloat() * 90.0F;
	public Boolean openState = null;
	public int valveTicks = -1;
	public int turnTicks = -1;
	public int animationTicks = 0;

	public boolean isOpen() {
		if (this.openState == null) {
			this.openState = (this.getBlockMetadata() & 0b0100) != 0;
		}
		return this.openState;
	}

	@Override
	public void updateEntity() {
		this.openState = null;
		if (this.animationTicks > 0) this.animationTicks--;

		if (this.isOpen()) {
			if (this.valveTicks != -1 && this.valveTicks < HATCH_VALVE_OPEN_TICKS) {
				this.valveTicks++;
			} else if (this.turnTicks != -1 && this.turnTicks < HATCH_TURN_OPEN_TICKS) {
				this.turnTicks++;
				if (this.turnTicks >= HATCH_TURN_OPEN_TICKS) {
					this.valveTicks = -1;
					this.turnTicks = -1;
				}
			}
		} else {
			if (this.turnTicks != -1 && this.turnTicks < HATCH_TURN_CLOSE_TICKS) {
				this.turnTicks++;
			} else if (this.valveTicks != -1 && this.valveTicks < HATCH_VALVE_CLOSE_TICKS) {
				this.valveTicks++;
				if (this.valveTicks >= HATCH_VALVE_CLOSE_TICKS) {
					this.turnTicks = -1;
					this.valveTicks = -1;
				}
			}
		}
	}

	public boolean canSwitchState() {
		return this.animationTicks <= 0;
	}
	public boolean mayPass() {
		return this.isOpen() && this.turnTicks == -1;
	}

	public float getOpen(float pt) {
		if (this.turnTicks < 0) return this.isOpen() ? 1.0F : 0.0F;
		if (this.isOpen() && this.turnTicks == 0 && this.valveTicks < HATCH_VALVE_OPEN_TICKS) return 0.0F;
		float transition = this.turnTicks + pt;
		return BorgeMath.clamp(this.isOpen() ? transition / HATCH_TURN_OPEN_TICKS : 1.0F-(transition / HATCH_TURN_CLOSE_TICKS), 0.0F, 1.0F);
	}
	public float getValve(float pt) {
		if (this.valveTicks < 0) return 0.0F;
		if (!this.isOpen() && this.valveTicks == 0 && this.turnTicks < HATCH_TURN_CLOSE_TICKS) return 0.0F;
		float transition = this.valveTicks + pt;
		return BorgeMath.clamp(this.isOpen() ? transition / HATCH_VALVE_OPEN_TICKS : 1.0F-(transition / HATCH_VALVE_CLOSE_TICKS), 0.0F, 1.0F);
	}

	public void setOpen(boolean open) {
		this.valveTicks = 0;
		this.turnTicks = 0;
		this.openState = open;
		this.animationTicks = open ? 90 : 95;
	}
}
