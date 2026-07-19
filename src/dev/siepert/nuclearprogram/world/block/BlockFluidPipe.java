package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.world.block.render.RenderBlockFluidPipe;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

public class BlockFluidPipe extends BlockContainer {
	public Icon blockTextureVertical;
	public Icon blockTextureHorizontal;

	public BlockFluidPipe(int blockID) {
		super(blockID, Material.iron);

		isBlockContainerMetaMask[blockID] = 0;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
