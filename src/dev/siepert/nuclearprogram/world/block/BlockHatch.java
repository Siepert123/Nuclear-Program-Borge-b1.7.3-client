package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.block.render.RenderBlockHatch;
import dev.siepert.nuclearprogram.world.te.TileEntityHatch;
import net.minecraft.src.*;
import net.minecraftborge.loader.Side;

public class BlockHatch extends BlockContainer {
	private static boolean persistentTE = false;

	public BlockHatch(int blockID) {
		super(blockID, Material.iron);
		this.setLightOpacity(0);
		this.disableNeighborNotifyOnMetadataChange();
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 3.0F/16.0F, 1.0F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 3.0F/16.0F, 1.0F);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, facing);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		TileEntityHatch te = (TileEntityHatch) world.getBlockTileEntity(x, y, z);
		if (te != null && te.mayPass()) return null;
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	public static int getFacing(int meta) {
		switch (meta) {
			case 0:
				return Side.EAST;
			case 1:
				return Side.SOUTH;
			case 2:
				return Side.WEST;
			case 3:
				return Side.NORTH;
			default:
				return  0;
		}
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (!world.multiplayerWorld) {
			TileEntityHatch te = (TileEntityHatch) world.getBlockTileEntity(x, y, z);
			if (!te.canSwitchState()) return true;
			int meta = world.getBlockMetadata(x, y, z);
			boolean isOpen = (meta & 0b0100) != 0;
			persistentTE = true;
			world.setBlockMetadataWithNotify(x, y, z, meta ^ 0b0100);
			world.playEvent(2137, x, y, z, isOpen ? 3 : 2);
			te.setOpen(!isOpen);
			persistentTE = false;
		}
		return true;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityHatch();
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		if (!persistentTE) world.removeBlockTileEntity(x, y, z);
	}

	@Override
	public int getRenderType() {
		return RenderBlockHatch.RENDER_TYPE;
	}
}
