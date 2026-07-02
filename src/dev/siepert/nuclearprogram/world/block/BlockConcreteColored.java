package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraftborge.loader.DyeHelper;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

public class BlockConcreteColored extends Block {
	public final Icon[] blockTextures = new Icon[16];

	public BlockConcreteColored(int blockID) {
		super(blockID, NPMaterials.concrete);
	}

	@Override
	public String getRegistryName() {
		return this.getSimpleName() + "Colored";
	}

	@Override
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < 16; i++) {
			this.blockTextures[i] = register.getTexture(this.getSimpleName() + DyeHelper.COLOR_NAMES[i], 16, 16);
		}
		this.blockTexture = register.getTexture(this.getSimpleName(), 16, 16);
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
