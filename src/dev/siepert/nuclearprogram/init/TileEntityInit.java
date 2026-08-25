package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.*;
import dev.siepert.nuclearprogram.world.te.render.*;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.TileEntitySpecialRenderer;

public class TileEntityInit {

	public static void register() {
		TileEntity.addMapping(TileEntityProxy.Proxy00.class, NuclearProgram.path("proxy00"));
		TileEntity.addMapping(TileEntityProxy.Proxy01.class, NuclearProgram.path("proxy01"));
		TileEntity.addMapping(TileEntityProxy.Proxy10.class, NuclearProgram.path("proxy10"));
		TileEntity.addMapping(TileEntityProxy.Proxy11.class, NuclearProgram.path("proxy11"));

		TileEntity.addMapping(TileEntityFurnaceBuilder.class, NuclearProgram.MODID + "/furnaceBuilder");

		register(TileEntityHatch.class, "hatch", RenderHatch.RENDERER);
		register(TileEntitySealedDoor.class, "sealedDoor", RenderSealedDoor.INSTANCE);
		register(TileEntityModulator.class, "modulator", null);

		TileEntity.addMapping(TileEntityBloomery.class, NuclearProgram.path("bloomery"));

		TileEntity.addMapping(TileEntityCableCoated.class, NuclearProgram.path("cableCoated"));
		TileEntity.addMapping(TileEntityFluidPipe.class, NuclearProgram.path("fluidPipe"));
		TileEntity.addMapping(TileEntityFluidPipeCoated.class, NuclearProgram.path("fluidPipeCoated"));

		register(TileEntityCreativeSupply.class, "creativeSupply", null);

		// Oil machines
		register(TileEntityDerrick.class, "derrick", RenderDerrick.INSTANCE);
		register(TileEntityOilDistilleryController.class, "oilDistilleryController", RenderOilDistilleryController.INSTANCE);
		register(TileEntityOilDistillerySegment.class, "oilDistillerySegment", RenderOilDistillerySegment.INSTANCE);

		// Nuclear machines
		register(TileEntityGasCentrifuge.class, "gasCentrifuge", RenderGasCentrifuge.INSTANCE);

		register(TileEntityRTG.class, "rtg", null);

		TileEntity.addMapping(TileEntityRBMKColumn.class, NuclearProgram.path("rbmk/blank"));
		TileEntity.addMapping(TileEntityRBMKBoiler.class, NuclearProgram.path("rbmk/boiler"));
		TileEntity.addMapping(TileEntityRBMKFuel.class, NuclearProgram.path("rbmk/fuel"));
	}

	private static <T extends TileEntity> void register(Class<T> type, String name, TileEntitySpecialRenderer<T> tesr) {
		TileEntity.addMapping(type, NuclearProgram.path(name));
		if (tesr != null) TileEntityRenderer.instance.addRenderer(type, tesr);
	}
}
