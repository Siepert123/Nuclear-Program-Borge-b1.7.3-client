package dev.siepert.nuclearprogram.init;

import dev.objlib.api.IObjModelFactory;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.WavefrontObj;

public class OBJInit {
	public static WavefrontObj gas_centrifuge = null;
	public static final String gas_centrifuge_tex = get("gas_centrifuge");

	public static void register() {
		final IObjModelFactory factory = IObjModelFactory.newFactory(null);
		if (factory == null) throw new RuntimeException("Where's my OBJ factory at");
		gas_centrifuge = get(factory, "gas_centrifuge");
	}
	public static void optimize() {
		gas_centrifuge.rerender();
	}

	private static WavefrontObj get(IObjModelFactory factory, String path) {
		return new WavefrontObj(factory.create("assets/obj/" + NuclearProgram.MODID + "/" + path + ".obj"));
	}
	private static String get(String path) {
		return "assets/obj_tex/" + NuclearProgram.MODID + "/" + path + ".png";
	}
}
