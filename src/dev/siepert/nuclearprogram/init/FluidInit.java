package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.world.fluid.Fluid;

public class FluidInit {
	public static final Fluid water = new Fluid(1)
			.setName("water").setColor(0.0F, 0.0F, 1.0F);
	public static final Fluid lava = new Fluid(2)
			.setName("lava").setColor(1.0F, 0.0F, 0.0F)
			.setTemperature(1000);
	public static final Fluid steam = new Fluid(3)
			.setName("steam").setColor(1.0F, 1.0F, 1.0F)
			.setTemperature(100)
			.setGaseous();
	public static final Fluid depletedSteam = new Fluid(4)
			.setName("depletedSteam").setColor(0x7C90FF)
			.setTemperature(100)
			.setGaseous();
	public static final Fluid hydrogenPeroxide = new Fluid(5)
			.setName("hydrogenPeroxide").setColor(0xD6EEFF);
	public static final Fluid sulfuricAcid = new Fluid(6)
			.setName("sulfuricAcid").setColor(0x7F6A00);
	public static final Fluid hydrofluoricAcid = new Fluid(7)
			.setName("hydrofluoricAcid").setColor(0xFFF0AA);
	public static final Fluid uraniumHexafluoride = new Fluid(8)
			.setName("uraniumHexafluoride").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous();
	public static final Fluid uraniumHexafluorideLE = new Fluid(9)
			.setName("uraniumHexafluorideLE").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous();
	public static final Fluid uraniumHexafluorideME = new Fluid(10)
			.setName("uraniumHexafluorideME").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous();
	public static final Fluid uraniumHexafluorideHE = new Fluid(11)
			.setName("uraniumHexafluorideHE").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous();

	public static void register() {}

	static {
		int counter = 1;
		int max = Fluid.ID_SIZE;

		for (int i = 1; i < 256; i++) {
			if (Fluid.fluidsList[i] != null) counter++;
		}

		System.out.println("There are " + counter + "/" + max + " fluids registered");
	}
}
