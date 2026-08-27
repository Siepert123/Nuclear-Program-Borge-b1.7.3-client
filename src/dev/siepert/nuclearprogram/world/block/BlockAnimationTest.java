package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.te.TileEntityAnimationTest;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;

import java.util.List;

public class BlockAnimationTest extends BlockMulti implements IOverlayInfo {
	public BlockAnimationTest(int blockID, Material material) {
		super(blockID, material);

		this.flagEnableEnergyConnection();
		this.flagEnableFluidConnection();
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 2;
		dims[1] = 0;
		dims[2] = 2;
		dims[3] = 2;
		dims[4] = 2;
		dims[5] = 2;
	}

	@Override
	public int getCoreOffset() {
		return 2;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityAnimationTest();
		if (meta >= 6) return TileEntityProxy.create(true, true);
		return null;
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, EnumFacing facing, int offset) {
		super.fillSpace(world, x, y, z, facing, offset);
		x += facing.getOffsetX() * offset;
		z += facing.getOffsetZ() * offset;

		this.setFlag(world, x + 2, y, z);
		this.setFlag(world, x - 2, y, z);
	}

	private final int[] pos = new int[3];
	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (this.findCore(world, x, y, z, this.pos)) {
			return ((TileEntityAnimationTest)world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2])).toggle();
		} else return false;
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityAnimationTest te = (TileEntityAnimationTest) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			information.add("animationTicks: " + te.animationTicks);
			colors.add(0xFFFFFF);
			information.add("isOpen: " + te.isOpen);
			colors.add(0xFFFFFF);
			information.add("soundLoop: " + te.soundLoop);
			colors.add(0xFFFFFF);
			information.add("rotation: " + te.rotation);
			colors.add(0xFFFFFF);
		} else {
			information.add("Core not found");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}
}
