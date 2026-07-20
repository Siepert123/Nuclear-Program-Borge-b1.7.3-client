package dev.siepert.nuclearprogram.world.te;

import java.util.List;

public class TileEntityRBMKFuel extends TileEntityRBMKColumn {
	public TileEntityRBMKFuel() {

	}

	@Override
	protected void doMeltdownFX() {
		this.worldObj.newExplosion(null,
				this.xCoord + 0.5, this.yCoord + this.worldObj.rand.nextDouble() * 4.0 + 3, this.zCoord + 0.5,
				5.0F, true
		);
	}

	@Override
	protected void logicTick() {
		this.heat += 20.0;
	}

	@Override
	public void debug(List<String> props) {
		super.debug(props);
		props.add("Fuel: NULL");
	}
}
