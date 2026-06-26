package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.block.*;
import dev.siepert.nuclearprogram.world.item.ItemBlockMetal;
import dev.siepert.nuclearprogram.world.item.ItemBlockMetalOre;
import dev.siepert.nuclearprogram.world.item.ItemBlockWorkbench;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraftborge.loader.event.register.IdAllocationEvent;

import java.util.function.IntFunction;

public class BlockInit {
	public static boolean available = false;

	public static BlockMetalOre oreMetal;
	public static BlockMetal blockMetal;
	public static BlockWorkbench workbench;
	public static BlockFurnaceBuilder furnaceBuilderIdle;
	public static BlockFurnaceBuilder furnaceBuilderLit;
	public static BlockCropsPotato potatoes;

	public static void register(IdAllocationEvent<Block> event) {
		Helper helper = new Helper(NuclearProgram.MODID, event);

		oreMetal = helper.register("oreMetal", id -> new BlockMetalOre(id)
				.setHarvestLevel("pickaxe", 2)
				.setHarvestLevel("pickaxe", 1, BlockMetalOre.COPPER)
				.setHardness(Block.oreIron.getHardness())
				.setResistance(Block.oreIron.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		blockMetal = helper.register("blockMetal", id -> new BlockMetal(id)
				.setHarvestLevel("pickaxe", 2)
				.setHarvestLevel("pickaxe", 1, BlockMetal.COPPER)
				.setHardness(Block.blockIron.getHardness())
				.setResistance(Block.blockIron.getExplosionResistance(null))
				.setStepSound(Block.soundMetalFootstep)
		);
		workbench = helper.register("workbench", BlockWorkbench::new);
		furnaceBuilderIdle = helper.register("furnaceBuilder", id -> new BlockFurnaceBuilder(id, false)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(Block.stoneOvenIdle.getHardness())
				.setResistance(Block.stoneOvenIdle.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		furnaceBuilderLit = helper.register("furnaceBuilder", id -> new BlockFurnaceBuilder(id, true)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(Block.stoneOvenActive.getHardness())
				.setResistance(Block.stoneOvenActive.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		potatoes = helper.register("potatoes", id -> new BlockCropsPotato(id)
				.setHarvestLevel("hoe", -1)
				.setStepSound(Block.soundGrassFootstep)
		);

		available = true;
	}

	public static void registerItemBlocks() {
		Item.itemsList[oreMetal.blockID] = new ItemBlockMetalOre(oreMetal);
		Item.itemsList[blockMetal.blockID] = new ItemBlockMetal(blockMetal);
		Item.itemsList[workbench.blockID] = new ItemBlockWorkbench(workbench);
	}

	@SuppressWarnings("unchecked")
	static class Helper {
		private final String modid;
		private final IdAllocationEvent<Block> event;

		public Helper(String modid, IdAllocationEvent<Block> event) {
			this.modid = modid;
			this.event = event;
		}

		public <T extends Block> T register(String name, IntFunction<Block> sup) {
			T block = (T) this.event.createWithFreeId(sup);
			block.setBlockName(this.modid + "/" + name);
			return block;
		}
	}
}
