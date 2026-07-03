package dev.siepert.nuclearprogram.util;

import net.minecraft.src.ISaveHandler;
import net.minecraft.src.World;

import java.lang.reflect.Field;

public class SaveHandlerHook {
	private static final Field fieldISaveHandler = getField();
	private static Field getField() {
		for (Field field : World.class.getDeclaredFields()) {
			if (field.getType() == ISaveHandler.class) {
				return field;
			}
		}
		throw new RuntimeException("Field not found");
	}

	public static ISaveHandler cache = null;
	public static void set(World world) {
		cache = null;
		try {
			if (world != null) {
				fieldISaveHandler.setAccessible(true);
				cache = (ISaveHandler) fieldISaveHandler.get(world);
			}
		} catch (Exception e) {
			System.err.println("Failed to steal ISaveHandler: " + e);
		}
	}
}
