package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.util.IconSubpartStretch;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockSealedDoor;
import dev.siepert.nuclearprogram.world.te.TileEntitySealedDoor;
import net.minecraft.src.*;
import net.minecraftborge.loader.IGraphicsListener;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

public class BlockSealedDoor extends BlockContainer implements IGraphicsListener {
	public Icon blockTexturePart;
	public Icon blockTextureFrame, blockTextureFrameSide, blockTextureDoor;
	public Icon blockTextureDoorFront, blockTextureDoorSide;

	public BlockSealedDoor(int blockID) {
		super(blockID, Material.iron);
		this.setLightOpacity(0);
		this.disableNeighborNotifyOnMetadataChange();
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		this.blockTexturePart = register.getTexture(this.getSimpleName() + "Part", 48, 80);
		this.blockTextureFrame = IconSubpartStretch.create(this.blockTexturePart, 48, 80, 0, 0, 40, 72);
		this.blockTextureFrameSide = IconSubpartStretch.create(this.blockTexturePart, 48, 80, 45, 1, 2, 2);
		this.blockTextureDoor = register.getTexture(this.getSimpleName() + "Door", 64, 64);
		this.blockTextureDoorFront = IconSubpartStretch.create(this.blockTextureDoor, 64, 64, 0, 0, 32, 64);
		this.blockTextureDoorSide = IconSubpartStretch.create(this.blockTextureDoor, 64, 64, 32, 0, 32, 64);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		final float minX, maxX, minZ, maxZ;
		switch (meta & 0b0011) {
			case 0:
				minX = 0.0F;
				maxX = 1.0F;
				minZ = 0.0F;
				maxZ = 0.5F;
				break;
			case 1:
				minX = 0.5F;
				maxX = 1.0F;
				minZ = 0.0F;
				maxZ = 1.0F;
				break;
			case 2:
				minX = 0.0F;
				maxX = 1.0F;
				minZ = 0.5F;
				maxZ = 1.0F;
				break;
			case 3:
				minX = 0.0F;
				maxX = 0.5F;
				minZ = 0.0F;
				maxZ = 1.0F;
				break;
			default:
				minX = 0.0F;
				maxX = 1.0F;
				minZ = 0.0F;
				maxZ = 1.0F;
		}
		this.setBlockBounds(minX, 0.0F, minZ, maxX, 1.0F, maxZ);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		TileEntitySealedDoor te = this.getController(world, x, y, z);
		if (te != null && te.mayPass()) return null;
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	public int pass = 0;
	@Override
	public Icon getBlockIconFromSide(int side) {
		switch (this.pass) {
			case 1: return side == Side.NEG_Z || side == Side.POS_Z ? this.blockTextureDoorFront : this.blockTextureDoorSide;
			case 2: return side == Side.NEG_Z || side == Side.POS_Z ? this.blockTextureFrame : this.blockTextureFrameSide;
			case 3: return side == Side.NEG_X || side == Side.POS_X ? this.blockTextureFrame : this.blockTextureFrameSide;
			default: return this.blockTexture;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, facing);
		world.setBlockAndMetadataWithNotify(x, y+1, z, this.blockID, facing | 0b1000);
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
	public void onBlockRemoval(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (isUpperPart(metadata)) {
			world.setBlock(x, y-1, z, 0);
		} else {
			world.setBlock(x, y+1, z, 0);
		}

		super.onBlockRemoval(world, x, y, z);
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking()) return false;
		if (!world.multiplayerWorld) {
			TileEntitySealedDoor te = this.getController(world, x, y, z);
			if (te == null) return true;
			if (!te.canSwitchState()) return true;
			int meta = world.getBlockMetadata(x, y, z);
			boolean open = isOpen(meta);
			world.setBlockMetadataWithNotify(x, y, z, meta ^ 0b0100);
			world.setBlockMetadataWithNotify(x, isUpperPart(meta) ? y-1 : y+1, z, ((meta & 0b0111) ^ 0b0100) | (world.getBlockMetadata(x, isUpperPart(meta) ? y-1 : y+1, z)) & 0b1000);
			world.playEvent(2137, x, isUpperPart(meta) ? y-1 : y, z, open ? 5 : 4);
			te.setOpen(!open);
		}
		return true;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntitySealedDoor();
	}

	public TileEntitySealedDoor getController(IBlockAccess world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (isUpperPart(metadata)) return (TileEntitySealedDoor) world.getBlockTileEntity(x, y-1, z);
		else return (TileEntitySealedDoor) world.getBlockTileEntity(x, y, z);
	}
	public boolean isOpen(IBlockAccess world, int x, int y, int z) {
		TileEntitySealedDoor te = this.getController(world, x, y, z);
		if (te != null) return te.mayPass();
		return isOpen(world.getBlockMetadata(x, y, z));
	}

	@Override
	public int getRenderType() {
		return RenderBlockSealedDoor.RENDER_TYPE;
	}

	public static boolean isUpperPart(int meta) {
		return (meta & 0b1000) != 0;
	}
	public static boolean isOpen(int meta) {
		return (meta & 0b0100) != 0;
	}

	@Override
	public void setGraphicsFancy(boolean fancy) {
		RenderBlockSealedDoor.INSTANCE.doRender3DItem = fancy;
	}
}
