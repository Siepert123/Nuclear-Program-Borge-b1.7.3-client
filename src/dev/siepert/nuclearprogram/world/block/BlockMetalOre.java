package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

public class BlockMetalOre extends Block {
	public static final String[] VARIANTS = {
			"Copper",
			"Lead",
			"Titanium",
			"Tungsten"
	};

	public static final int COPPER = 0;

	public final Icon[] blockTextures = new Icon[VARIANTS.length];

	public BlockMetalOre(int blockID) {
		super(blockID, Material.rock);
	}

	@Override
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < VARIANTS.length; i++) {
			this.blockTextures[i] = register.getTexture(NuclearProgram.MODID + "/ore" + VARIANTS[i], 16, 16);
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
