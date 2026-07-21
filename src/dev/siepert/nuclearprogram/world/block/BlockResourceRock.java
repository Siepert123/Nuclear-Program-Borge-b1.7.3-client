package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;
import java.util.Random;

public class BlockResourceRock extends Block {
	public static final String[] VARIANTS = {
			"Hematite",
			"Malachite",
			"Bauxite",
			"DirtyCoal",
	};

	public static final int HEMATITE = 0;
	public static final int MALACHITE = 1;
	public static final int BAUXITE = 2;
	public static final int DIRTY_COAL = 3;

	public final Icon[] blockTextures = new Icon[VARIANTS.length];

	public BlockResourceRock(int blockID) {
		super(blockID, Material.rock);
	}

	@Override
	public Block disableStats() {
		return super.disableStats();
	}

	@Override
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < VARIANTS.length; i++) {
			this.blockTextures[i] = register.getTexture(this.getSimpleName() + VARIANTS[i], 16, 16);
		}
		this.blockTexture = Block.stone.blockTexture;
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTextures[meta];
	}

	@Override
	public int idDropped(int meta, Random random) {
		switch (meta) {
			case HEMATITE: return ItemInit.resourceBrickHematite.shiftedIndex;
			case MALACHITE: return ItemInit.resourceBrickMalachite.shiftedIndex;
			case BAUXITE: return ItemInit.resourceBrickBauxite.shiftedIndex;
			case DIRTY_COAL: return ItemInit.resourceBrickDirtyCoal.shiftedIndex;
			default: return 0;
		}
	}

	@Override
	public void getSubBlocks(Collection<ItemStack> items) {
		for (int i = 0; i < VARIANTS.length; i++) items.add(new ItemStack(this, 1, i));
	}
}
