package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockHSRFS;
import dev.siepert.nuclearprogram.world.te.TileEntityHSRFS;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.IconRegister;

public class BlockHSRFS extends BlockMulti {
	public BlockHSRFS(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityHSRFS();
		if (meta >= 6) return TileEntityProxy.create(true, true);
		return null;
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
	protected void fillSpace(World world, int x, int y, int z, EnumFacing facing, int offset) {
		super.fillSpace(world, x, y, z, facing, offset);
		x += facing.getOffsetX() * offset;
		z += facing.getOffsetZ() * offset;

		this.setFlag(world, x + facing.getOffsetZ() * 2, y, z + facing.getOffsetX() * 2);
		this.setFlag(world, x - facing.getOffsetZ() * 2, y, z - facing.getOffsetX() * 2);
	}

	private final int[] pos = new int[3];

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityHSRFS te = (TileEntityHSRFS) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			if (te.canSwitchState()) {
				te.setState(!te.open);
				return true;
			} else return false;
		} else return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockHSRFS.RENDER_TYPE;
	}
	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = BlockInit.blockMetal.blockTextures[BlockMetal.STEEL];
	}
}
