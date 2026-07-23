package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.*;
import dev.siepert.nuclearprogram.world.te.render.RenderHatch;
import dev.siepert.nuclearprogram.world.te.render.RenderSealedDoor;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;

public class TileEntityInit {

	public static void register() {
		TileEntity.addMapping(TileEntityFurnaceBuilder.class, NuclearProgram.MODID + "/furnaceBuilder");

		TileEntity.addMapping(TileEntityHatch.class, NuclearProgram.MODID + "/hatch");
		TileEntityRenderer.instance.addRenderer(TileEntityHatch.class, RenderHatch.RENDERER);
		TileEntity.addMapping(TileEntitySealedDoor.class, NuclearProgram.MODID + "/sealedDoor");
		TileEntityRenderer.instance.addRenderer(TileEntitySealedDoor.class, RenderSealedDoor.INSTANCE);
		TileEntity.addMapping(TileEntityModulator.class, NuclearProgram.path("modulator"));

		TileEntity.addMapping(TileEntityBloomery.class, NuclearProgram.path("bloomery"));

		TileEntity.addMapping(TileEntityCableCoated.class, NuclearProgram.path("cableCoated"));
		TileEntity.addMapping(TileEntityFluidPipe.class, NuclearProgram.path("fluidPipe"));
		TileEntity.addMapping(TileEntityFluidPipeCoated.class, NuclearProgram.path("fluidPipeCoated"));

		TileEntity.addMapping(TileEntityGasCentrifuge.class, NuclearProgram.path("gasCentrifuge"));

		TileEntity.addMapping(TileEntityRTG.class, NuclearProgram.path("rtg"));

		TileEntity.addMapping(TileEntityRBMKColumn.class, NuclearProgram.path("rbmk/blank"));
		TileEntity.addMapping(TileEntityRBMKBoiler.class, NuclearProgram.path("rbmk/boiler"));
		TileEntity.addMapping(TileEntityRBMKFuel.class, NuclearProgram.path("rbmk/fuel"));
	}
}
