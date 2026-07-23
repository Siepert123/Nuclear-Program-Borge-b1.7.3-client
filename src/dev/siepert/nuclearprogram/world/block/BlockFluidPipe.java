package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.block.render.RenderBlockFluidPipe;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

public class BlockFluidPipe extends BlockContainer {
	public static final boolean[] canConnectPipe = new boolean[blocksList.length];
	public static final int[] canConnectPipeMetaMask = new int[blocksList.length];

	public static boolean canConnectPipe(int block, int meta) {
		if (canConnectPipe[block]) {
			return (canConnectPipeMetaMask[block] & (1 << meta)) != 0;
		} else return false;
	}

	public static void enableConnection(int block, int meta) {
		canConnectPipe[block] = true;
		canConnectPipeMetaMask[block] |= (1 << meta);
	}
	public static void disableConnection(int block, int meta) {
		canConnectPipeMetaMask[block] &= ~(1 << meta);
		canConnectPipe[block] = canConnectPipeMetaMask[block] != 0;
	}

	public static void enableConnection(int block) {
		canConnectPipe[block] = true;
		canConnectPipeMetaMask[block] = 0xFFFF;
	}

	public Icon blockTextureVertical;
	public Icon blockTextureHorizontal;

	public BlockFluidPipe(int blockID) {
		super(blockID, NPMaterials.pipe);

		isBlockContainerMetaMask[blockID] = 0;

		enableConnection(blockID);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float size = 0.25F;
		this.setBlockBounds(0.5F-size, 0.5F-size, 0.5F-size, 0.5F+size, 0.5F+size, 0.5F+size);
		int ox, oy, oz;
		int count = 0;
		EnumFacing first = null;
		for (EnumFacing side : EnumFacing.VALUES) {
			ox = x + side.getOffsetX();
			oy = y + side.getOffsetY();
			oz = z + side.getOffsetZ();
			if (canConnectPipe(world.getBlockId(ox, oy, oz), world.getBlockMetadata(ox, oy, oz))) {
				if (first == null) first = side;
				count++;

				switch (side) {
					case UP:
						this.maxY = 1.0;
						break;
					case DOWN:
						this.minY = 0.0;
						break;
					case NORTH:
						this.minX = 0.0;
						break;
					case EAST:
						this.minZ = 0.0;
						break;
					case SOUTH:
						this.maxX = 1.0;
						break;
					case WEST:
						this.maxZ = 1.0;
						break;
				}
			}
		}

		if (count == 1) {
			EnumFacing side = first.getOpposite();
			switch (side) {
				case UP:
					this.maxY = 1.0;
					break;
				case DOWN:
					this.minY = 0.0;
					break;
				case NORTH:
					this.minX = 0.0;
					break;
				case EAST:
					this.minZ = 0.0;
					break;
				case SOUTH:
					this.maxX = 1.0;
					break;
				case WEST:
					this.maxZ = 1.0;
					break;
			}
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		this.blockTextureVertical = register.getTexture(this.getSimpleName() + "Vertical", 16, 16);
		this.blockTextureHorizontal = register.getTexture(this.getSimpleName() + "Horizontal", 16, 16);
	}

	public static int pass = 0;
	@Override
	public Icon getBlockIconFromSide(int side) {
		if (pass != 0) return this.blockTexture;
		else {
			if (axis == -1) return this.blockTexture;
			else {
				int beam = Side.getAxis(axis);
				switch (beam) {
					case Side.Y:
						if (side == Side.UP || side == Side.DOWN) return this.blockTexture;
						else return this.blockTextureVertical;
					case Side.X:
						if (side == Side.POS_X || side == Side.NEG_X) return this.blockTexture;
						else return this.blockTextureHorizontal;
					case Side.Z:
						if (side == Side.POS_Z || side == Side.NEG_Z) return this.blockTexture;
						else if (side == Side.UP || side == Side.DOWN) return this.blockTextureVertical;
						else return this.blockTextureHorizontal;
					default: return this.blockTexture;
				}
			}
		}
	}

	public static int axis = -1;

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return axis != side;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockFluidPipe.RENDER_TYPE;
	}
}
