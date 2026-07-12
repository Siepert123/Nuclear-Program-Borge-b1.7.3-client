package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;
import java.util.Random;

public class BlockStepConcreteColored extends Block {
	public final boolean isDouble;

	public final Icon[] blockTextures = new Icon[16];

	public BlockStepConcreteColored(int blockID, boolean isDouble) {
		super(blockID, NPMaterials.concrete);
		this.isDouble = isDouble;
		if(!isDouble) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			this.setUseAboveLight(true);
			this.disableResizeItem();
		}
		this.setLightOpacity(255);
	}

	@Override
	public String getRegistryName() {
		return this.isDouble ? this.getSimpleName() + "Colored_double" : this.getSimpleName() + "Colored";
	}

	@Override
	public void registerIcons(IconRegister register) {
		System.arraycopy(BlockInit.concreteColored.blockTextures, 0, this.blockTextures, 0, 16);
		this.blockTexture = BlockInit.concreteColored.blockTexture;
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTextures[meta];
	}

	@Override
	public void getSubBlocks(Collection<ItemStack> items) {
		if (this.isDouble) return;
		for (int i = 0; i < 16; i++) items.add(new ItemStack(this, 1, i));
	}

	@Override
	public boolean isOpaqueCube() {
		return this.isDouble;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return this.isDouble;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if (this.isDouble) super.onBlockAdded(world, x, y, z);
		else {
			int blockBelow = world.getBlockId(x, y-1, z);
			int meta = world.getBlockMetadata(x, y, z);
			int metaBelow = world.getBlockMetadata(x, y-1, z);
			if (blockBelow == this.blockID && metaBelow == meta) {
				world.setBlockWithNotify(x, y, z, 0);
				world.setBlockAndMetadataWithNotify(x, y-1, z, BlockInit.slabConcreteColoredDouble.blockID, meta);
			}
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return BlockInit.slabConcreteColoredSingle.blockID;
	}

	@Override
	public int quantityDropped(Random random) {
		return this.isDouble ? 2 : 1;
	}

	@Override
	protected int damageDropped(int meta) {
		return meta;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if (this.isDouble) return super.shouldSideBeRendered(world, x, y, z, side);
		return side == 1 || (super.shouldSideBeRendered(world, x, y, z, side) && (side == 0 || world.getBlockId(x, y, z) != this.blockID));
	}
}
