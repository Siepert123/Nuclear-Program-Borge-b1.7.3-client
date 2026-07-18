package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.block.render.RenderBlockRBMKColumn;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Random;

public class BlockRBMKColumn extends BlockContainer {
	public Icon blockTextureTop;

	public BlockRBMKColumn(int blockID) {
		super(blockID, Material.iron);
		isBlockContainerMetaMask[blockID] = 0;

		this.enableTileEntity(0);
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
		for (int i = 0; i < 4; i++) {
			int blockID = world.getBlockId(x, y+i, z);
			if (blockID != 0) return false;
		}
		return true;
	}

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		for (int i = 0; i < 4; i++) {
			world.setBlockAndMetadataWithNotify(x, y+i, z, this.blockID, i);
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return meta == 0 ? this.blockID : 0;
	}

	@Override
	public int getRenderType() {
		return RenderBlockRBMKColumn.RENDER_TYPE;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return null;
	}
}
