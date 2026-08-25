package dev.siepert.nuclearprogram.util;

import dev.objlib.api.IObjModel;
import net.minecraft.src.Tessellator;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.TerrainTextureMap;

public class TessellatableObj {
	public final IObjModel model;

	public TessellatableObj(IObjModel model) {
		this.model = model;
	}

	public void render(Tessellator tes, String group, TerrainTextureMap atlas, Icon texture) {
		this.model.tessellateGroup(tes, (float) texture.getU(0.0), (float) texture.getV(0.0), (float) atlas.getTileWidth(), (float) atlas.getTileHeight(), group);
	}
}
