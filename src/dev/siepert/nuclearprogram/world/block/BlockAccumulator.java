package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.node.CNNBasicReceiver;
import dev.siepert.nuclearprogram.util.NumFormat;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.te.TileEntityAccumulator;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Collection;
import java.util.List;

public class BlockAccumulator extends BlockContainer implements IOverlayInfo {
	private static final int VARIANTS = 2;
	private static final long[] ENERGIES = new long[VARIANTS];

	public Icon[] blockTextures = new Icon[VARIANTS];
	public Icon[] blockTexturesTop = new Icon[VARIANTS];
	public Icon[] blockTexturesBottom = new Icon[VARIANTS];

	public BlockAccumulator(int blockID, Material material) {
		super(blockID, material);

		BlockCable.enableConnection(blockID);

		ENERGIES[0] = 1_000_000L;
		ENERGIES[1] = 16_000_000L;
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityAccumulator(ENERGIES[meta]);
	}

	@Override
	public void registerIcons(IconRegister register) {
		for (int i = 0; i < VARIANTS; i++) {
			this.blockTextures[i] = register.getTexture(this.getSimpleName() + i, 16, 16);
			this.blockTexturesTop[i] = register.getTexture(this.getSimpleName() + i + "_top", 16, 16);
			this.blockTexturesBottom[i] = register.getTexture(this.getSimpleName() + i + "_bottom", 16, 16);
		}

		this.blockTexture = this.blockTextures[0];
	}
	@Override
	public Icon getBlockIconFromSide(int side) {
		return this.getBlockIconFromSideAndMetadata(side, 0);
	}
	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		if (side == Side.UP) return this.blockTexturesTop[meta];
		if (side == Side.DOWN) return this.blockTexturesBottom[meta];
		return this.blockTextures[meta];
	}

	@Override
	public void getSubBlocks(Collection<ItemStack> items) {
		for (int i = 0; i < VARIANTS; i++) items.add(new ItemStack(this, 1, i));
	}
	@Override
	protected int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);

		CableNet.setNode(world, x, y, z, new CNNBasicReceiver(world).positioned(x, y, z));
	}
	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);

		CableNet.setNode(world, x, y, z, null);
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		TileEntityAccumulator te = (TileEntityAccumulator) world.getBlockTileEntity(x, y, z);
		information.add(NumFormat.format(te.energy) + "/" + NumFormat.format(te.maxEnergy) + "RF (" + NumFormat.percentage(te.energy, te.maxEnergy) + ")");
		colors.add(0xFFFFFF);
	}
}
