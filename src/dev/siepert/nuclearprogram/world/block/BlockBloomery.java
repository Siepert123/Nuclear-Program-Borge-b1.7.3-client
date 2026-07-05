package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockBloomery;
import dev.siepert.nuclearprogram.world.te.TileEntityBloomery;
import net.minecraft.src.*;
import net.minecraftborge.loader.ContainerUtil;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Random;

public class BlockBloomery extends BlockContainer {
	private final Random random = new Random();
	public final boolean lit;
	private static boolean keepFurnaceInventory = false;

	public Icon blockTextureSide, blockTextureTop;

	public BlockBloomery(int blockID, boolean lit) {
		super(blockID, Material.rock);
		this.lit = lit;
		this.disableNeighborNotifyOnMetadataChange();
		if (lit) this.setLightValue(1.0F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public String getRegistryName() {
		return this.lit ? this.getSimpleName() + "_lit" : this.getSimpleName();
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = register.getTexture(this.getSimpleName() + "_" + (this.lit ? "lit" : "idle"), 16, 16);
		this.blockTextureSide = register.getTexture(this.getSimpleName() + "_side", 16, 16);
		this.blockTextureTop = register.getTexture(this.getSimpleName() + "_top", 16, 16);
	}

	public int renderPass = 0;

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		if (side == Side.UP) return this.blockTextureTop;
		if (this.renderPass > 0) return this.blockTextureSide;
		if (side == Side.DOWN) return this.blockTextureSide;
		return (side == meta || meta == 0 && side == 3) ? this.blockTexture : this.blockTextureSide;
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		if (side == Side.UP) return this.blockTextureTop;
		return (side == 3) ? this.blockTexture : this.blockTextureSide;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if (side == Side.DOWN && this.renderPass > 0) return false;
		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	public static void updateFurnaceBlockState(World world, int x, int y, int z, boolean lit) {
		int meta = world.getBlockMetadata(x, y, z);
		TileEntity te = world.getBlockTileEntity(x, y, z);
		keepFurnaceInventory = true;
		world.setBlockAndMetadataWithNotify(x, y, z, lit ? BlockInit.bloomeryLit.blockID : BlockInit.bloomeryIdle.blockID, meta);
		keepFurnaceInventory = false;
		te.validate();
		world.setBlockTileEntity(x, y, z, te);
	}

	@Override
	protected TileEntity getBlockEntity() {
		return new TileEntityBloomery();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		final int meta;
		switch (facing) {
			case 0:
				meta = Side.EAST;
				break;
			case 1:
				meta = Side.SOUTH;
				break;
			case 2:
				meta = Side.WEST;
				break;
			case 3:
				meta = Side.NORTH;
				break;
			default:
				meta = 0;
				break;
		}

		world.setBlockMetadataWithNotify(x, y, z, meta);
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		if (!keepFurnaceInventory) {
			IInventory te = (IInventory) world.getBlockTileEntity(x, y, z);

			if (te != null) ContainerUtil.dropContents(world, x, y, z, te, this.random);
		}

		super.onBlockRemoval(world, x, y, z);
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (this.lit) {
			int facing = world.getBlockMetadata(x, y, z);
			float posX = (float) x + 0.5F;
			float posY = (float) y + 0.0F + random.nextFloat() * 0.5F;
			float posZ = (float) z + 0.5F;
			float shift = 0.52F;
			float position = random.nextFloat() * 0.5F - 0.25F;
			if(facing == Side.NEG_X) {
				world.spawnParticle("smoke", posX - shift, posY, posZ + position, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", posX - shift, posY, posZ + position, 0.0D, 0.0D, 0.0D);
			} else if(facing == Side.POS_X) {
				world.spawnParticle("smoke", posX + shift, posY, posZ + position, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", posX + shift, posY, posZ + position, 0.0D, 0.0D, 0.0D);
			} else if(facing == Side.NEG_Z) {
				world.spawnParticle("smoke", posX + position, posY, posZ - shift, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", posX + position, posY, posZ - shift, 0.0D, 0.0D, 0.0D);
			} else if(facing == Side.POS_Z) {
				world.spawnParticle("smoke", posX + position, posY, posZ + shift, 0.0D, 0.0D, 0.0D);
				world.spawnParticle("flame", posX + position, posY, posZ + shift, 0.0D, 0.0D, 0.0D);
			}
			if (random.nextFloat() < 0.25F) {
				world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "random.blastfurnace", 1.0F, 0.9F + random.nextFloat() * 0.2F);
			}
		}
	}

	@Override
	public int getRenderType() {
		return RenderBlockBloomery.RENDER_TYPE;
	}

	@Override
	public int idDropped(int meta, Random random) {
		return BlockInit.bloomeryIdle.blockID;
	}
}
