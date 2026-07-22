package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Random;

public class BlockDustOre extends Block {
	public static final String[] VARIANTS = {
			"Sulphur",
			"Saltpeter",
			"Fluorite",
	};

	public static final int SULPHUR = 0;
	public static final int SALTPETER = 1;
	public static final int FLUORITE = 2;

	public Icon[] blockTextures = new Icon[VARIANTS.length];

	public BlockDustOre(int blockID) {
		super(blockID, Material.rock);
	}

	@Override
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < VARIANTS.length; i++) {
			this.blockTextures[i] = register.getTexture(NuclearProgram.path("ore" + VARIANTS[i]), 16, 16);
		}
		this.blockTexture = this.blockTextures[0];
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTextures[meta];
	}

	@Override
	public int idDropped(int meta, Random random) {
		switch (meta) {
			case SULPHUR:
				return ItemInit.dustSulphur.shiftedIndex;
			case SALTPETER:
				return ItemInit.dustSaltpeter.shiftedIndex;
			case FLUORITE:
				return ItemInit.dustFluorite.shiftedIndex;
			default:
				return -1;
		}
	}

	@Override
	public int quantityDropped(int meta, Random random) {
		switch (meta) {
			case SULPHUR:
			case FLUORITE:
				return 2 + random.nextInt(4);
			case SALTPETER:
				return 4 + random.nextInt(3);
			default:
				return this.quantityDropped(random);
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 4 + random.nextInt(5);
	}
}
