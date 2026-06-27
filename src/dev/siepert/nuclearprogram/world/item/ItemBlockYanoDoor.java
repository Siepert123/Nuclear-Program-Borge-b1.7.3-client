package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockYanoDoor;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

public class ItemBlockYanoDoor extends ItemBlock {
	private final BlockYanoDoor block;
	public ItemBlockYanoDoor(BlockYanoDoor block) {
		super(block.blockID - 256);
		this.block = block;
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.itemTexture = register.getTexture(this.getSimpleName(), 16, 16);
	}

	@Override
	public Icon getTextureFromDamage(int damage) {
		return this.itemTexture;
	}

	public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
		if(var7 != 1) {
			return false;
		} else {
			++var5;
			Block block = this.block;

			if(!block.canPlaceBlockAt(var3, var4, var5, var6)) {
				return false;
			} else {
				int var9 = MathHelper.floor_double((double)((var2.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
				byte var10 = 0;
				byte var11 = 0;
				if(var9 == 0) {
					var11 = 1;
				}

				if(var9 == 1) {
					var10 = -1;
				}

				if(var9 == 2) {
					var11 = -1;
				}

				if(var9 == 3) {
					var10 = 1;
				}

				int var12 = (var3.isBlockNormalCube(var4 - var10, var5, var6 - var11) ? 1 : 0) + (var3.isBlockNormalCube(var4 - var10, var5 + 1, var6 - var11) ? 1 : 0);
				int var13 = (var3.isBlockNormalCube(var4 + var10, var5, var6 + var11) ? 1 : 0) + (var3.isBlockNormalCube(var4 + var10, var5 + 1, var6 + var11) ? 1 : 0);
				boolean var14 = var3.getBlockId(var4 - var10, var5, var6 - var11) == block.blockID || var3.getBlockId(var4 - var10, var5 + 1, var6 - var11) == block.blockID;
				boolean var15 = var3.getBlockId(var4 + var10, var5, var6 + var11) == block.blockID || var3.getBlockId(var4 + var10, var5 + 1, var6 + var11) == block.blockID;
				boolean var16 = false;
				if(var14 && !var15) {
					var16 = true;
				} else if(var13 > var12) {
					var16 = true;
				}

				if(var16) {
					var9 = var9 - 1 & 3;
					var9 += 4;
				}

				var3.editingBlocks = true;
				var3.setBlockAndMetadataWithNotify(var4, var5, var6, block.blockID, var9);
				var3.setBlockAndMetadataWithNotify(var4, var5 + 1, var6, block.blockID, var9 + 8);
				var3.editingBlocks = false;
				var3.notifyBlocksOfNeighborChange(var4, var5, var6, block.blockID);
				var3.notifyBlocksOfNeighborChange(var4, var5 + 1, var6, block.blockID);
				--var1.stackSize;
				return true;
			}
		}
	}
}
