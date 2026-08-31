package dev.siepert.nuclearprogram.init;

import dev.objlib.api.IObjModelFactory;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.WavefrontObj;

public class OBJInit {
	public static WavefrontObj animation_test = null;
	public static final String animation_test_tex = get("animation_test");

	// Early game
	public static WavefrontObj air_stove = null;
	public static final String air_stove_tex = get("air_stove");
	public static WavefrontObj blast_furnace = null;
	public static final String blast_furnace_tex = get("blast_furnace");

	// Oil machines
	public static WavefrontObj oil_derrick = null;
	public static final String oil_derrick_tex = get("oil_derrick");
	public static WavefrontObj gas_flare = null;
	public static final String gas_flare_tex = get("gas_flare");
	public static WavefrontObj drainage_pipe = null;
	public static final String drainage_pipe_tex = get("drainage_pipe");
	public static WavefrontObj oil_distillery_base = null;
	public static final String oil_distillery_base_tex = get("oil_distillery_base");
	public static WavefrontObj oil_distillery_segment = null;
	public static final String oil_distillery_segment_tex = get("oil_distillery_segment");

	public static WavefrontObj combustion_engine_v8 = null;
	public static final String combustion_engine_v8_tex = get("combustion_engine_v8");

	// Nuclear machines
	public static WavefrontObj gas_centrifuge = null;
	public static final String gas_centrifuge_tex = get("gas_centrifuge");

	public static WavefrontObj hsrfs = null;
	public static final String hsrfs_tex = get("hsrfs");

	public static void register() {
		final IObjModelFactory factory = IObjModelFactory.newFactory(null);
		if (factory == null) throw new RuntimeException("Where's my OBJ factory at");
		animation_test = get(factory, "animation_test");

		// Early game
		air_stove = get(factory, "air_stove");
		blast_furnace = get(factory, "blast_furnace");

		// Oil machines
		oil_derrick = get(factory, "oil_derrick");
		gas_flare = get(factory, "gas_flare");
		drainage_pipe = get(factory, "drainage_pipe");
		oil_distillery_base = get(factory, "oil_distillery_base");
		oil_distillery_segment = get(factory, "oil_distillery_segment");

		combustion_engine_v8 = get(factory, "combustion_engine_v8");

		// Nuclear machines
		gas_centrifuge = get(factory, "gas_centrifuge");

		hsrfs = get(factory, "hsrfs");
	}
	public static void optimize() {
		animation_test.rerender();

		// Early game
		air_stove.rerender();
		blast_furnace.rerender();

		// Oil machines
		oil_derrick.rerender();
		gas_flare.rerender();
		drainage_pipe.rerender();
		oil_distillery_base.rerender();
		oil_distillery_segment.rerender();

		combustion_engine_v8.rerender();

		// Nuclear machines
		gas_centrifuge.rerender();

		hsrfs.rerender();
	}

	private static WavefrontObj get(IObjModelFactory factory, String path) {
		return new WavefrontObj(factory.create("assets/obj/" + NuclearProgram.MODID + "/" + path + ".obj"));
	}
	private static String get(String path) {
		return "assets/obj_tex/" + NuclearProgram.MODID + "/" + path + ".png";
	}
}
