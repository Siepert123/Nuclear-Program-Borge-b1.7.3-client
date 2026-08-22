package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.MultiblockHelper;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockInvisible;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.Icon;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockMulti extends BlockContainer {
	protected BlockMulti(int blockID, Material material) {
		super(blockID, material);

		// These are non-flagged multiblock slaves, and as such should not do anything of value
		this.disableTileEntity(0);
		this.disableTileEntity(1);
		this.disableTileEntity(2);
		this.disableTileEntity(3);
		this.disableTileEntity(4);
		this.disableTileEntity(5);
	}

	public static final int OFFSET = 10;
	public static final int FLAG = 6;
	public static boolean keepInventory = false;

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		super.onNeighborBlockChange(world, x, y, z, neighborBlockID);

		if (keepInventory) return;

		this.checkValidity(world, x, y, z);
	}

	private void checkValidity(World world, int x, int y, int z) {
		if (world.multiplayerWorld) return;
		int meta = world.getBlockMetadata(x, y, z);

		if (meta >= FLAG) meta -= FLAG;
		if (meta < 0 || meta > 5) return;

		EnumFacing dir = EnumFacing.VALUES[meta].getOpposite();
		int neighbour = world.getBlockId(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());

		if (neighbour != this.blockID && world.checkChunksExist(x - 1, 64, z - 1, x + 1, 64, z + 1)) {
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	public boolean findCore(IBlockAccess world, int x, int y, int z, int[] pos) {
		positionsIndex = 0;
		return this.findCoreImpl(world, x, y, z, pos);
	}

	static ChunkCoordinates reused = new ChunkCoordinates();
	static List<ChunkCoordinates> positions = new ArrayList<>();
	static int positionsIndex = 0;

	protected boolean findCoreImpl(IBlockAccess world, int x, int y, int z, int[] pos) {
		reused.x = x;
		reused.y = y;
		reused.z = z;

		int meta = world.getBlockMetadata(x, y, z);
		if (meta >= FLAG) meta -= FLAG;

		if (world.getBlockId(x, y, z) == this.blockID && (meta < 0 || meta > 5)) {
			pos[0] = x;
			pos[1] = y;
			pos[2] = z;
			return true;
		}

		for (int i = 0; i < positionsIndex; i++) {
			if (positions.get(i).equals(reused)) return false;
		}

		EnumFacing dir = EnumFacing.VALUES[meta].getOpposite();
		int block = world.getBlockId(x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
		if (block != this.blockID) return false;

		if (positions.size() == positionsIndex) positions.add(new ChunkCoordinates());
		ChunkCoordinates current = positions.get(positionsIndex);
		current.x = x;
		current.y = y;
		current.z = z;
		positionsIndex++;

		return this.findCoreImpl(world, x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), pos);
	}

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		keepInventory = true;
		world.setBlockMetadataWithNotify(x, y, z, side);
		keepInventory = false;
	}

	private static final int[] dims = new int[6];
	private static final int[] dimsRot = new int[6];
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		EnumFacing placedSide = EnumFacing.VALUES[world.getBlockMetadata(x, y, z)];

		keepInventory = true;
		world.setBlockWithNotify(x, y, z, 0);
		keepInventory = false;

		EnumFacing facingDir = EnumFacing.NORTH;

		if (!placedSide.isHorizontal()) {
			int rot = MathHelper.floor_float(placer.rotationYaw * 4.0F / 360.0F + 0.5F) & 3;
			switch (rot) {
				case 0:
					facingDir = EnumFacing.VALUES[2];
					break;
				case 1:
					facingDir = EnumFacing.VALUES[5];
					break;
				case 2:
					facingDir = EnumFacing.VALUES[3];
					break;
				case 3:
					facingDir = EnumFacing.VALUES[4];
					break;
			}
		} else {
			facingDir = placedSide;
		}

		EnumFacing facing = this.limitRotations(facingDir);

		int o = -this.getCoreOffset();

		int ox = x + facingDir.getOffsetX() * o;
		int oy = y + this.getCoreHeightOffset();
		int oz = z + facingDir.getOffsetZ() * o;

		this.getDimensions(dims);
		if (placedSide == EnumFacing.DOWN) {
			oy -= dims[0] + dims[1];
		} else if (placedSide != EnumFacing.UP) {
			MultiblockHelper.rotate(dims, placedSide, dimsRot);
			ox = x + placedSide.getOffsetX() * dimsRot[placedSide.getOpposite().getIndex()];
			oz = z + placedSide.getOffsetZ() * dimsRot[placedSide.getOpposite().getIndex()];
		}

		if (!this.checkRequirement(world, ox - facing.getOffsetX() * o, oy, oz - facing.getOffsetZ() * o, facing, o)) {
			if (placer instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) placer;
				ItemStack stack = player.inventory.mainInventory[player.inventory.currentItem];
				if (stack == null) {
					player.inventory.mainInventory[player.inventory.currentItem] = new ItemStack(this);
				} else {
					if (stack.itemID != this.blockID || stack.stackSize == stack.getMaxStackSize()) {
						player.inventory.addItemStackToInventory(new ItemStack(this));
					} else {
						stack.stackSize++;
					}
				}
			}
			return;
		}

		if (!world.multiplayerWorld) {
			int meta = this.modifyCoreMetadata(world, ox, oy, ox, placer, facing.getIndex() + OFFSET);
			world.setBlockAndMetadataWithNotify(ox, oy, oz, this.blockID, meta);
			this.fillSpace(world, ox - facing.getOffsetX() * o, oy, oz - facing.getOffsetZ() * o, facing, o);
		}

		super.onBlockPlacedBy(world, x, y, z, placer);
	}

	protected int modifyCoreMetadata(World world, int x, int y, int z, EntityLiving placer, int meta) {
		return meta;
	}
	protected EnumFacing limitRotations(EnumFacing facing) {
		return facing;
	}
	protected boolean checkRequirement(World world, int x, int y, int z, EnumFacing facing, int offset) {
		getDimensions(dims);
		return MultiblockHelper.isSpaceUnoccupied(world, x + facing.getOffsetX() * offset, y + facing.getOffsetY() * offset, z + facing.getOffsetZ() * offset, dims, facing);
	}
	protected void fillSpace(World world, int x, int y, int z, EnumFacing facing, int offset) {
		this.getDimensions(dims);
		MultiblockHelper.fill(world, x + facing.getOffsetX() * offset, y, z + facing.getOffsetZ() * offset, dims, this.blockID, facing);
	}

	protected final void setFlag(World world, int x, int y, int z) {
		if (world.getBlockId(x, y, z) != this.blockID) {
			System.err.print("Tried placing multiblock extra flag at non-self X:" + x + " Y:" + y + " Z:" + z);
			return;
		}

		int meta = world.getBlockMetadata(x, y, z);
		if (meta > 5) return;

		keepInventory = true;
		world.setBlockMetadataWithNotify(x, y, z, meta + FLAG);
		keepInventory = false;
	}

	public final boolean hasFlag(int meta) {
		return meta > 5 && meta < 12;
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta < 12 && !keepInventory) {
			int[] core = new int[3];
			if (this.findCore(world, x, y, z, core)) {
				world.setBlockWithNotify(core[0], core[1], core[2], 0);
			}
			if (meta >= FLAG) meta -= FLAG;
			EnumFacing facing = EnumFacing.VALUES[meta];

			if (world.getBlockId(x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ()) == this.blockID) {
				world.setBlockWithNotify(x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ(), 0);
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	@Override
	public int getRenderType() {
		return RenderBlockInvisible.RENDER_TYPE;
	}
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Writes multiblock dimensions to array.
	 * Ordered as UP-DOWN-FORWARD-BACKWARD-LEFT-RIGHT.
	 * @param dims The dimensions array to write to
	 */
	public abstract void getDimensions(int[] dims);
	public abstract int getCoreOffset();
	public int getCoreHeightOffset() {
		return 0;
	}

	@Override
	public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6) {
		this.dropBlockAsItem(var1, var3, var4, var5, var6);
	}

	public int getAllDimensionsCount() {
		return 1;
	}
	public void getAllDimensions(int[][] dims) {
		this.getDimensions(dims[0]);
	}

	@Override
	public Icon getBlockIcon(IBlockAccess world, int x, int y, int z, int side) {
		return BlockInit.blockMetal.blockTextures[BlockMetal.STEEL];
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		return this.blockTexture;
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		return this.getBlockIconFromSideAndMetadata(side, 0);
	}
}
