package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.world.fluid.Fluid;

public class FluidInit {
	// Console Edition optimizations iq
	public static final int water_Id = 1;
	public static final int lava_Id = 2;
	public static final int steam_Id = 3;
	public static final int depletedSteam_Id = 4;
	public static final int hydrogenPeroxide_Id = 5;
	public static final int sulfuricAcid_Id = 6;
	public static final int hydrofluoricAcid_Id = 7;
	public static final int uraniumHexafluoride_Id = 8;
	public static final int uraniumHexafluorideLE_Id = 9;
	public static final int uraniumHexafluorideME_Id = 10;
	public static final int uraniumHexafluorideHE_Id = 11;
	public static final int crudeOil_Id = 12;
	public static final int naturalGas_Id = 13;
	public static final int heavyOil_Id = 14;
	public static final int diesel_Id = 15;
	public static final int kerosene_Id = 16;
	public static final int naphtha_Id = 17;
	public static final int gasoline_Id = 18;
	public static final int petroleumGas_Id = 19;
	public static final int lpg_Id = 20;
	public static final int ethane_Id = 21;
	public static final int propane_Id = 22;
	public static final int air_Id = 23;
	public static final int airBlast_Id = 24;
	public static final int creosote_Id = 25;
	public static final int carbonDioxide_Id = 26;
	public static final int carbonDioxideHot_Id = 27;

	// Fluids
	public static final Fluid water = new Fluid(water_Id)
			.setName("water").setColor(0.0F, 0.0F, 1.0F);
	public static final Fluid lava = new Fluid(lava_Id)
			.setName("lava").setColor(1.0F, 0.0F, 0.0F)
			.setTemperature(1000);
	public static final Fluid steam = new Fluid(steam_Id)
			.setName("steam").setColor(1.0F, 1.0F, 1.0F)
			.setTemperature(100)
			.setGaseous();
	public static final Fluid depletedSteam = new Fluid(depletedSteam_Id)
			.setName("depletedSteam").setColor(0x7C90FF)
			.setTemperature(100)
			.setGaseous();
	public static final Fluid hydrogenPeroxide = new Fluid(hydrogenPeroxide_Id)
			.setName("hydrogenPeroxide").setColor(0xD6EEFF);
	public static final Fluid sulfuricAcid = new Fluid(sulfuricAcid_Id)
			.setName("sulfuricAcid").setColor(0x7F6A00);
	public static final Fluid hydrofluoricAcid = new Fluid(hydrofluoricAcid_Id)
			.setName("hydrofluoricAcid").setColor(0xFFF0AA);
	public static final Fluid uraniumHexafluoride = new Fluid(uraniumHexafluoride_Id)
			.setName("uraniumHexafluoride").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous();
	public static final Fluid uraniumHexafluorideLE = new Fluid(uraniumHexafluorideLE_Id)
			.setName("uraniumHexafluorideLE").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous()
			.disableIdentifier();
	public static final Fluid uraniumHexafluorideME = new Fluid(uraniumHexafluorideME_Id)
			.setName("uraniumHexafluorideME").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous()
			.disableIdentifier();
	public static final Fluid uraniumHexafluorideHE = new Fluid(uraniumHexafluorideHE_Id)
			.setName("uraniumHexafluorideHE").setColor(0.9F, 1.0F, 0.9F)
			.setGaseous()
			.disableIdentifier();
	public static final Fluid crudeOil = new Fluid(crudeOil_Id)
			.setName("crudeOil").setColor(0x020202);
	public static final Fluid naturalGas = new Fluid(naturalGas_Id)
			.setName("naturalGas").setColor(0.88F, 0.88F, 0.7F)
			.setGaseous();
	public static final Fluid heavyOil = new Fluid(heavyOil_Id)
			.setName("heavyOil").setColor(0x000000);
	public static final Fluid diesel = new Fluid(diesel_Id)
			.setName("diesel").setColor(0xB9A191);
	public static final Fluid kerosene = new Fluid(kerosene_Id)
			.setName("kerosene").setColor(0x888BC2);
	public static final Fluid naphtha = new Fluid(naphtha_Id)
			.setName("naphtha").setColor(0x78574F);
	public static final Fluid gasoline = new Fluid(gasoline_Id)
			.setName("gasoline").setColor(0xC0AF8B);
	public static final Fluid petroleumGas = new Fluid(petroleumGas_Id)
			.setName("petroleumGas").setColor(0xC0AF8B);
	public static final Fluid lpg = new Fluid(lpg_Id)
			.setName("lpg").setColor(0xB0A97D);
	public static final Fluid ethane = new Fluid(ethane_Id)
			.setName("ethane").setColor(0xFFFFFF);
	public static final Fluid propane = new Fluid(propane_Id)
			.setName("propane").setColor(0xFFFFFF);
	public static final Fluid air = new Fluid(air_Id)
			.setName("air").setColor(0xFFFFFF);
	public static final Fluid airBlast = new Fluid(airBlast_Id)
			.setName("airBlast").setColor(0xFFCCCC);
	public static final Fluid creosote = new Fluid(creosote_Id)
			.setName("creosote").setColor(0x654321);
	public static final Fluid carbonDioxide = new Fluid(carbonDioxide_Id)
			.setName("carbonDioxide").setColor(0x444444)
			.setGaseous()
			.setHeatable(carbonDioxideHot_Id, 100);
	public static final Fluid carbonDioxideHot = new Fluid(carbonDioxideHot_Id)
			.setName("carbonDioxideHot").setColor(0x664444)
			.setGaseous()
			.setCoolable(carbonDioxide_Id, 100);

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
