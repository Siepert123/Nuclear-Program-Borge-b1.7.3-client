package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNFlareBulb;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

public class BlockFlareBulb extends Block {
	public Icon blockTextureTop;

	public BlockFlareBulb(int blockID, Material material) {
		super(blockID, material);

		BlockFluidPipe.enableConnection(blockID);
		BlockFluidPipe.enableFilteredFluids(blockID);
		this.setLightOpacity(0);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		PipeNet.setNode(world, x, y, z, new PNNFlareBulb(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		PipeNet.setNode(world, x, y, z, null);
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		this.blockTextureTop = register.getTexture(this.getSimpleName() + "_top", 16, 16);
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		return (side == Side.UP || side == Side.DOWN) ? this.blockTextureTop : this.blockTexture;
	}
}
