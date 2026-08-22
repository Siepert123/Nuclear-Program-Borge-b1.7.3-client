package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.World;

public interface IFluidIdentifiable {
	void setFluidID(World world, int x, int y, int z, int fluidID);
}
