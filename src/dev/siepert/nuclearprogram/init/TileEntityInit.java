package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.world.te.TileEntityFurnaceBuilder;
import net.minecraft.src.TileEntity;

public class TileEntityInit {

	public static void register() {
		TileEntity.addMapping(TileEntityFurnaceBuilder.class, NuclearProgram.MODID + "/furnaceBuilder");
	}
}
