package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistilleryController;
import dev.siepert.nuclearprogram.world.te.TileEntityOilDistillerySegment;
import net.minecraft.src.Material;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockOilDistillerySegment extends BlockMulti implements IOverlayInfo {
	public BlockOilDistillerySegment(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableFluidConnection();
		this.flagDisableTE();
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 1;
		dims[1] = 0;
		dims[2] = 1;
		dims[3] = 1;
		dims[4] = 1;
		dims[5] = 1;
	}

	@Override
	public int getCoreOffset() {
		return 1;
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, EnumFacing facing, int offset) {
		super.fillSpace(world, x, y, z, facing, offset);
		x += facing.getOffsetX() * offset;
		z += facing.getOffsetZ() * offset;

		this.setFlag(world, x+1, y, z);
		this.setFlag(world, x-1, y, z);
		this.setFlag(world, x, y, z+1);
		this.setFlag(world, x, y, z-1);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityOilDistillerySegment();
		return null;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		int i = 1;
		while (world.getBlockId(x, y-i, z) == this.blockID) i++;
		if (world.getBlockId(x, y-i, z) == BlockInit.oilDistilleryController.blockID && world.getBlockMetadata(x, y-i, z) >= 12) {
			TileEntityOilDistilleryController te = (TileEntityOilDistilleryController) world.getBlockTileEntity(x, y-i, z);
			if (te != null) te.cachedSegmentCount = -1;
		}
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		int i = 1;
		while (world.getBlockId(x, y-i, z) == this.blockID) i++;
		if (world.getBlockId(x, y-i, z) == BlockInit.oilDistilleryController.blockID && world.getBlockMetadata(x, y-i, z) >= 12) {
			TileEntityOilDistilleryController te = (TileEntityOilDistilleryController) world.getBlockTileEntity(x, y-i, z);
			if (te != null) te.cachedSegmentCount = -1;
		}
	}

	private final int[] pos = new int[3];
	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		if (this.hasFlag(world.getBlockMetadata(x, y, z))) {
			if (this.findCore(world, x, y, z, this.pos)) {
				TileEntityOilDistillerySegment te = (TileEntityOilDistillerySegment) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
				StringTranslate translate = StringTranslate.getInstance();
				information.add(translate.translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[te.fluidType]))
						+ ": " + te.tank + "mB/" + TileEntityOilDistillerySegment.TANK_CAPACITY + "mB");
				colors.add(0xFFFFFF);
			} else {
				information.add("Core not found");
				colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
			}
		}
	}
}
