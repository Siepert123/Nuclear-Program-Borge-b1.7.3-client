package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

public class BlockPillar extends Block {
	private final String nameTop, nameSide;

	public Icon blockTextureTop;

	public BlockPillar(int blockID, Material material, String nameTop, String nameSide) {
		super(blockID, material);
		this.nameTop = nameTop;
		this.nameSide = nameSide;
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = register.getTexture(this.nameSide, 16, 16);
		this.blockTextureTop = register.getTexture(this.nameTop, 16, 16);
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		return side == Side.UP || side == Side.DOWN ? this.blockTextureTop : this.blockTexture;
	}
}
