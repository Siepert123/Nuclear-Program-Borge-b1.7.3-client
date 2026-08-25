package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.BlockProps;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityCreativeSupply;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import java.util.List;

public class BlockCreativeSupply extends BlockContainer implements IOverlayInfo, IFluidIdentifiable {
	public BlockCreativeSupply(int blockID) {
		super(blockID, NPMaterials.multiblock);
		this.setHarvestLevel("pickaxe", 1);
		this.setHardness(BlockProps.IRON_HARDNESS);
		this.setResistance(BlockProps.IRON_RESISTANCE);
		this.setStepSound(BlockInit.soundMetal2Footstep);

		BlockCable.enableConnection(blockID);
		BlockFluidPipe.enableConnection(blockID);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityCreativeSupply();
	}

	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		TileEntityCreativeSupply te = (TileEntityCreativeSupply) world.getBlockTileEntity(x, y, z);
		te.fluidType = fluidID;
		te.onInventoryChanged();
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		TileEntityCreativeSupply te = (TileEntityCreativeSupply) world.getBlockTileEntity(x, y, z);
		if (te.fluidType != 0) {
			information.add("Supplying " + Fluid.getLocalizedName(Fluid.fluidsList[te.fluidType]));
			colors.add(0x00FFFF);
		} else {
			information.add("Supplying Redstone Flux");
			colors.add(0x00FFFF);
		}
	}
}
