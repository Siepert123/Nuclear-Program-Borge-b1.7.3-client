package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.TileEntityBloomery;
import dev.siepert.nuclearprogram.world.te.TileEntityFurnaceBuilder;
import dev.siepert.nuclearprogram.world.te.TileEntityHatch;
import dev.siepert.nuclearprogram.world.te.TileEntitySealedDoor;
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

		TileEntity.addMapping(TileEntityBloomery.class, NuclearProgram.path("bloomery"));
	}
}
