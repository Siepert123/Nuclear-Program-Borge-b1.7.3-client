package dev.objlib.api;

import net.minecraftborge.loader.Icon;

import java.io.IOException;

public interface IObjModelFactory {
	IObjModel createWithoutRenderList(String path, boolean shade, Icon texture) throws IOException;
	IObjModel createWithRenderList(String path, boolean shade, Icon texture) throws IOException;
}
