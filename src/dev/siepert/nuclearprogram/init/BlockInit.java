package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.block.*;
import dev.siepert.nuclearprogram.world.block.BlockWorkbench;
import dev.siepert.nuclearprogram.world.item.*;
import net.minecraft.src.*;
import net.minecraftborge.loader.event.register.IdAllocationEvent;

import java.util.function.IntFunction;

public class BlockInit {
	public static final StepSoundPipe soundPipeFootstep = new StepSoundPipe(1.0F, 1.0F);
	public static final StepSound soundMetal2Footstep = new StepSound("metalStep", 1.0F, 1.0F);
	public static final StepSound soundChainFootstep = new StepSound("chain", 1.0F, 1.0F);

	public static boolean available = false;

	public static BlockMetalOre oreMetal;
	public static BlockMetal blockMetal;
	public static BlockFireclay fireclay;
	public static Block firebricks;
	public static BlockResourceRock resourceRock;
	public static BlockResourceRock resourceDeposit;

	public static BlockWorkbench workbench;
	public static BlockFurnaceBuilder furnaceBuilderIdle;
	public static BlockFurnaceBuilder furnaceBuilderLit;
	public static BlockCropsPotato potatoes;
	public static BlockCropsHemp hemp;
	public static BlockCropsHempTop hempTop;
	public static BlockYanoDoor doorOffice;
	public static BlockYanoDoor doorBunker;
	public static Block concrete;
	public static Block concreteBrick;
	public static BlockPillar concreteFoundation;
	public static BlockConcreteColored concreteColored;
	public static BlockStairs stairsConcrete;
	public static BlockStairs stairsConcreteBrick;
	public static BlockStairs stairsConcreteFoundation;
	public static BlockStepConcrete slabConcreteSingle;
	public static BlockStepConcrete slabConcreteDouble;
	public static BlockStepConcreteColored slabConcreteColoredSingle;
	public static BlockStepConcreteColored slabConcreteColoredDouble;
	public static BlockHatch hatch;
	public static BlockSealedDoor sealedDoor;
	public static BlockModulator modulator;

	public static BlockBloomery bloomeryIdle;
	public static BlockBloomery bloomeryLit;
	public static BlockBloomeryPipe bloomeryPipe;

	public static BlockFluidPipe fluidPipeCopper;
	public static BlockFluidPipeCoated fluidPipeCoated;

	public static BlockNukestone nukestone;
	public static BlockCharred charredWood;
	public static BlockFallout fallout;
	public static BlockNuke nukeNuclearCharge;
	public static BlockNuke nukeLittleBoy;
	public static BlockNuke nukeCaseoh;

	public static BlockExtractionTest extractionTest;

