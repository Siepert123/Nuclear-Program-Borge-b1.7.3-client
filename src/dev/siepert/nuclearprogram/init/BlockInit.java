package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.block.*;
import dev.siepert.nuclearprogram.world.item.*;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.StepSound;
import net.minecraftborge.loader.event.register.IdAllocationEvent;

import java.util.function.IntFunction;

public class BlockInit {
	public static final StepSoundPipe soundPipeFootstep = new StepSoundPipe(1.0F, 1.0F);
	public static final StepSound soundMetal2Footstep = new StepSound("metalStep", 1.0F, 1.0F);

	public static boolean available = false;

	public static BlockMetalOre oreMetal;
	public static BlockMetal blockMetal;
	public static BlockFireclay fireclay;
	public static Block firebricks;
	public static BlockWorkbench workbench;
	public static BlockFurnaceBuilder furnaceBuilderIdle;
	public static BlockFurnaceBuilder furnaceBuilderLit;
	public static BlockCropsPotato potatoes;
	public static BlockYanoDoor doorOffice;
	public static BlockYanoDoor doorBunker;
	public static Block concrete;
	public static Block concreteBrick;
	public static BlockConcreteColored concreteColored;
	public static BlockHatch hatch;
	public static BlockSealedDoor sealedDoor;

	public static BlockNukestone nukestone;
	public static BlockCharred charredWood;
	public static BlockNuke nukeNuclearCharge;
	public static BlockNuke nukeLittleBoy;
	public static BlockNuke nukeCaseoh;

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
		fireclay = helper.register("fireclay", id -> new BlockFireclay(id)
				.setHarvestLevel("shovel", 0)
				.setHardness(Block.blockClay.getHardness())
				.setResistance(Block.blockClay.getExplosionResistance(null))
				.setStepSound(Block.soundGravelFootstep)
		);
		firebricks = helper.register("firebricks", id -> new Block(id, Material.rock)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(Block.brick.getHardness())
				.setResistance(Block.brick.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
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
		doorOffice = helper.register("doorOffice", id -> new BlockYanoDoor(id, Material.wood)
				.setHarvestLevel("axe", 0)
				.setHardness(5.0F)
				.setResistance(15.0F)
				.setStepSound(Block.soundWoodFootstep)
		);
		doorBunker = helper.register("doorBunker", id -> new BlockYanoDoor(id, Material.iron)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(15.0F)
				.setResistance(256.0F)
				.setStepSound(soundMetal2Footstep)
		);
		concrete = helper.register("concrete", id -> new Block(id, NPMaterials.concrete)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(15.0F)
				.setResistance(256.0F)
				.setStepSound(Block.soundStoneFootstep)
		);
		concreteBrick = helper.register("concreteBrick", id -> new Block(id, NPMaterials.concrete)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(concrete.getHardness())
				.setResistance(concrete.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		concreteColored = helper.register("concrete", id -> new BlockConcreteColored(id)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(concrete.getHardness())
				.setResistance(concrete.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		hatch = helper.register("hatch", id -> new BlockHatch(id)
				.setHarvestLevel("pickaxe", 1)
				.setHardness(10.0F)
				.setResistance(128.0F)
				.setStepSound(soundMetal2Footstep)
		);
		sealedDoor = helper.register("sealedDoor", id -> new BlockSealedDoor(id)
				.setHarvestLevel("pickaxe", 1)
				.setHardness(10.0F)
				.setResistance(512.0F)
				.setStepSound(soundMetal2Footstep)
		);

		nukestone = helper.register("nukestone", id -> new BlockNukestone(id)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(Block.stone.getHardness())
				.setResistance(Block.stone.getExplosionResistance(null))
		);
		charredWood = helper.register("charredWood", id -> new BlockCharred(id)
				.setHarvestLevel("axe", 0)
				.setHardness(Block.wood.getHardness() / 2)
				.setResistance(Block.wood.getExplosionResistance(null) / 2)
				.setStepSound(Block.soundWoodFootstep)
		);
		nukeNuclearCharge = helper.register("nukeNuclearCharge", id -> new BlockNuclearCharge(id)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(10.0F)
				.setResistance(64.0F)
				.setStepSound(soundMetal2Footstep)
		);
		nukeLittleBoy = helper.register("nukeLittleBoy", id -> new BlockLittleBoy(id)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(10.0F)
				.setResistance(64.0F)
				.setStepSound(Block.soundMetalFootstep)
		);
		nukeCaseoh = helper.register("nukeCaseoh", id -> new BlockCaseoh(id)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(10.0F)
				.setResistance(64.0F)
				.setStepSound(Block.soundMetalFootstep)
		);

		available = true;

		Block.obsidian.setResistance(Block.stone.getExplosionResistance(null) * 3.0F);
	}

	public static void registerItemBlocks() {
		Item.itemsList[oreMetal.blockID] = new ItemBlockMetalOre(oreMetal);
		Item.itemsList[blockMetal.blockID] = new ItemBlockMetal(blockMetal);
		Item.itemsList[workbench.blockID] = new ItemBlockWorkbench(workbench);
		Item.itemsList[doorOffice.blockID] = new ItemBlockYanoDoor(doorOffice);
		Item.itemsList[doorBunker.blockID] = new ItemBlockYanoDoor(doorBunker);
		Item.itemsList[concreteColored.blockID] = new ItemBlockConcreteColored(concreteColored);
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
