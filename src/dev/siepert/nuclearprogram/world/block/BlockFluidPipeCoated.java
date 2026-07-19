package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
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
		if (player.isSneaking()) return false;
		ItemStack held = player.inventory.getCurrentItem();
		if (ItemTags.isItemEmpty(held)) return false;
		if (held.itemID < 4096) {
			Block block = Block.blocksList[held.itemID];
			if (block != null && block != BlockInit.fluidPipeCoated && block.isOpaqueCube() && block.renderAsNormalBlock()) {
				int meta = held.getItem().getPlacedBlockMetadata(held.getItemDamage());
				TileEntityFluidPipeCoated te = (TileEntityFluidPipeCoated) world.getBlockTileEntity(x, y, z);
				if (te.modelBlockID != 0) return false;
				if (!world.multiplayerWorld) {
					te.modelBlockID = held.itemID;
					world.setBlockMetadata(x, y, z, meta);
					te.onInventoryChanged();
					world.markBlocksDirty(x, y, z, x, y, z);
				}
				return true;
			}
			return false;
		} else if (held.itemID == ItemInit.screwdriver.shiftedIndex) {
			if (!world.multiplayerWorld) {
				TileEntityFluidPipeCoated te = (TileEntityFluidPipeCoated) world.getBlockTileEntity(x, y, z);
				te.modelBlockID = 0;
				world.setBlockMetadata(x, y, z, 0);
				te.onInventoryChanged();
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
