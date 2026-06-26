package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.entity.EntityHowitzerShell;
import net.minecraftborge.loader.GameRegistries;

public class EntityInit {
	public static void register() {
		GameRegistries.ENTITIES.register(NuclearProgram.MODID + "/HowitzerShell", EntityHowitzerShell.class);

		System.out.println("HowitzerShell id: " + GameRegistries.ENTITIES.wrapper.classToIDMap.get(EntityHowitzerShell.class));
	}
}
