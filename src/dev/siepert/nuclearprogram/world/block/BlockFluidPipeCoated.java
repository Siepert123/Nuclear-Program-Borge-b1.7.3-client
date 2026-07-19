package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.block.render.RenderBlockFluidPipeCoated;
import dev.siepert.nuclearprogram.world.te.TileEntityFluidPipeCoated;
import net.minecraft.src.*;
import net.minecraftborge.loader.tag.ItemTags;

public class BlockFluidPipeCoated extends BlockContainer {
	public BlockFluidPipeCoated(int blockID) {
		super(blockID, Material.iron);

		BlockFluidPipe.canConnectPipe[blockID] = true;
		BlockFluidPipe.canConnectPipeMetaMask[blockID] = 0xFFFF;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		ItemStack held = player.inventory.getCurrentItem();
		if (ItemTags.isItemEmpty(held)) return false;
		if (held.itemID < 4096 && Block.blocksList[held.itemID].renderAsNormalBlock()) {
			if (!world.multiplayerWorld) {
				TileEntityFluidPipeCoated te = (TileEntityFluidPipeCoated) world.getBlockTileEntity(x, y, z);
				te.modelBlockID = held.itemID;
				world.markBlocksDirty(x, y, z, x, y, z);
			}
			return true;
		} else return false;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityFluidPipeCoated();
	}

	@Override
	public int getRenderType() {
		return RenderBlockFluidPipeCoated.RENDER_TYPE;
	}
}
