package dev.siepert.nuclearprogram.world.block;

import net.minecraft.src.BlockDoor;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockYanoDoor extends BlockDoor {
	public BlockYanoDoor(int blockID, Material material) {
		super(blockID, material);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if(this.blockMaterial == Material.iron) {
			return true;
		} else {
			int var6 = world.getBlockMetadata(x, y, z);
			if((var6 & 8) != 0) {
				if(world.getBlockId(x, y - 1, z) == this.blockID) {
					this.blockActivated(world, x, y - 1, z, player);
				}

				return true;
			} else {
				if(world.getBlockId(x, y + 1, z) == this.blockID) {
					world.setBlockMetadataWithNotify(x, y + 1, z, (var6 ^ 4) + 8);
				}

				world.setBlockMetadataWithNotify(x, y, z, var6 ^ 4);
				world.markBlocksDirty(x, y - 1, z, x, y, z);
				world.playEvent(player, 2137, x, y, z, 1);
				return true;
			}
		}
	}

	public void onPoweredBlockChange(World var1, int var2, int var3, int var4, boolean var5) {
		int var6 = var1.getBlockMetadata(var2, var3, var4);
		if((var6 & 8) != 0) {
			if(var1.getBlockId(var2, var3 - 1, var4) == this.blockID) {
				this.onPoweredBlockChange(var1, var2, var3 - 1, var4, var5);
			}

		} else {
			boolean var7 = (var1.getBlockMetadata(var2, var3, var4) & 4) > 0;
			if(var7 != var5) {
				if(var1.getBlockId(var2, var3 + 1, var4) == this.blockID) {
					var1.setBlockMetadataWithNotify(var2, var3 + 1, var4, (var6 ^ 4) + 8);
				}

				var1.setBlockMetadataWithNotify(var2, var3, var4, var6 ^ 4);
				var1.markBlocksDirty(var2, var3 - 1, var4, var2, var3, var4);
				var1.playEvent(null, 2137, var2, var3, var4, 1);
			}
		}
	}
}
