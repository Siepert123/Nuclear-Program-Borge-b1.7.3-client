package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.entity.EntityExplosionHelper;
import dev.siepert.nuclearprogram.world.entity.EntityHowitzerShell;
import net.minecraft.src.Entity;
import net.minecraftborge.loader.GameRegistries;

public class EntityInit {
	public static void register() {
		register("HowitzerShell", EntityHowitzerShell.class);
		register("ExplosionHelper", EntityExplosionHelper.class);
	}

	private static void register(String name, Class<? extends Entity> clazz) {
		GameRegistries.ENTITIES.register(NuclearProgram.path(name), clazz);
		System.out.println(name + " id: " + GameRegistries.ENTITIES.wrapper.classToIDMap.get(clazz));
	}
}
