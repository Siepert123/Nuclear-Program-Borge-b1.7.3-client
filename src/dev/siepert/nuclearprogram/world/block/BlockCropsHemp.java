package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.*;

import java.util.Random;

public class BlockCropsHemp extends BlockCrops {
	public BlockCropsHemp(int blockID) {
		super(blockID, 0);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float size = 0.5F - 0.0625F;
		int meta = world.getBlockMetadata(x, y, z);
		if (meta < 7) {
			this.setBlockBounds(0.5F-size, 0.0F, 0.5F-size, 0.5F+size, (meta+1) * 0.125F, 0.5F+size);
		} else {
			this.setBlockBounds(0.5F-size, 0.0F, 0.5F-size, 0.5F+size, 1.0F, 0.5F+size);
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		this.func_268_h(world, x, y, z);
		if(world.getBlockLightValue(x, y + 1, z) >= 9) {
			int meta = world.getBlockMetadata(x, y, z);
			if(meta < 7) {
				float rate = this.getGrowthRate(world, x, y, z);
				if(random.nextInt((int)(100.0F / rate)) == 0) {
					++meta;
					world.setBlockMetadataWithNotify(x, y, z, meta);
				}
			} else if (world.getBlockId(x, y+1, z) == 0) {
				float rate = this.getGrowthRate(world, x, y, z);
				if(random.nextInt((int)(100.0F / rate)) == 0) {
					world.setBlockWithNotify(x, y+1, z, BlockInit.hempTop.blockID);
				}
			}
		}
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		ItemStack held = player.getCurrentEquippedItem();
		if (held == null) return false;
		if (held.itemID == Item.dyePowder.shiftedIndex && held.getItemDamage() == 15) {
			if (world.getBlockMetadata(x, y, z) < 7) {
				if (!world.multiplayerWorld) {
					this.fertilize(world, x, y, z);
					held.stackSize--;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance) {
		if(!world.multiplayerWorld) {
			int quantity = this.quantityDropped(meta, world.rand);

			for(int i = 0; i < quantity; ++i) {
				if(world.rand.nextFloat() < chance) {
					int id = this.idDropped(meta, world.rand);
					if(id > 0) {
						this.dropBlockAsItem_do(world, x, y, z, new ItemStack(id, 1, this.damageDropped(meta, world.rand)));
					}
				}
			}
		}
	}

	@Override
	public int idDropped(int meta, Random random) {
		return ItemInit.hempSeeds.shiftedIndex;
	}

	@Override
	public int quantityDropped(int meta, Random random) {
		return meta < 7 ? 1 : random.nextInt(3) + 2;
	}
}
