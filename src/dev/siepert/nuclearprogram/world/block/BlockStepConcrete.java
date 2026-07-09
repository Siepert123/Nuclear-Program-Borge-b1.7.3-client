package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Collection;
import java.util.Random;

public class BlockStepConcrete extends Block {
	public static final String[] NAMES = new String[]{
			"Concrete",
			"ConcreteBrick",
			"ConcreteFoundation",
			"Null",
	};
	public final boolean isDouble;

	public final Icon[] blockTextures = new Icon[8];

	public BlockStepConcrete(int blockID, boolean isDouble) {
		super(blockID, NPMaterials.concrete);
		this.isDouble = isDouble;
		if(!isDouble) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}
		this.setLightOpacity(255);
	}

	@Override
	public String getRegistryName() {
		return this.isDouble ? this.getSimpleName() + "_double" : this.getSimpleName();
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTextures[0] = BlockInit.concrete.blockTexture;
		this.blockTextures[1] = BlockInit.concreteBrick.blockTexture;
		this.blockTextures[2] = BlockInit.concreteFoundation.blockTextureTop;
		this.blockTextures[3] = Block.bedrock.blockTexture;
		this.blockTextures[4] = BlockInit.concrete.blockTexture;
		this.blockTextures[5] = BlockInit.concreteBrick.blockTexture;
		this.blockTextures[6] = BlockInit.concreteFoundation.blockTexture;
		this.blockTextures[7] = Block.bedrock.blockTexture;

		this.blockTexture = this.blockTextures[0];
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTextures[meta & 3 + (side == Side.UP || side == Side.DOWN ? 0 : 4)];
	}

	@Override
	public void getSubBlocks(Collection<ItemStack> items) {
		if (this.isDouble) return;
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
		items.add(new ItemStack(this, 1, 2));
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
				world.setBlockAndMetadataWithNotify(x, y-1, z, this.blockID, meta);
			}
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return BlockInit.slabConcreteSingle.blockID;
	}

	@Override
	public int quantityDropped(Random random) {
		return this.isDouble ? 2 : 1;
	}

	@Override
	protected int damageDropped(int meta) {
		return meta & 3;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if (this.isDouble) return super.shouldSideBeRendered(world, x, y, z, side);
		return side == 1 || (super.shouldSideBeRendered(world, x, y, z, side) && (side == 0 || world.getBlockId(x, y, z) != this.blockID));
	}
}
