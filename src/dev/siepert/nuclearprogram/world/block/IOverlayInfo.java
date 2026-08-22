package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.util.collect.IntList;
import net.minecraft.src.World;

import java.util.List;

public interface IOverlayInfo {
	void addInformation(World world, int x, int y, int z, List<String> information, IntList colors);
}
