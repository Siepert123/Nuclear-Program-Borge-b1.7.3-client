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
			.setGaseous()
			.disableIdentifier();
	public static final Fluid uraniumHexafluorideME = new Fluid(10)
			.setName("uraniumHexafluorideME").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous()
			.disableIdentifier();
	public static final Fluid uraniumHexafluorideHE = new Fluid(11)
			.setName("uraniumHexafluorideHE").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous()
			.disableIdentifier();
	public static final Fluid crudeOil = new Fluid(12)
			.setName("crudeOil").setColor(0x020202);
	public static final Fluid naturalGas = new Fluid(13)
			.setName("naturalGas").setColor(0.88F, 0.88F, 0.7F)
			.setGaseous();
	public static final Fluid heavyOil = new Fluid(14)
			.setName("heavyOil").setColor(0x000000);
	public static final Fluid diesel = new Fluid(15)
			.setName("diesel").setColor(0xB9A191);
	public static final Fluid kerosene = new Fluid(16)
			.setName("kerosene").setColor(0x888BC2);
	public static final Fluid naphtha = new Fluid(17)
			.setName("naphtha").setColor(0x78574F);
	public static final Fluid gasoline = new Fluid(18)
			.setName("gasoline").setColor(0xC0AF8B);
	public static final Fluid petroleumGas = new Fluid(19)
			.setName("petroleumGas").setColor(0xC0AF8B);
	public static final Fluid lpg = new Fluid(20)
			.setName("lpg").setColor(0xB0A97D);
	public static final Fluid ethane = new Fluid(21)
			.setName("ethane").setColor(0xFFFFFF);
	public static final Fluid propane = new Fluid(22)
			.setName("propane").setColor(0xFFFFFF);
	public static final Fluid air = new Fluid(23)
			.setName("air").setColor(0xFFFFFF);
	public static final Fluid airBlast = new Fluid(24)
			.setName("airBlast").setColor(0xFFCCCC);
	public static final Fluid creosote = new Fluid(25)
			.setName("creosote").setColor(0x654321);
	public static final Fluid carbonDioxide = new Fluid(26)
			.setName("carbonDioxide").setColor(0x444444);

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
