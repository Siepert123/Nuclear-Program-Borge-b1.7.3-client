package dev.siepert.nuclearprogram.weapon;

import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.util.IEasing;

public class NukeTypeCharge extends NukeType {
	public NukeTypeCharge(int registryID) {
		super(registryID);
	}

	@Override
	public int getBlastRadius() {
		return 64;
	}

	@Override
	public int getNuclearRemainsRadius() {
		return 32;
	}

	@Override
	public int getCharredTreesRadius() {
		return 0;
	}

	@Override
	public int getDestroyedLeavesRadius() {
		return 0;
	}

	@Override
	public float getReleasedRadiation() {
		return 100F;
	}

	@Override
	public int getReleasedRadiationLingerTicks() {
		return 200;
	}

	@Override
	public IEasing getReleasedRadiationDropOff() {
		return Easing.OUT_CUBIC;
	}

	@Override
	public int getFalloutRadius() {
		return 0;
	}

	@Override
	public boolean hasDarkenedNuclearRemains() {
		return false;
	}

	@Override
	public boolean hasShockwave() {
		return false;
	}

	@Override
	public boolean isSoulType() {
		return false;
	}

	@Override
	public float getMushroomCloudSize() {
		return 5;
	}

	@Override
	public int getEventData() {
		return 7;
	}

	@Override
	public int getMinimalBiomeRadius() {
		return 0;
	}

	@Override
	public int getNormalBiomeRadius() {
		return 0;
	}

	@Override
	public int getSevereBiomeRadius() {
		return 0;
	}

	@Override
	public boolean brightensSky() {
		return false;
	}
}