	public static BlockRBMKColumn rbmkBlank;
	public static BlockRBMKColumn rbmkBoiler;
	public static BlockRBMKColumn rbmkFuel;
	public static BlockRBMKColumn rbmkModerator;
	public static BlockRBMKColumn rbmkControl;
	public static BlockRBMKColumn rbmkAbsorber;
	public static BlockRBMKColumn rbmkReflector;

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
		resourceRock = helper.register("resourceRock", id -> new BlockResourceRock(id)
				.setHarvestLevel("pickaxe", 1)
				.setHardness(Block.oreIron.getHardness())
				.setResistance(Block.oreIron.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		resourceDeposit = helper.register("resourceDeposit", id -> new BlockResourceRock(id)
				.setBlockUnbreakable()
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
				.disableNeighborNotifyOnMetadataChange()
		);
		hemp = helper.register("hemp", id -> new BlockCropsHemp(id)
				.setHarvestLevel("hoe", -1)
				.setStepSound(Block.soundGrassFootstep)
				.disableNeighborNotifyOnMetadataChange()
		);
		hempTop = helper.register("hemp", id -> new BlockCropsHempTop(id)
				.setHarvestLevel("hoe", -1)
				.setStepSound(Block.soundGrassFootstep)
				.disableNeighborNotifyOnMetadataChange()
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
				.setResistance(128.0F)
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
		concreteFoundation = helper.register("concreteFoundation",
				id -> new BlockPillar(id, NPMaterials.concrete, NuclearProgram.path("concrete"), NuclearProgram.path("concreteFoundation"))
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
		stairsConcrete = helper.register("stairsConcrete", id -> new BlockStairs(id, concrete));
		stairsConcreteBrick = helper.register("stairsConcreteBrick", id -> new BlockStairs(id, concreteBrick));
		stairsConcreteFoundation = helper.register("stairsConcreteFoundation", id -> new BlockStairs(id, concreteFoundation));
		slabConcreteSingle = helper.register("slabConcrete", id -> new BlockStepConcrete(id, false)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(concrete.getHardness())
				.setResistance(concrete.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		slabConcreteDouble = helper.register("slabConcrete", id -> new BlockStepConcrete(id, true)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(concrete.getHardness())
				.setResistance(concrete.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		slabConcreteColoredSingle = helper.register("slabConcrete", id -> new BlockStepConcreteColored(id, false)
				.setHarvestLevel("pickaxe", 2)
				.setHardness(concrete.getHardness())
				.setResistance(concrete.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		slabConcreteColoredDouble = helper.register("slabConcrete", id -> new BlockStepConcreteColored(id, true)
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
		modulator = helper.register("modulator", id -> new BlockModulator(id, Material.iron)
				.setHarvestLevel("pickaxe", 1)
				.setHardness(5.0F)
				.setResistance(15.0F)
				.setStepSound(Block.soundMetalFootstep)
		);

		bloomeryIdle = helper.register("bloomery", id -> new BlockBloomery(id, false)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(Block.brick.getHardness())
				.setResistance(Block.brick.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		bloomeryLit = helper.register("bloomery", id -> new BlockBloomery(id, true)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(Block.brick.getHardness())
				.setResistance(Block.brick.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
		);
		bloomeryPipe = helper.register("bloomeryPipe", id -> new BlockBloomeryPipe(id)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(Block.brick.getHardness())
				.setResistance(Block.brick.getExplosionResistance(null))
				.setStepSound(Block.soundStoneFootstep)
				.disableResizeItem()
		);

		fluidPipeCopper = helper.register("fluidPipeCopper", id -> new BlockFluidPipe(id)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(2.5F)
				.setStepSound(soundMetal2Footstep)
		);
		fluidPipeCoated = helper.register("fluidPipeCoated", id -> new BlockFluidPipeCoated(id)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(2.5F)
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
		fallout = helper.register("fallout", id -> new BlockFallout(id)
				.setHarvestLevel("shovel", 0)
				.setHardness(Block.snow.getHardness())
				.setResistance(Block.snow.getExplosionResistance(null))
				.setStepSound(Block.soundGravelFootstep)
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

		extractionTest = helper.register("extractionTest", id -> new BlockExtractionTest(id, Material.iron)
				.setHarvestLevel("pickaxe", 0)
				.setHardness(1.0F)
				.setResistance(1.0F)
				.setStepSound(soundChainFootstep)
		);

		rbmkBlank = helper.register("rbmkBlank", BlockRBMKColumn::new);
		rbmkBoiler = helper.register("rbmkBoiler", BlockRBMKColumn::new);
		rbmkFuel = helper.register("rbmkFuel", BlockRBMKColumn::new);
		rbmkModerator = helper.register("rbmkModerator", BlockRBMKColumn::new);
		rbmkControl = helper.register("rbmkControl", BlockRBMKColumn::new);
		rbmkAbsorber = helper.register("rbmkAbsorber", BlockRBMKColumn::new);
		rbmkReflector = helper.register("rbmkReflector", BlockRBMKColumn::new);

		available = true;

		Block.obsidian.setResistance(Block.stone.getExplosionResistance(null) * 3.0F);
	}

	public static void registerItemBlocks() {
		Item.itemsList[oreMetal.blockID] = new ItemBlockMetalOre(oreMetal);
		Item.itemsList[blockMetal.blockID] = new ItemBlockMetal(blockMetal);
		Item.itemsList[resourceRock.blockID] = new ItemBlockResourceRock(resourceRock);
		Item.itemsList[resourceDeposit.blockID] = new ItemBlockResourceRock(resourceDeposit);
		Item.itemsList[workbench.blockID] = new ItemBlockWorkbench(workbench);
		Item.itemsList[doorOffice.blockID] = new ItemBlockYanoDoor(doorOffice);
		Item.itemsList[doorBunker.blockID] = new ItemBlockYanoDoor(doorBunker);
		Item.itemsList[concreteColored.blockID] = new ItemBlockConcreteColored(concreteColored);
		Item.itemsList[slabConcreteSingle.blockID] = new ItemBlockStepConcrete(slabConcreteSingle);
		Item.itemsList[slabConcreteDouble.blockID] = new ItemBlockStepConcrete(slabConcreteDouble);
		Item.itemsList[slabConcreteColoredSingle.blockID] = new ItemBlockStepConcreteColored(slabConcreteColoredSingle);
		Item.itemsList[slabConcreteColoredDouble.blockID] = new ItemBlockStepConcreteColored(slabConcreteColoredDouble);
		Item.itemsList[rbmkBlank.blockID] = new ItemBlockRBMKColumn(rbmkBlank);
		Item.itemsList[rbmkBoiler.blockID] = new ItemBlockRBMKColumn(rbmkBoiler);
		Item.itemsList[rbmkFuel.blockID] = new ItemBlockRBMKColumn(rbmkFuel);
		Item.itemsList[rbmkModerator.blockID] = new ItemBlockRBMKColumn(rbmkModerator);
		Item.itemsList[rbmkControl.blockID] = new ItemBlockRBMKColumn(rbmkControl);
		Item.itemsList[rbmkAbsorber.blockID] = new ItemBlockRBMKColumn(rbmkAbsorber);
		Item.itemsList[rbmkReflector.blockID] = new ItemBlockRBMKColumn(rbmkReflector);
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
