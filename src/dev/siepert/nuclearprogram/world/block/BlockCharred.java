package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.Random;

public class BlockCharred extends Block {
	public Icon blockTextureLog, blockTextureLogTop;

	public BlockCharred(int blockID) {
		super(blockID, NPMaterials.charred);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = register.getTexture(NuclearProgram.path("planksCharred"), 16, 16);
		this.blockTextureLog = register.getTexture(NuclearProgram.path("logCharred"), 16, 16);
		this.blockTextureLogTop = register.getTexture(NuclearProgram.path("logCharredTop"), 16, 16);
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		if (meta != 0) return this.blockTexture;
		return side == Side.UP || side == Side.DOWN ? this.blockTextureLogTop : this.blockTextureLog;
	}

	@Override
	public int idDropped(int meta, Random random) {
		return Item.coal.shiftedIndex;
	}

	@Override
	protected int damageDropped(int meta) {
		return 1;
	}

	@Override
	public int quantityDropped(int meta, Random random) {
		if (meta == 0) return 1;
		return random.nextInt(4) == 0 ? 1 : 0;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		this.tryToFall(world, x, y, z);
	}

	@Override
	public int tickRate() {
		return 3;
	}

	private void tryToFall(World world, int x, int y, int z) {
		if(BlockSand.canFallBelow(world, x, y - 1, z) && y >= 0) {
			byte var8 = 32;
			if(!BlockSand.fallInstantly && world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
				EntityFallingSand entity = new EntityFallingSand(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.blockID);
				entity.blockMeta = world.getBlockMetadata(x, y, z);
				world.entityJoinedWorld(entity);
			} else {
				world.setBlockWithNotify(x, y, z, 0);

				while(BlockSand.canFallBelow(world, x, y - 1, z) && y > 0) {
					--y;
				}

				if(y > 0) {
					world.setBlockWithNotify(x, y, z, this.blockID);
				}
			}
		}
	}
}
