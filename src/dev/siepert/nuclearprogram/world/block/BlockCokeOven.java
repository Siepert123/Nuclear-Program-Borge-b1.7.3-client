package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockCokeOven;
import dev.siepert.nuclearprogram.world.te.TileEntityCokeOven;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.IconRegister;

import java.util.List;

public class BlockCokeOven extends BlockMulti implements IOverlayInfo {
	public BlockCokeOven(int blockID, Material material) {
		super(blockID, material);

		this.flagDisableTE();
		this.flagEnableFluidConnection();
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		if (meta >= 12) return new TileEntityCokeOven();
		return null;
	}

	@Override
	public void getDimensions(int[] dims) {
		dims[0] = 4;
		dims[1] = 1;
		dims[2] = 1;
		dims[3] = 1;
		dims[4] = 0;
		dims[5] = 0;
	}

	@Override
	public int getCoreOffset() {
		return 1;
	}
	@Override
	public int getCoreHeightOffset() {
		return 1;
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, EnumFacing facing, int offset) {
		super.fillSpace(world, x, y, z, facing, offset);
		x += facing.getOffsetX() * offset;
		z += facing.getOffsetZ() * offset;

		this.setFlag(world, x - facing.getOffsetX(), y, z - facing.getOffsetZ());
	}

	private final int[] pos = new int[3];
	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.inventory.getCurrentItem() != null) return false;
		if (this.findCore(world, x, y, z, this.pos)) {
			if (!world.multiplayerWorld) {
				TileEntityCokeOven te = (TileEntityCokeOven) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
				te.inventory[0] = new ItemStack(Item.coal, 16);
			}
			return true;
		} else return false;
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		if (this.findCore(world, x, y, z, this.pos)) {
			TileEntityCokeOven te = (TileEntityCokeOven) world.getBlockTileEntity(this.pos[0], this.pos[1], this.pos[2]);
			information.add("Coal buffer: " + (te.inventory[0] != null ? te.inventory[0].stackSize : 0));
			colors.add(0xFFFFFF);
			information.add("Coke buffer: " + (te.inventory[1] != null ? te.inventory[1].stackSize : 0));
			colors.add(0xFFFFFF);
			information.add("Creosote buffer: " + te.tankCreosote + "mB/" + TileEntityCokeOven.TANK_CAPACITY + "mB");
			colors.add(0xFFFFFF);
			information.add("Progress: " + te.progress + "/200");
			colors.add(0xFFFFFF);
		} else {
			information.add("Core not found");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}

	@Override
	public int getRenderType() {
		return RenderBlockCokeOven.RENDER_TYPE;
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = BlockInit.blockMetal.blockTextures[BlockMetal.STEEL];
	}
}
