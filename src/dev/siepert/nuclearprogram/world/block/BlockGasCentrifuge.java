package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.gui.GuiGasCentrifuge;
import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockGasCentrifuge;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockInvisible;
import dev.siepert.nuclearprogram.world.te.TileEntityGasCentrifuge;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.ContainerUtil;
import net.minecraftborge.loader.IconRegister;

import java.util.Random;

public class BlockGasCentrifuge extends BlockContainer {
	private final Random random = new Random();

	public BlockGasCentrifuge(int blockID) {
		super(blockID, NPMaterials.multiblock);

		BlockCable.enableConnection(blockID);
		BlockFluidPipe.enableConnection(blockID);

		this.setLightOpacity(0);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = BlockInit.blockMetal.blockTextures[BlockMetal.STEEL];
	}

	@Override
	public void onBlockPlaced(World world, int x, int y, int z, int side) {
		for (int i = 1; i < 4; i++) {
			world.setBlock(x, y+i, z, BlockInit.centrifugeExtension.blockID);
		}
		this.onNeighborBlockChange(world, x, y, z, this.blockID);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
		int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		world.setBlockMetadata(x, y, z, facing);
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		IInventory te = (IInventory) world.getBlockTileEntity(x, y, z);
		if (te != null) ContainerUtil.dropContents(world, x, y, z, te, this.random);

		super.onBlockRemoval(world, x, y, z);

		if (world.getBlockId(x, y+1, z) == BlockInit.centrifugeExtension.blockID) world.setBlockWithNotify(x, y+1, z, 0);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		TileEntityGasCentrifuge te = (TileEntityGasCentrifuge) world.getBlockTileEntity(x, y, z);
		if (te != null) te.updateEnrichmentStatus();
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		for (int i = 0; i < 4; i++) {
			if (world.getBlockId(x, y+i, z) != 0) return false;
		}
		return true;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (!world.multiplayerWorld) {
			TileEntityGasCentrifuge te = (TileEntityGasCentrifuge) world.getBlockTileEntity(x, y, z);
			Minecraft.getTheMinecraft().displayGuiScreen(new GuiGasCentrifuge(player.inventory, te));
		}
		return true;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityGasCentrifuge();
	}

	@Override
	public int getRenderType() {
		return RenderBlockInvisible.RENDER_TYPE;
	}
}
