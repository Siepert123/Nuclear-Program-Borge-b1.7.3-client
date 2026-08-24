package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.pipenet.node.PNNBasic;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockFluidPipeCoated;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityFluidPipeCoated;
import net.minecraft.src.*;
import net.minecraftborge.loader.tag.ItemTags;

import java.util.List;

public class BlockFluidPipeCoated extends BlockContainer implements IOverlayInfo, IFluidIdentifiable {
	public BlockFluidPipeCoated(int blockID) {
		super(blockID, NPMaterials.pipe);

		BlockFluidPipe.enableConnection(blockID);
		BlockFluidPipe.enableFilteredFluids(blockID);
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
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		PipeNet.setNode(world, x, y, z, new PNNBasic(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		PipeNet.setNode(world, x, y, z, null);
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		PipeNetNode node = PipeNet.getNode(x, y, z);
		if (node != null) {
			int fluidID = node.fluidType;
			StringTranslate translate = StringTranslate.getInstance();
			Fluid fluid = Fluid.fluidsList[fluidID];
			information.add(translate.translateNamedKey(Fluid.getUnlocalizedName(fluid)));
			colors.add(Fluid.colorLookup[fluidID]);
			information.add("Network ID: " + node.network);
			colors.add(0x00FFFF);
		} else {
			information.add("(no attached PipeNet node)");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}

	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		PipeNetNode node = PipeNet.getNode(x, y, z);
		if (node != null && node.fluidType != fluidID) {
			node.fluidType = fluidID;
			world.markBlockNeedsUpdate(x, y, z);
			PipeNet.getData(world).invalidateAround(node);
		}
	}

	@Override
	public int getRenderType() {
		return RenderBlockFluidPipeCoated.RENDER_TYPE;
	}
}
