package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.MapColor;
import net.minecraft.src.Material;

public class NPMaterials {
	public static final Material charred = new Material(MapColor.stoneColor);
	public static final Material concrete = new Material(MapColor.stoneColor).setImmovableMobility().setRequiresCorrectTool();
}
