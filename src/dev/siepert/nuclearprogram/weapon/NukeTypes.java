package dev.siepert.nuclearprogram.weapon;

public class NukeTypes {
	public static final NukeType[] LIST = new NukeType[256];

	public static final NukeType CHARGE = new NukeTypeCharge(0);
	public static final NukeType LITTLE_BOY = new NukeTypeLittleBoy(1);
}
