package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

public class BlockMetal extends Block {
	public static final String[] VARIANTS = {
			"Copper",
			"Aluminium",
			"Lead",
			"Titanium",
			"Tungsten",
			"Steel",
			"Electrum",
			"Kaupium",
			"YanoizedKaupium",
	};

	public static final int COPPER = 0;
	public static final int ALUMINIUM = 1;
	public static final int LEAD = 2;
	public static final int TITANIUM = 3;
	public static final int TUNGSTEN = 4;
	public static final int STEEL = 5;
	public static final int ELECTRUM = 6;
	public static final int KAUPIUM = 7;
	public static final int YANOIZED_KAUPIUM = 8;

	public final Icon[] blockTextures = new Icon[VARIANTS.length];

	public BlockMetal(int blockID) {
		super(blockID, Material.iron);
	}

	@Override
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < VARIANTS.length; i++) {
			this.blockTextures[i] = register.getTexture(NuclearProgram.MODID + "/block" + VARIANTS[i], 16, 16);
		}
		this.blockTexture = this.blockTextures[0];
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTextures[meta];
	}

	@Override
	protected int damageDropped(int meta) {
		return meta;
	}
}
