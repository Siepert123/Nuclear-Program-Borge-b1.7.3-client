package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockRBMKColumn;
import dev.siepert.nuclearprogram.world.te.TileEntityRBMKColumn;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Random;
import java.util.function.Supplier;

public class BlockRBMKColumn extends BlockContainer {
	protected static boolean preventColumnRemoval = false;

	public Icon blockTextureTop;
	public final Supplier<TileEntityRBMKColumn> tileentity;

	public BlockRBMKColumn(int blockID) {
		this(blockID, null);
		throw new AssertionError("Legacy constructor");
	}
	public BlockRBMKColumn(int blockID, Supplier<TileEntityRBMKColumn> ctor) {
		super(blockID, Material.iron);
		this.setHarvestLevel("pickaxe", 2);
		this.setHardness(5.0F);
		this.setStepSound(BlockInit.soundMetal2Footstep);

		isBlockContainerMetaMask[blockID] = 0;
		this.enableTileEntity(0);

		this.tileentity = ctor;
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = register.getTexture(this.getSimpleName() + "_side", 16, 16);
		this.blockTextureTop = register.getTexture(this.getSimpleName() + "_top", 16, 16);
	}

	@Override
	public Icon getBlockIcon(IBlockAccess world, int x, int y, int z, int side) {
		return this.getBlockIconFromSide(side);
	}
	@Override
	public Icon getBlockIconFromSide(int side) {
		return side == Side.UP || side == Side.DOWN ? this.blockTextureTop : this.blockTexture;
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		for (int i = 0; i < 7; i++) {
			int blockID = world.getBlockId(x, y+i, z);
			if (blockID != 0) return false;
		}
		return true;
	}

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		for (int i = 1; i < 7; i++) {
			world.setBlockAndMetadataWithNotify(x, y+i, z, this.blockID, i);
		}
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta > 0) {
			if (world.getBlockId(x, y-1, z) != this.blockID || world.getBlockMetadata(x, y-1, z) != meta - 1) return false;
		}
		if (meta < 6) {
			return world.getBlockId(x, y + 1, z) == this.blockID && world.getBlockMetadata(x, y + 1, z) == meta + 1;
		}
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		int meta = world.getBlockMetadata(x, y, z);
		if (!this.canBlockStay(world, x, y, z)) {
			world.setBlockWithNotify(x, y, z, 0);
			this.dropBlockAsItem(world, x, y, z, meta);
		} else if (meta == 0) {
			((TileEntityRBMKColumn)world.getBlockTileEntity(x, y, z)).updateNeighbourColumns();
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return meta == 0 ? this.blockID : 0;
	}

	private static final boolean USE_CUSTOM_ITEM_RENDERER = true;
	@Override
	public int getRenderType() {
		return USE_CUSTOM_ITEM_RENDERER ? RenderBlockRBMKColumn.RENDER_TYPE : 0;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return meta == 0 ? this.tileentity.get() : null;
	}
}
