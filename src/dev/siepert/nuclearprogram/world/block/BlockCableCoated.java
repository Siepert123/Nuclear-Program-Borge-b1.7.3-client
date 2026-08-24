package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import dev.siepert.nuclearprogram.cablenet.node.CNNBasic;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockCableCoated;
import dev.siepert.nuclearprogram.world.te.TileEntityCableCoated;
import net.minecraft.src.*;
import net.minecraftborge.loader.tag.ItemTags;

import java.util.List;

public class BlockCableCoated extends BlockContainer implements IOverlayInfo {
	public BlockCableCoated(int blockID) {
		super(blockID, NPMaterials.cable);

		BlockCable.canConnectCable[blockID] = true;
		BlockCable.canConnectCableMetaMask[blockID] = 0xFFFF;
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
				TileEntityCableCoated te = (TileEntityCableCoated) world.getBlockTileEntity(x, y, z);
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
				TileEntityCableCoated te = (TileEntityCableCoated) world.getBlockTileEntity(x, y, z);
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
		return new TileEntityCableCoated();
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		CableNet.setNode(world, x, y, z, new CNNBasic(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		CableNet.setNode(world, x, y, z, null);
	}

	@Override
	public int getRenderType() {
		return RenderBlockCableCoated.RENDER_TYPE;
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		CableNetNode node = CableNet.getNode(x, y, z);
		if (node != null) {
			information.add("Network ID: " + node.network);
			colors.add(0x00FFFF);
		} else {
			information.add("(no attached CableNet node)");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}
}
