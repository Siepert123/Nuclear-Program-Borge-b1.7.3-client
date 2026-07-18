package dev.siepert.nuclearprogram.util;

import net.minecraft.src.*;
import net.minecraftborge.loader.WorldMapper;

public class SingletonWorld extends World {
	public static final SingletonWorld INSTANCE = new SingletonWorld();
	public static SingletonWorld get(int blockID, int metadata) {
		INSTANCE.blockID = blockID;
		INSTANCE.metadata = metadata;
		return INSTANCE;
	}
	public static World or(World world, int blockID, int metadata) {
		if (world != null) return world;
		else return get(blockID, metadata);
	}

	public int blockID = 0;
	public int metadata = 0;

	public SingletonWorld() {
		super(new SaveHandlerMP(), "singleton", 2137L);

		ChunkLoader.mapper = WorldMapper.IDENTITY;
	}

	@Override
	protected IChunkProvider getChunkProvider() {
		return null;
	}

	@Override
	protected void getInitialSpawnLocation() {
		this.worldInfo.setSpawn(0, 0, 0);
	}

	@Override
	public void calculateInitialSkylight() {

	}

	@Override
	public void tick() {

	}

	@Override
	public int getBlockId(int x, int y, int z) {
		if (x != 0 || y != 0 || z != 0) return 0;
		else return this.metadata;
	}

	@Override
	public TileEntity getBlockTileEntity(int x, int y, int z) {
		return null;
	}

	@Override
	public float getBrightness(int x, int y, int z, int minimum) {
		return 1.0F;
	}

	@Override
	public float getLightBrightness(int x, int y, int z) {
		return 1.0F;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z) {
		if (x != 0 || y != 0 || z != 0) return 0;
		else return this.metadata;
	}

	@Override
	public Material getBlockMaterial(int x, int y, int z) {
		if (x != 0 || y != 0 || z != 0) return Material.air;
		else {
			Block block = Block.blocksList[this.blockID];
			return block != null ? block.blockMaterial : Material.air;
		}
	}

	@Override
	public boolean isBlockOpaqueCube(int x, int y, int z) {
		if (x != 0 || y != 0 || z != 0) return false;
		else {
			Block block = Block.blocksList[this.blockID];
			return block != null && block.isOpaqueCube();
		}
	}

	@Override
	public boolean isBlockNormalCube(int x, int y, int z) {
		if (x != 0 || y != 0 || z != 0) return false;
		else {
			Block block = Block.blocksList[this.blockID];
			return block != null && block.isOpaqueCube() && block.renderAsNormalBlock();
		}
	}
}
