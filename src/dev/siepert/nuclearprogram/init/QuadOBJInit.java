package dev.siepert.nuclearprogram.init;

import dev.objlib.api.IObjModelFactory;

import java.util.Objects;

public class QuadOBJInit {
	private static final IObjModelFactory FACTORY = IObjModelFactory.newFactory(null);

	public static void register() {

	}

	static {
		Objects.requireNonNull(FACTORY, "QuadOBJInit.FACTORY");
	}
}
