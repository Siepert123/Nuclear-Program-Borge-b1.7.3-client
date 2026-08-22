package dev.siepert.nuclearprogram.pipenet;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.WorldMap;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;
import net.minecraftborge.loader.event.EventBusSubscriber;
import net.minecraftborge.loader.event.EventHandler;
import net.minecraftborge.loader.event.world.ChangeWorldEvent;
import net.minecraftborge.loader.event.world.chunk.ChunkDataEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(NuclearProgram.MODID + ".pipenet")
public final class PipeNet {
	@EventHandler
	public static void loadChunkData(ChunkDataEvent.Load event) {
		if (event.getWorld() == null) throw new RuntimeException("Yo -- World is null :joy:");
		WorldData data = worlds.computeIfAbsent(event.getWorld(), $ -> new WorldData());
		NBTTagCompound nbt = event.getData().getCompoundTag("PipeNet");
		if (!nbt.values().isEmpty()) {
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(event.getChunk().xPosition, event.getChunk().zPosition);
			Chunk chunk = new Chunk();
			if (data.chunks.put(chunkPos, chunk) != null) {
				System.out.println("WARN: PipeNet chunk replaced!!");
			}
			chunk.readFromNBT(nbt);
		}
	}

	@EventHandler
	public static void storeChunkData(ChunkDataEvent.Store event) {
		WorldData data = worlds.get(event.getWorld());
		if (data == null) return;
		int chunkPos = ChunkCoordIntPair.chunkXZ2Int(event.getChunk().xPosition, event.getChunk().zPosition);
		Chunk chunk = data.chunks.get(chunkPos);
		NBTTagCompound nbt = new NBTTagCompound();
		if (chunk != null && !chunk.nodes.isEmpty()) {
			chunk.writeToNBT(nbt);
		}
		event.getData().setCompoundTag("PipeNet", nbt);
	}

	@EventHandler
	public static void changeWorld(ChangeWorldEvent event) {
		if (event.getWorld() == null) {
			worlds.optimize();
		} else {
			main = worlds.computeIfAbsent(event.getWorld(), $ -> new WorldData());
		}
	}

	public static PipeNetNode getNode(int x, int y, int z) {
		return main.getNode(x, y, z);
	}
	public static void setNode(int x, int y, int z, PipeNetNode node) {
		main.setNode(x, y, z, node);
	}

	public static PipeNetNode getNode(World world, int x, int y, int z) {
		return worlds.get(world).getNode(x, y, z);
	}
	public static void setNode(World world, int x, int y, int z, PipeNetNode node) {
		worlds.get(world).setNode(x, y, z, node);
	}

	private static final WorldMap<WorldData> worlds = new WorldMap<>();
	public static WorldData main = null;
	public static class WorldData {
		private WorldData() {}

		private final Map<Integer, Chunk> chunks = new HashMap<>();

		public PipeNetNode getNode(int x, int y, int z) {
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(x >> 4, z >> 4);
			Chunk chunk = chunks.get(chunkPos);
			if (chunk == null) return null;
			return chunk.getNode(x & 15, y, z & 15);
		}

		public void setNode(int x, int y, int z, PipeNetNode node) {
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(x >> 4, z >> 4);
			Chunk chunk = chunks.get(chunkPos);
			if (chunk == null) {
				if (node == null) return;
				chunk = new Chunk();
				chunks.put(chunkPos, chunk);
			}
			chunk.setNode(x & 15, y, z & 15, node);
		}
	}

	public static class Chunk {
		private Chunk() {

		}

		public final Map<Integer, PipeNetNode> nodes = new HashMap<>();

		public void readFromNBT(NBTTagCompound nbt) {
			NBTTagList list = nbt.getTagList("Nodes");
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
				PipeNetNode node = PipeNetNode.create(compound);
				if (node != null) {
					int packed = ((node.x & 15) << 12) | ((node.z & 15) << 8) | (node.y & 255);
					this.nodes.put(packed, node);
				}
			}
		}
		public void writeToNBT(NBTTagCompound nbt) {
			NBTTagList list = new NBTTagList(this.nodes.size());
			for (PipeNetNode node : this.nodes.values()) {
				NBTTagCompound compound = new NBTTagCompound(5);
				node.writeToNBT(compound);
				list.setTag(compound);
			}
			nbt.setTag("Nodes", list);
		}

		public PipeNetNode getNode(int x, int y, int z) {
			int packed = (x << 12) | (z << 8) | y;
			return this.nodes.get(packed);
		}
		public void setNode(int x, int y, int z, PipeNetNode node) {
			int packed = (x << 12) | (z << 8) | y;
			if (node == null) this.nodes.remove(packed);
			else this.nodes.put(packed, node);
		}
	}
}
