package dev.objlib.api;

import net.minecraft.src.Tessellator;

public interface IObjModel {
	int[] getRawData();
	int getRenderList();
	void tessellate(Tessellator tes);
}
