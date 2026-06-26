package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.item.ItemConsumableSeeds;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraftborge.loader.event.register.IdAllocationEvent;

import java.util.function.IntFunction;

public class ItemInit {
	public static Item ingotCopper;
	public static Item ingotAluminium;
	public static Item ingotLead;
	public static Item ingotTitanium;
	public static Item ingotTungsten;
	public static Item ingotSteel;
	public static Item ingotElectrum;
	public static Item ingotKaupium;
	public static Item ingotYanoizedKaupium;
	public static ItemConsumableSeeds potato;
	public static ItemFood potatoCooked;

	public static void register(IdAllocationEvent<Item> event) {
		Helper helper = new Helper(NuclearProgram.MODID, event);

		ingotCopper = helper.register("ingotCopper", Item::new);
		ingotAluminium = helper.register("ingotAluminium", Item::new);
		ingotLead = helper.register("ingotLead", Item::new);
		ingotTitanium = helper.register("ingotTitanium", Item::new);
		ingotTungsten = helper.register("ingotTungsten", Item::new);
		ingotSteel = helper.register("ingotSteel", Item::new);
		ingotElectrum = helper.register("ingotElectrum", Item::new);
		ingotKaupium = helper.register("ingotKaupium", Item::new);
		ingotYanoizedKaupium = helper.register("ingotYanoizedKaupium", Item::new);
		potato = helper.register("potato", id -> new ItemConsumableSeeds(id, BlockInit.potatoes.blockID, 1, false));
		potatoCooked = helper.register("potatoCooked", id -> new ItemFood(id, 4, false)
				.setMaxStackSize(64));
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
	}
}
