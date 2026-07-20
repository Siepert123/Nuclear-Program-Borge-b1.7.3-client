package dev.siepert.nuclearprogram.world.te;

import java.util.List;
import java.util.Random;

public class TileEntityRBMKBoiler extends TileEntityRBMKColumn {
	protected int fx = 0;

	public TileEntityRBMKBoiler() {

	}

	@Override
	protected void doMeltdownFX() {
		Random rnd = this.worldObj.rand;
		this.worldObj.spawnParticle("nuclear_program/steam",
				this.xCoord + rnd.nextDouble(), this.yCoord + 6.5 + rnd.nextDouble(), this.zCoord + rnd.nextDouble(),
				0, 0, 0
		);
		this.worldObj.spawnParticle("nuclear_program/steam",
				this.xCoord + rnd.nextDouble(), this.yCoord + 6.5 + rnd.nextDouble(), this.zCoord + rnd.nextDouble(),
				0, 0, 0
		);
		this.worldObj.spawnParticle("nuclear_program/steam",
				this.xCoord + rnd.nextDouble(), this.yCoord + 6.5 + rnd.nextDouble(), this.zCoord + rnd.nextDouble(),
				0, 0, 0
		);
		this.worldObj.spawnParticle("nuclear_program/steam",
				this.xCoord + rnd.nextDouble(), this.yCoord + 6.5 + rnd.nextDouble(), this.zCoord + rnd.nextDouble(),
				0, 0, 0
		);
		this.worldObj.spawnParticle("nuclear_program/steam",
				this.xCoord + rnd.nextDouble(), this.yCoord + 6.5 + rnd.nextDouble(), this.zCoord + rnd.nextDouble(),
				0, 0, 0
		);
	}

	@Override
	protected void logicTick() {
		if (this.heat > 300.0) {
			this.heat = 300.0;

			if ((this.fx++ & 3) == 0) {
				this.worldObj.spawnParticle("nuclear_program/steam",
						this.xCoord + 0.5, this.yCoord + 7.0, this.zCoord + 0.5,
						0, 0, 0
				);
			}
		}
	}

	@Override
	public void debug(List<String> props) {
		super.debug(props);
		props.add("water: NULL mB");
		props.add("steam: NULL mB");
		props.add("pressure: NULL bar");
		props.add("fx: " + this.fx);
	}
}
