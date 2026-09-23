package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.item.*;
import dev.siepert.nuclearprogram.world.reactor.FuelPebbleStats;
import dev.siepert.nuclearprogram.world.reactor.curve.ReactivityCurveLog10;
import dev.siepert.nuclearprogram.world.reactor.curve.ReactivityCurveSqrt;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemSeeds;
import net.minecraftborge.loader.event.register.IdAllocationEvent;

import java.util.function.IntFunction;

public class ItemInit {
	public static Item cokeCoal;
	public static Item cokePetroleum;

	public static Item ingotCopper;
	public static Item ingotAluminium;
	public static Item ingotLead;
	public static Item ingotTitanium;
	public static Item ingotTungsten;
	public static Item ingotSteel;
	public static Item ingotElectrum;
	public static Item ingotUranium;
	public static Item ingotUraniumLE;
	public static Item ingotUraniumME;
	public static Item ingotUraniumHE;
	public static Item ingotUranium233;
	public static Item ingotUranium235;
	public static Item ingotUranium238;
	public static Item ingotThorium;
	public static Item ingotKaupium;
	public static Item ingotYanoizedKaupium;

	public static Item nuggetUranium;
	public static Item nuggetUraniumLE;
	public static Item nuggetUraniumME;
	public static Item nuggetUraniumHE;
	public static Item nuggetUranium233;
	public static Item nuggetUranium235;
	public static Item nuggetUranium238;
	public static Item nuggetThorium;

	public static Item dustSulphur;
	public static Item dustSaltpeter;
	public static Item dustFluorite;
	public static Item dustRedPhosphorus;
	public static Item dustWhitePhosphorus;

	public static Item plateIron;
	public static Item plateGold;
	public static Item plateCopper;
	public static Item plateAluminium;
	public static Item plateLead;
	public static Item plateTitanium;
	public static Item plateTungsten;
	public static Item plateSteel;

	public static Item ballFireclay;
	public static Item firebrick;
	public static ItemConsumableSeeds potato;
	public static ItemFood potatoCooked;
	public static ItemSeeds hempSeeds;
	public static Item hempFibers;

	public static Item resourceBrickHematite;
	public static Item resourceBrickMalachite;
	public static Item resourceBrickBauxite;
	public static Item resourceBrickDirtyCoal;

	public static Item yellowcake;
	public static ItemFood uraniumSandwich;

	public static Item valve;

	public static ItemCraftingTool hammer;
	public static ItemCraftingTool cutters;
	public static Item screwdriver;
	public static ItemDetonator detonator;

	public static Item fuelRodEmpty;
	public static ItemFuelRod fuelRod;
	public static Item fuelRodArrayEmpty;
	public static ItemFuelRod fuelRodArray;

	public static ItemFuelPebbleSource pebbleSourceRa226Be;
	public static ItemFuelPebble pebbleFuelNU;
	public static ItemFuelPebble pebbleFuelMEU;
	public static ItemFuelPebble pebbleFuelHEU235;
	public static ItemFuelPebble pebbleFuelMEP239;

	public static Item fuelRodRbmkEmpty;

	public static ItemFluidIdentifier fluidIdentifier;
	public static ItemFluidRepresentation fluid;

