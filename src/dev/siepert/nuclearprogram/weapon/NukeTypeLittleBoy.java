package dev.siepert.nuclearprogram.weapon;

import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.util.IEasing;

public class NukeTypeLittleBoy extends NukeType {
	public NukeTypeLittleBoy(int registryID) {
		super(registryID);
	}

	@Override
	public int getBlastRadius() {
		return 128;
	}

	@Override
	public int getCharredTreesRadius() {
		return 256;
	}

	@Override
	public int getDestroyedLeavesRadius() {
		return 256;
	}

	@Override
	public float getReleasedRadiation() {
		return 0;
	}

	@Override
	public int getReleasedRadiationLingerTicks() {
		return 0;
	}

	@Override
	public IEasing getReleasedRadiationDropOff() {
		return Easing.LINEAR;
	}

	@Override
	public int getFalloutRadius() {
		return 0;
	}

	@Override
	public boolean hasDarkenedNuclearRemains() {
		return true;
	}

	@Override
	public boolean hasShockwave() {
		return true;
	}

	@Override
	public boolean isSoulType() {
		return false;
	}

	@Override
	public float getMushroomCloudSize() {
		return 10;
	}

	@Override
	public int getSoundDistance() {
		return 256;
	}
}
