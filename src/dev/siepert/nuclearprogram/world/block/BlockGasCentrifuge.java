package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockGasCentrifuge;
import dev.siepert.nuclearprogram.world.te.TileEntityGasCentrifuge;
import net.minecraft.src.*;
import net.minecraftborge.loader.ContainerUtil;
import net.minecraftborge.loader.IconRegister;

import java.util.Random;

public class BlockGasCentrifuge extends BlockContainer {
	private final Random random = new Random();

	public BlockGasCentrifuge(int blockID) {
		super(blockID, Material.iron);

		BlockCable.enableConnection(blockID);
		BlockFluidPipe.enableConnection(blockID);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = Block.blockIron.blockTexture;
	}

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		for (int i = 1; i < 4; i++) {
			world.setBlock(x, y+i, z, BlockInit.centrifugeExtension.blockID);
		}
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		IInventory te = (IInventory) world.getBlockTileEntity(x, y, z);
		if (te != null) ContainerUtil.dropContents(world, x, y, z, te, this.random);

		super.onBlockRemoval(world, x, y, z);

		if (world.getBlockId(x, y+1, z) == BlockInit.centrifugeExtension.blockID) world.setBlockWithNotify(x, y+1, z, 0);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		for (int i = 0; i < 4; i++) {
			if (world.getBlockId(x, y+i, z) != 0) return false;
		}
		return true;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityGasCentrifuge();
	}

	@Override
	public int getRenderType() {
		return RenderBlockGasCentrifuge.RENDER_TYPE;
	}
}
