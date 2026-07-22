package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.MapColor;
import net.minecraft.src.Material;

public class NPMaterials {
	public static final Material cable = new Material(MapColor.stoneColor).setImmovableMobility();
	public static final Material pipe = new Material(MapColor.stoneColor).setImmovableMobility();
	public static final Material charred = new Material(MapColor.stoneColor);
	public static final Material concrete = new Material(MapColor.stoneColor).setImmovableMobility().setRequiresCorrectTool();
	public static final Material fallout = new Material(MapColor.snowColor).setIsTranslucent().setReplaceable().setNoPushMobility();
}
