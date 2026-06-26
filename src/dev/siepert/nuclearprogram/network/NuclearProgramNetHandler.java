package dev.siepert.nuclearprogram.network;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipes;
import net.minecraft.src.NetHandler;
import net.minecraftborge.loader.ByteArrayReader;
import net.minecraftborge.loader.ByteArrayWriter;
import net.minecraftborge.loader.ModList;
import net.minecraftborge.loader.net.ModNetHandler;
import net.minecraftborge.loader.net.Packet143Custom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NuclearProgramNetHandler extends ModNetHandler {
	private final int modIndex;
	public static NuclearProgramNetHandler instance;
	private NuclearProgramNetHandler() {
		this.modIndex = ModList.get().getModIndex(NuclearProgram.MODID);
	}

	public static NuclearProgramNetHandler register() {
		if (instance != null) throw new IllegalStateException("Already registered!");
		return instance = new NuclearProgramNetHandler();
	}

	@Override
	public void processPacket(NetHandler network, byte[] data) {
		this.reader.setData(data);
		int type = this.reader.readUnsignedByte();
		try {
			switch (type) {
				case 0:
					this.handleWorkbenchSyncPacket();
					break;
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to process packet #" + type, e);
		}
	}

	public void handleWorkbenchSyncPacket() throws IOException {
		int entries = this.reader.readInt();
		Map<String, Integer> remote = new HashMap<>(entries);
		for (int i = 0; i < entries; ++i) {
			String key = ByteArrayReader.readString(this.reader, 64);
			int index = this.reader.readInt();
			remote.put(key, index);
		}
		WorkbenchRecipes.crafting().setRemote(remote);
	}

	public Packet143Custom createWorkbenchPacket(String recipeID, boolean massProduce) {
		this.writer.reset();
		this.writer.writeByte(0);
		this.writer.writeInt(WorkbenchRecipes.crafting().getRemoteIndex(recipeID));
		this.writer.writeBoolean(massProduce);
		return new Packet143Custom(this.modIndex, this.writer.finish());
	}
}
