package dev.siepert.nuclearprogram.weapon;

import dev.siepert.nuclearprogram.util.IEasing;

public abstract class NukeType {
	public final int registryID;
	public NukeType(int registryID) {
		this.registryID = registryID;
		NukeTypes.LIST[registryID] = this;
	}

	public abstract int getBlastRadius();
	public float getBlastStrength() {
		return this.getBlastRadius() * 2;
	}
	public int getNuclearRemainsRadius() {
		return this.getBlastRadius();
	}
	public abstract int getCharredTreesRadius();
	public abstract int getDestroyedLeavesRadius();
	public abstract float getReleasedRadiation();
	public abstract int getReleasedRadiationLingerTicks();
	public abstract IEasing getReleasedRadiationDropOff();
	public abstract int getFalloutRadius();
	public abstract boolean hasDarkenedNuclearRemains();
	public abstract boolean hasShockwave();
	public abstract boolean isSoulType();
	public abstract float getMushroomCloudSize();
	public int getEntityBlowRadius() {
		return this.getBlastRadius() * 2;
	}
	public int getEntityFireTicks() {
		return 200;
	}
	public float entityDamageMultiplier() {
		return 1.0F;
	}

	public int getEventID() {
		return 2137;
	}
	public int getEventData() {
		return 6;
	}

	public int getMinimalBiomeRadius() {
		return (int)Math.ceil(this.getBlastRadius() * 2.0F);
	}
	public int getNormalBiomeRadius() {
		return this.getBlastRadius();
	}
	public int getSevereBiomeRadius() {
		return (int)Math.ceil(this.getBlastRadius() * 0.5F);
	}
	public boolean brightensSky() {
		return true;
	}
	public double lightBeamMultiplier() {
		return this.getBlastRadius();
	}

	public boolean grantsAchievement() {
		return true;
	}

	public int getAffectedRange() {
		int blocks = Math.max(Math.max(this.getBlastRadius(), this.getNuclearRemainsRadius()), Math.max(this.getCharredTreesRadius(), this.getDestroyedLeavesRadius()));
		int biomes = Math.max(Math.max(this.getMinimalBiomeRadius(), this.getNormalBiomeRadius()), this.getSevereBiomeRadius());
		return Math.max(blocks, biomes);
	}
}
