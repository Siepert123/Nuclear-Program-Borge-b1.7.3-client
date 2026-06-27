package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.World;

public interface IDetonateBehaviour {
	void detonate(World world, int x, int y, int z);
}
