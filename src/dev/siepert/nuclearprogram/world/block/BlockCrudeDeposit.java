package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;

public class BlockCrudeDeposit extends Block {
	public final Icon[] blockTextures = new Icon[3];

	public static final String[] VARIANTS = {
			"Oil",
			"Gas",
			"Depleted"
	};

	public static final int OIL = 0;
	public static final int GAS = 1;
	public static final int DEPLETED = 2;

	public BlockCrudeDeposit(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTextures[0] = register.getTexture(this.getSimpleName() + "Oil", 16, 16);
		this.blockTextures[1] = register.getTexture(this.getSimpleName() + "Gas", 16, 16);
		this.blockTextures[2] = register.getTexture(this.getSimpleName() + "Depleted", 16, 16);

		this.blockTexture = this.blockTextures[0];
	}

	@Override
	protected int damageDropped(int meta) {
		return meta;
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTextures[meta];
	}

	@Override
	public void getSubBlocks(Collection<ItemStack> items) {
		items.add(new ItemStack(this, 1, OIL));
		items.add(new ItemStack(this, 1, GAS));
		items.add(new ItemStack(this, 1, DEPLETED));
	}
}
