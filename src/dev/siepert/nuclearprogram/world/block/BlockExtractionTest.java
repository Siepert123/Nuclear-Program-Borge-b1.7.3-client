package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.capability.CapabilityItemHandler;
import net.minecraftborge.loader.capability.IItemHandler;

import java.util.Random;

public class BlockExtractionTest extends Block {
	public BlockExtractionTest(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.blockID, 5);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockId(x, 0, z) == BlockInit.resourceDeposit.blockID) {
			int type = world.getBlockMetadata(x, 0, z);
			for (EnumFacing side : EnumFacing.VALUES) {
				int ox = side.getOffsetX() + x;
				int oy = side.getOffsetY() + y;
				int oz = side.getOffsetZ() + z;

				TileEntity te = world.getBlockTileEntity(ox, oy, oz);
				if (te != null && te.hasCapability(CapabilityItemHandler.CAPABILITY, side.getOpposite())) {
					IItemHandler inv = te.getCapability(CapabilityItemHandler.CAPABILITY, side.getOpposite());
					if (inv != null) {
						ItemStack stack = new ItemStack(BlockInit.resourceRock.idDropped(type, random), 1, BlockInit.resourceRock.damageDropped(type, random));
						for (int i = 0; (i < inv.getSlots() && stack != null); i++) {
							stack = inv.insertItem(i, stack, false);
							if (stack != null && stack.stackSize <= 0) stack = null;
						}
					}
					break;
				}
			}

			world.scheduleBlockUpdate(x, y, z, this.blockID, 5);
		}
	}
}