	public static void register(IdAllocationEvent<Item> event) {
		Helper helper = new Helper(NuclearProgram.MODID, event);

		cokeCoal = helper.register("cokeCoal");
		cokePetroleum = helper.register("cokePetroleum");

		ingotCopper = helper.register("ingotCopper");
		ingotAluminium = helper.register("ingotAluminium");
		ingotLead = helper.register("ingotLead");
		ingotTitanium = helper.register("ingotTitanium");
		ingotTungsten = helper.register("ingotTungsten");
		ingotSteel = helper.register("ingotSteel");
		ingotElectrum = helper.register("ingotElectrum");
		ingotUranium = helper.register("ingotUranium");
		ingotUraniumLE = helper.register("ingotUraniumLE");
		ingotUraniumME = helper.register("ingotUraniumME");
		ingotUraniumHE = helper.register("ingotUraniumHE");
		ingotUranium233 = helper.register("ingotUranium233");
		ingotUranium235 = helper.register("ingotUranium235");
		ingotUranium238 = helper.register("ingotUranium238");
		ingotThorium = helper.register("ingotThorium");
		ingotKaupium = helper.register("ingotKaupium");
		ingotYanoizedKaupium = helper.register("ingotYanoizedKaupium");

		nuggetUranium = helper.register("nuggetUranium");
		nuggetUraniumLE = helper.register("nuggetUraniumLE");
		nuggetUraniumME = helper.register("nuggetUraniumME");
		nuggetUraniumHE = helper.register("nuggetUraniumHE");
		nuggetUranium233 = helper.register("nuggetUranium233");
		nuggetUranium235 = helper.register("nuggetUranium235");
		nuggetUranium238 = helper.register("nuggetUranium238");
		nuggetThorium = helper.register("nuggetThorium");

		dustSulphur = helper.register("dustSulphur");
		dustSaltpeter = helper.register("dustSaltpeter");
		dustFluorite = helper.register("dustFluorite");
		dustRedPhosphorus = helper.register("dustRedPhosphorus");
		dustWhitePhosphorus = helper.register("dustWhitePhosphorus", ItemWhitePhosphorus::new);

		plateIron = helper.register("plateIron");
		plateGold = helper.register("plateGold");
		plateCopper = helper.register("plateCopper");
		plateAluminium = helper.register("plateAluminium");
		plateLead = helper.register("plateLead");
		plateTitanium = helper.register("plateTitanium");
		plateTungsten = helper.register("plateTungsten");
		plateSteel = helper.register("plateSteel");

		ballFireclay = helper.register("ballFireclay");
		firebrick = helper.register("firebrick");
		potato = helper.register("potato", id -> new ItemConsumableSeeds(id, BlockInit.potatoes.blockID, 1, false));
		potatoCooked = helper.register("potatoCooked", id -> new ItemFood(id, 4, false).setMaxStackSize(64));
		hempSeeds = helper.register("hempSeeds", id -> new ItemSeeds(id, BlockInit.hemp.blockID));
		hempFibers = helper.register("hempFibers");

		resourceBrickHematite = helper.register("resourceBrickHematite");
		resourceBrickMalachite = helper.register("resourceBrickMalachite");
		resourceBrickBauxite = helper.register("resourceBrickBauxite");
		resourceBrickDirtyCoal = helper.register("resourceBrickDirtyCoal");

		yellowcake = helper.register("yellowcake");
		uraniumSandwich = helper.register("uraniumSandwich", id -> new ItemFood(id, 1000, false));

		valve = helper.register("valve");

		hammer = helper.register("hammer", id -> new ItemCraftingTool(id)
				.setMaxDamage(256)
		);
		cutters = helper.register("cutters", id -> new ItemCraftingTool(id)
				.setMaxDamage(256)
		);
		screwdriver = helper.register("screwdriver", id -> new ItemCraftingTool(id)
				.setMaxDamage(256)
		);
		detonator = helper.register("detonator", ItemDetonator::new);

		fuelRodEmpty = helper.register("fuelRodEmpty", Item::new);
		fuelRod = helper.register("fuelRod", ItemFuelRod::new);
		fuelRodArrayEmpty = helper.register("fuelRodArrayEmpty", Item::new);
		fuelRodArray = helper.register("fuelRodArray", ItemFuelRod::new);

		pebbleSourceRa226Be = helper.register("pebbleSourceRa226Be", id -> new ItemFuelPebbleSource(id, 20, 20*60));
		pebbleFuelNU = helper.register("pebbleFuelNU", id -> new ItemFuelPebble(id,
				new FuelPebbleStats(0.2F, new ReactivityCurveLog10(1.0F), 4096)
		));
		pebbleFuelMEU = helper.register("pebbleFuelMEU", id -> new ItemFuelPebble(id,
				new FuelPebbleStats(0.5F, new ReactivityCurveSqrt(1.0F, 1.0F), 4096*8)
		));
		pebbleFuelHEU235 = helper.register("pebbleFuelHEU235", id -> new ItemFuelPebble(id,
				new FuelPebbleStats(1.0F, new ReactivityCurveSqrt(1.0F, 2.0F), 4096*4)
		));
		pebbleFuelMEP239 = helper.register("pebbleFuelMEP239", id -> new ItemFuelPebble(id,
				new FuelPebbleStats(1.0F, new ReactivityCurveSqrt(2.0F, 1.0F), 4096*8)
		));

		fuelRodRbmkEmpty = helper.register("fuelRodRbmkEmpty", ItemFuelRodRBMK::new);

		fluidIdentifier = helper.register("fluidIdentifier", ItemFluidIdentifier::new);
		fluid = helper.register("fluid", ItemFluidRepresentation::new);
	}

	@SuppressWarnings("unchecked")
	static class Helper {
		private final String modid;
		private final IdAllocationEvent<Item> event;
		public Helper(String modid, IdAllocationEvent<Item> event) {
			this.modid = modid;
			this.event = event;
		}

		public <T extends Item> T register(String name, IntFunction<Item> sup) {
			T item = (T) this.event.createWithFreeId(sup);
			item.setItemName(this.modid + "/" + name);
			return item;
		}
		public Item register(String name) {
			return this.register(name, Item::new);
		}
	}
}
