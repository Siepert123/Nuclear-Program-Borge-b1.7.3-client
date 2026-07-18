package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.world.block.BlockYanoDoor;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

public class ItemBlockYanoDoor extends ItemBlock {
	private final BlockYanoDoor block;
	public ItemBlockYanoDoor(BlockYanoDoor block) {
		super(block.blockID - 4096);
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

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side) {
		if(side != 1) {
			return false;
		} else {
			++y;
			Block block = this.block;

			if(!block.canPlaceBlockAt(world, x, y, z)) {
				return false;
			} else {
				int facing = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
				byte var10 = 0;
				byte var11 = 0;
				if(facing == 0) {
					var11 = 1;
				}

				if(facing == 1) {
					var10 = -1;
				}

				if(facing == 2) {
					var11 = -1;
				}

				if(facing == 3) {
					var10 = 1;
				}

				int var12 = (world.isBlockNormalCube(x - var10, y, z - var11) ? 1 : 0) + (world.isBlockNormalCube(x - var10, y + 1, z - var11) ? 1 : 0);
				int var13 = (world.isBlockNormalCube(x + var10, y, z + var11) ? 1 : 0) + (world.isBlockNormalCube(x + var10, y + 1, z + var11) ? 1 : 0);
				boolean var14 = world.getBlockId(x - var10, y, z - var11) == block.blockID || world.getBlockId(x - var10, y + 1, z - var11) == block.blockID;
				boolean var15 = world.getBlockId(x + var10, y, z + var11) == block.blockID || world.getBlockId(x + var10, y + 1, z + var11) == block.blockID;
				boolean var16 = false;
				if(var14 && !var15) {
					var16 = true;
				} else if(var13 > var12) {
					var16 = true;
				}

				if(var16) {
					facing = facing - 1 & 3;
					facing += 4;
				}

				world.editingBlocks = true;
				world.setBlockAndMetadataWithNotify(x, y, z, block.blockID, facing);
				world.setBlockAndMetadataWithNotify(x, y + 1, z, block.blockID, facing + 8);
				world.editingBlocks = false;
				world.notifyBlocksOfNeighborChange(x, y, z, block.blockID);
				world.notifyBlocksOfNeighborChange(x, y + 1, z, block.blockID);
				world.playSoundEffect(x + 0.5, y + 1.0, z + 0.5,
						block.stepSound.getPlaceSound(),
						(block.stepSound.getVolume() + 1.0F) * 0.5F,
						block.stepSound.getPitch() * 0.8F
				);
				--stack.stackSize;
				return true;
			}
		}
	}
}
