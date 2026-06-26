package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.*;

import java.util.Random;

public class BlockCropsPotato extends BlockCrops {
	public BlockCropsPotato(int blockID) {
		super(blockID, 0);
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
		return ItemInit.potato.shiftedIndex;
	}

	@Override
	public int quantityDropped(int meta, Random random) {
		return meta < 7 ? 1 : random.nextInt(3) + 2;
	}
}
