package dev.siepert.nuclearprogram.util;

import net.minecraft.src.Block;

public class BlockProps {
	public static final float DIRT_HARDNESS = Block.dirt.getHardness();
	public static final float DIRT_RESISTANCE = Block.dirt.getExplosionResistance(null) * 5.0F / 3.0F;

	public static final float CLAY_HARDNESS = Block.blockClay.getHardness();
	public static final float CLAY_RESISTANCE = Block.blockClay.getExplosionResistance(null) * 5.0F / 3.0F;

	public static final float WOOD_HARDNESS = Block.wood.getHardness();
	public static final float WOOD_RESISTANCE = Block.wood.getExplosionResistance(null) * 5.0F / 3.0F;

	public static final float STONE_HARDNESS = Block.stone.getHardness();
	public static final float STONE_RESISTANCE = Block.stone.getExplosionResistance(null) * 5.0F / 3.0F;

	public static final float ORE_HARDNESS = Block.oreIron.getHardness();
	public static final float ORE_RESISTANCE = Block.oreIron.getExplosionResistance(null) * 5.0F / 3.0F;

	public static final float IRON_HARDNESS = Block.blockIron.getHardness();
	public static final float IRON_RESISTANCE = Block.blockIron.getExplosionResistance(null) * 5.0F / 3.0F;

	public static final float CONCRETE_HARDNESS = 15.0F;
	public static final float CONCRETE_RESISTANCE = 256.0F;
}
