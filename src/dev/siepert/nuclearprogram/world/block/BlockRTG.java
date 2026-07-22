package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.gui.GuiRTG;
import dev.siepert.nuclearprogram.world.te.TileEntityRTG;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.ContainerUtil;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Random;

public class BlockRTG extends BlockContainer {
	private final Random random = new Random();

	public Icon blockTextureSide, blockTextureTop, blockTextureBottom;

	public BlockRTG(int blockID) {
		super(blockID, Material.iron);

		BlockCable.enableConnection(blockID);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = register.getTexture(this.getSimpleName(), 16, 16);
		this.blockTextureSide = register.getTexture(this.getSimpleName() + "_side", 16, 16);
		this.blockTextureBottom = register.getTexture(this.getSimpleName() + "_bottom", 16, 16);
		this.blockTextureTop = register.getTexture(this.getSimpleName() + "_top", 16, 16);
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		if (side == Side.DOWN) return this.blockTextureBottom;
		if (side == Side.UP) return this.blockTextureTop;
		return (side == meta || meta == 0 && side == 3) ? this.blockTexture : this.blockTextureSide;
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		if (side == Side.DOWN) return this.blockTextureBottom;
		if (side == Side.UP) return this.blockTextureTop;
		return (side == 3) ? this.blockTexture : this.blockTextureSide;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (!world.multiplayerWorld && player instanceof EntityPlayerSP) {
			TileEntityRTG te = (TileEntityRTG) world.getBlockTileEntity(x, y, z);
			Minecraft.getTheMinecraft().displayGuiScreen(new GuiRTG(player.inventory, te));
		}
		return true;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityRTG();
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
		IInventory te = (IInventory) world.getBlockTileEntity(x, y, z);
		if (te != null) ContainerUtil.dropContents(world, x, y, z, te, this.random);

		super.onBlockRemoval(world, x, y, z);
	}
}
