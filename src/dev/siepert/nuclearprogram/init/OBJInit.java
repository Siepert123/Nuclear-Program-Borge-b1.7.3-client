package dev.siepert.nuclearprogram.init;

import dev.objlib.api.IObjModelFactory;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.WavefrontObj;

public class OBJInit {
	// Oil machines
	public static WavefrontObj oil_derrick = null;
	public static final String oil_derrick_tex = get("oil_derrick");
	public static WavefrontObj gas_flare = null;
	public static final String gas_flare_tex = get("gas_flare");
	public static WavefrontObj oil_distillery_base = null;
	public static final String oil_distillery_base_tex = get("oil_distillery_base");
	public static WavefrontObj oil_distillery_segment = null;
	public static final String oil_distillery_segment_tex = get("oil_distillery_segment");

	// Nuclear machines
	public static WavefrontObj gas_centrifuge = null;
	public static final String gas_centrifuge_tex = get("gas_centrifuge");

	public static void register() {
		final IObjModelFactory factory = IObjModelFactory.newFactory(null);
		if (factory == null) throw new RuntimeException("Where's my OBJ factory at");
		// Oil machines
		oil_derrick = get(factory, "oil_derrick");
		gas_flare = get(factory, "gas_flare");
		oil_distillery_base = get(factory, "oil_distillery_base");
		oil_distillery_segment = get(factory, "oil_distillery_segment");

		// Nuclear machines
		gas_centrifuge = get(factory, "gas_centrifuge");
	}
	public static void optimize() {
		// Oil machines
		oil_derrick.rerender();
		gas_flare.rerender();
		oil_distillery_base.rerender();
		oil_distillery_segment.rerender();

		// Nuclear machines
		gas_centrifuge.rerender();
	}

	private static WavefrontObj get(IObjModelFactory factory, String path) {
		return new WavefrontObj(factory.create("assets/obj/" + NuclearProgram.MODID + "/" + path + ".obj"));
	}
	private static String get(String path) {
		return "assets/obj_tex/" + NuclearProgram.MODID + "/" + path + ".png";
	}
}
