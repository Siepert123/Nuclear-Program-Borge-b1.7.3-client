package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.pipenet.node.PPNBasic;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockFluidPipe;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.IFilteredFluidConnection;
import dev.siepert.nuclearprogram.world.te.TileEntityFluidPipe;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.List;

public class BlockFluidPipe extends BlockContainer implements IOverlayInfo, IFluidIdentifiable {
	public static final boolean[] canConnectPipe = new boolean[blocksList.length];
	public static final int[] canConnectPipeMetaMask = new int[blocksList.length];
	public static final boolean[] hasFilteredFluids = new boolean[blocksList.length];

	public static boolean canConnectPipe(int block, int meta) {
		if (canConnectPipe[block]) {
			return (canConnectPipeMetaMask[block] & (1 << meta)) != 0;
		} else return false;
	}
	public static int getAssignedFluidType(IBlockAccess world, int x, int y, int z) {
		PipeNetNode node = PipeNet.getNode(x, y, z);
		return node != null ? node.fluidType : -1;
	}
	public static boolean getFilteredFluid(IBlockAccess world, int x, int y, int z, int block, int fluidID) {
		if (hasFilteredFluids[block]) {
			PipeNetNode node = PipeNet.getNode(x, y, z);
			if (node != null) return node.canConnect(fluidID);
		}
		return true;
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
	public static void enableFilteredFluids(int block) {
		hasFilteredFluids[block] = true;
	}

	public Icon blockTextureVertical;
	public Icon blockTextureHorizontal;

	public BlockFluidPipe(int blockID) {
		super(blockID, NPMaterials.pipe);

		isBlockContainerMetaMask[blockID] = 0;

		enableConnection(blockID);
		enableFilteredFluids(blockID);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float size = 0.25F;
		this.setBlockBounds(0.5F-size, 0.5F-size, 0.5F-size, 0.5F+size, 0.5F+size, 0.5F+size);
		int ox, oy, oz;
		int ob, om;
		int count = 0;
		int myFilter = getAssignedFluidType(world, x, y, z);
		int of;
		EnumFacing first = null;
		for (EnumFacing side : EnumFacing.VALUES) {
			ox = x + side.getOffsetX();
			oy = y + side.getOffsetY();
			oz = z + side.getOffsetZ();
			ob = world.getBlockId(ox, oy, oz);
			om = world.getBlockMetadata(ox, oy, oz);
			if (canConnectPipe(ob, om)) {
				if (BlockFluidPipe.getFilteredFluid(world, ox, oy, oz, ob, myFilter)) {
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

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		PipeNet.setNode(world, x, y, z, new PPNBasic().positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		PipeNet.setNode(world, x, y, z, null);
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

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		PipeNetNode node = PipeNet.getNode(x, y, z);
		if (node != null) {
			int fluidID = node.fluidType;
			StringTranslate translate = StringTranslate.getInstance();
			Fluid fluid = Fluid.fluidsList[fluidID];
			information.add(translate.translateNamedKey(Fluid.getUnlocalizedName(fluid)));
			colors.add(Fluid.colorLookup[fluidID]);
		} else {
			information.add("(no attached PipeNet node)");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}

	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		PipeNetNode node = PipeNet.getNode(x, y, z);
		if (node != null) {
			node.fluidType = fluidID;
			world.markBlockNeedsUpdate(x, y, z);
		}
	}
}
