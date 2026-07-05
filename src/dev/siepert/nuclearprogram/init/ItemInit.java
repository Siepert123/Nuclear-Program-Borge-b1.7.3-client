package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.item.ItemConsumableSeeds;
import dev.siepert.nuclearprogram.world.item.ItemDetonator;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
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
	public static Item ingotKaupium;
	public static Item ingotYanoizedKaupium;
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

	public static Item valve;

	public static Item screwdriver;
	public static ItemDetonator detonator;

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
		ingotKaupium = helper.register("ingotKaupium");
		ingotYanoizedKaupium = helper.register("ingotYanoizedKaupium");
		plateCopper = helper.register("plateCopper");
		plateAluminium = helper.register("plateAluminium");
		plateLead = helper.register("plateLead");
		plateTitanium = helper.register("plateTitanium");
		plateTungsten = helper.register("plateTungsten");
		plateSteel = helper.register("plateSteel");
		ballFireclay = helper.register("ballFireclay");
		firebrick = helper.register("firebrick");
		potato = helper.register("potato", id -> new ItemConsumableSeeds(id, BlockInit.potatoes.blockID, 1, false));
		potatoCooked = helper.register("potatoCooked", id -> new ItemFood(id, 4, false)
				.setMaxStackSize(64));

		valve = helper.register("valve");

		screwdriver = helper.register("screwdriver");
		detonator = helper.register("detonator", ItemDetonator::new);
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
