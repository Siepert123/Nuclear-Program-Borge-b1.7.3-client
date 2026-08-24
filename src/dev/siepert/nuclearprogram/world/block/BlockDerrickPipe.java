package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.te.TileEntityDerrick;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Random;

public class BlockDerrickPipe extends Block {
	public Icon blockTextureTop;

	public BlockDerrickPipe(int blockID, Material material) {
		super(blockID, material);

		this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		this.blockTextureTop = register.getTexture(this.getSimpleName() + "_top", 16, 16);
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		return side == Side.UP || side == Side.DOWN ? this.blockTextureTop : this.blockTexture;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	}
	@Override
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	}

	@Override
	public int idDropped(int meta, Random random) {
		return 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		for (int i = y+1; i < 128; i++) {
			int block = world.getBlockId(x, i, z);
			if (block == BlockInit.derrick.blockID) {
				TileEntity te = world.getBlockTileEntity(x, i, z);
				if (te instanceof TileEntityDerrick) {
					((TileEntityDerrick)te).cachedDrillDepth = -1;
				}
				return;
			} else if (block != this.blockID) return;
		}
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		for (int i = y+1; i < 128; i++) {
			int block = world.getBlockId(x, i, z);
			if (block == BlockInit.derrick.blockID) {
				TileEntity te = world.getBlockTileEntity(x, i, z);
				if (te instanceof TileEntityDerrick) {
					((TileEntityDerrick)te).cachedDrillDepth = -1;
				}
				return;
			} else if (block != this.blockID) return;
		}
	}
}
