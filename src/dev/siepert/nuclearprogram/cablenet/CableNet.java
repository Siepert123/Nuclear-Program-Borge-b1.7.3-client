package dev.siepert.nuclearprogram.cablenet;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.BlockPos;
import dev.siepert.nuclearprogram.util.WorldMap;
import dev.siepert.nuclearprogram.util.collect.LongList;
import dev.siepert.nuclearprogram.util.collect.SizedLongArrayList;
import net.minecraft.src.*;
import net.minecraftborge.loader.event.EventBusSubscriber;
import net.minecraftborge.loader.event.EventHandler;
import net.minecraftborge.loader.event.world.ChangeWorldEvent;
import net.minecraftborge.loader.event.world.chunk.ChunkDataEvent;

import java.util.*;

@EventBusSubscriber(NuclearProgram.MODID + ".cablenet")
public final class CableNet {
	public static long nextNetworkID = 1L;

	@EventHandler
	public static void loadChunkData(ChunkDataEvent.Load event) {
		if (event.getWorld() == null) throw new RuntimeException("World is null!");
		WorldData data = worlds.computeIfAbsent(event.getWorld(), WorldData::new);
		NBTTagCompound nbt = event.getData().getCompoundTag("CableNet");
		if (!nbt.values().isEmpty()) {
			int chunkX = event.getChunk().xPosition;
			int chunkZ = event.getChunk().zPosition;
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
			Chunk chunk = new Chunk(event.getWorld(), data, chunkX, chunkZ);
			if (data.chunks.put(chunkPos, chunk) != null) {
				System.out.println("WARN: CableNet chunk replaced!!");
			}
			chunk.readFromNBT(nbt);

			chunk.nodes.values().forEach(data::invalidateAround);
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
		event.getData().setCompoundTag("CableNet", nbt);
	}

	@EventHandler
	public static void changeWorld(ChangeWorldEvent event) {
		if (event.getWorld() == null) {
			worlds.optimize();
		} else {
			main = worlds.computeIfAbsent(event.getWorld(), WorldData::new);
		}
	}

	public static CableNetNode getNode(int x, int y, int z) {
		return main.getNode(x, y, z);
	}
	public static void setNode(int x, int y, int z, CableNetNode node) {
		main.setNode(x, y, z, node);
	}

	public static CableNetNode getNode(World world, int x, int y, int z) {
		return worlds.get(world).getNode(x, y, z);
	}
	public static void setNode(World world, int x, int y, int z, CableNetNode node) {
		worlds.get(world).setNode(x, y, z, node);
	}

	public static WorldData getData(World world) {
		return world != null ? worlds.get(world) : main;
	}

	private static final WorldMap<WorldData> worlds = new WorldMap<>();
	public static WorldData main = null;
	public static class WorldData {
		private WorldData(World world) {
			this.worldObj = world;
		}

		public final World worldObj;
		private final Map<Integer, Chunk> chunks = new HashMap<>();
		private final Map<Long, Network> networks = new HashMap<>();
		private final LongList reusableNetworkList = new SizedLongArrayList(64);
		private final BlockPos.Pool posPool = new BlockPos.Pool();

		public CableNetNode getNode(int x, int y, int z) {
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(x >> 4, z >> 4);
			Chunk chunk = this.chunks.get(chunkPos);
			if (chunk == null) return null;
			return chunk.getNode(x & 15, y, z & 15);
		}
		public void setNode(int x, int y, int z, CableNetNode node) {
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(x >> 4, z >> 4);
			Chunk chunk = this.chunks.get(chunkPos);
			if (chunk == null) {
				if (node == null) return;
				chunk = new Chunk(this.worldObj, this, x >> 4, z >> 4);
				this.chunks.put(chunkPos, chunk);
			}
			chunk.setNode(x & 15, y, z & 15, node);
			this.worldObj.getChunkFromBlockCoords(x, z).isModified = true;
		}

		public Network getNetwork(long id) {
			if (id == 0L) return null;
			return this.networks.get(id);
		}
		public Network getOrCreateNetwork(CableNetNode node) {
			if (node.isReceiving()) throw new IllegalStateException("Cannot bind network to receiving node");

			if (node.network != 0L) {
				return this.getNetwork(node.network);
			} else {
				Network network = new Network(nextNetworkID++);
				this.networks.put(network.id, network);
				BlockPos.Pool pool = this.posPool;
				pool.reset();

				Set<ChunkCoordinates> positions = new HashSet<>();
				Set<ChunkCoordinates> temp = new HashSet<>();
				List<CableNetNode> queue = new ArrayList<>();

				queue.add(node);

				while (!queue.isEmpty()) {
					CableNetNode next = queue.remove(0);
					positions.add(pool.get(next.x, next.y, next.z));
					network.addNode(next);

					if (!next.isReceiving()) {
						next.getConnectedPositions(pool, temp);
						for (ChunkCoordinates pos : temp) {
							if (positions.contains(pos)) continue;
							CableNetNode connected = this.getNode(pos.x, pos.y, pos.z);
							if (connected != null) queue.add(connected);
						}
						temp.clear();
					}
				}
				System.out.println("NETWORK CREATED #" + network.id);
				System.out.println("Node count: " + network.nodes.size());
				System.out.println("Receiver count: " + network.receivers.size());
				System.out.println("BlockPos pool data: " + pool.getIndex() + "/" + pool.getSize());
				return network;
			}
		}

		public void invalidateNetwork(long id) {
			if (id == 0L) return;
			Network network = this.networks.remove(id);
			if (network == null) return;
			for (CableNetNode node : network.nodes) {
				node.network = 0L;
			}
		}
		public void invalidateAround(CableNetNode added) {
			this.reusableNetworkList.clear();
			this.posPool.reset();
			if (added.network != 0L) this.reusableNetworkList.add(added.network);
			List<ChunkCoordinates> candidates = new ArrayList<>(16);
			added.getConnectedPositions(this.posPool, candidates);
			for (ChunkCoordinates pos : candidates) {
				CableNetNode other = this.getNode(pos.x, pos.y, pos.z);
				if (other != null && other.network != 0L && this.reusableNetworkList.indexOf(other.network) == -1) {
					this.reusableNetworkList.add(other.network);
				}
			}
			for (int i = 0; i < this.reusableNetworkList.size(); i++) {
				this.invalidateNetwork(this.reusableNetworkList.get(i));
			}
		}
	}
	public static class Chunk {
		private Chunk(World world, WorldData parent, int x, int z) {
			this.worldObj = world;
			this.parent = parent;
			this.x = x;
			this.z = z;
		}

		public final World worldObj;
		public final WorldData parent;
		public final int x, z;
		public final Map<Integer, CableNetNode> nodes = new HashMap<>();

		public void readFromNBT(NBTTagCompound nbt) {
			NBTTagList list = nbt.getTagList("Nodes");
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
				CableNetNode node = CableNetNode.create(this.worldObj, compound);
				if (node != null) {
					int packed = ((node.x & 15) << 12) | ((node.z & 15) << 8) | (node.y & 255);
					this.nodes.put(packed, node);
				}
			}
		}
		public void writeToNBT(NBTTagCompound nbt) {
			NBTTagList list = new NBTTagList(this.nodes.size());
			for (CableNetNode node : this.nodes.values()) {
				NBTTagCompound compound = new NBTTagCompound(4);
				node.writeToNBT(compound);
				list.setTag(compound);
			}
			nbt.setTag("Nodes", list);
		}

		// checking self is always faster but there are instances where a node is in another chunk
		private CableNetNode getNodeGlobally(int x, int y, int z) {
			if (x >> 4 == this.x && z >> 4 == this.z) {
				return this.getNode(x & 15, y, z & 15);
			} else return this.parent.getNode(x, y, z);
		}

		public CableNetNode getNode(int x, int y, int z) {
			int packed = (x << 12) | (z << 8) | y;
			return this.nodes.get(packed);
		}
		public void setNode(int x, int y, int z, CableNetNode node) {
			int packed = (x << 12) | (z << 8) | y;
			if (node == null) {
				CableNetNode prev = this.nodes.remove(packed); //TODO: Invalidate network connected to this cable
				if (prev != null) this.parent.invalidateNetwork(prev.network);
			} else {
				this.nodes.put(packed, node); //TODO: Invalidate all networks in surrounding chunks
				this.parent.invalidateAround(node);
			}
		}
	}
	public static class Network {
		public final long id;

		public final Set<CableNetNode> nodes = new HashSet<>();
		public final Set<IReceivingCableNetNode> receivers = new TreeSet<>();

		public Network(long id) {
			this.id = id;
		}

		private void addNode(CableNetNode node) {
			Objects.requireNonNull(node, "node");

			if (node.isReceiving()) {
				this.receivers.add(node.asReceiving());
			} else {
				this.nodes.add(node);
				node.network = this.id;
			}
		}

		public long pushEnergy(long amount) {
			for (IReceivingCableNetNode receiver : this.receivers) {
				amount = receiver.addEnergy(amount);
			}
			return amount;
		}
		public long getTotalCapacity() {
			long amount = 0L;
			for (IReceivingCableNetNode receiver : this.receivers) {
				amount += receiver.getCapacity();
			}
			return amount;
		}
		public long getRemainingCapacity() {
			long amount = 0L;
			for (IReceivingCableNetNode receiver : this.receivers) {
				amount += receiver.getRemainingCapacity();
			}
			return amount;
		}

		@Override
		public String toString() {
			return "CableNet.Network#" + this.id;
		}
	}
}
