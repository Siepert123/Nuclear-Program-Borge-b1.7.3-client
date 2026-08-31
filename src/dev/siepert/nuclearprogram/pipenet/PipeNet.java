package dev.siepert.nuclearprogram.pipenet;

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

@EventBusSubscriber(NuclearProgram.MODID + ".pipenet")
public final class PipeNet {
	public static long nextNetworkID = 1L;

	@EventHandler
	public static void loadChunkData(ChunkDataEvent.Load event) {
		if (event.getWorld() == null) throw new RuntimeException("World is null!");
		WorldData data = worlds.computeIfAbsent(event.getWorld(), WorldData::new);
		NBTTagCompound nbt = event.getData().getCompoundTag("PipeNet");
		if (!nbt.values().isEmpty()) {
			int chunkX = event.getChunk().xPosition;
			int chunkZ = event.getChunk().zPosition;
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
			Chunk chunk = new Chunk(event.getWorld(), data, chunkX, chunkZ);
			if (data.chunks.put(chunkPos, chunk) != null) {
				System.out.println("WARN: PipeNet chunk replaced!!");
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
		event.getData().setCompoundTag("PipeNet", nbt);
	}

	@EventHandler
	public static void changeWorld(ChangeWorldEvent event) {
		if (event.getWorld() == null) {
			worlds.optimize();
		} else {
			main = worlds.computeIfAbsent(event.getWorld(), WorldData::new);
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

		public PipeNetNode getNode(int x, int y, int z) {
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(x >> 4, z >> 4);
			Chunk chunk = this.chunks.get(chunkPos);
			if (chunk == null) return null;
			return chunk.getNode(x & 15, y, z & 15);
		}

		public void setNode(int x, int y, int z, PipeNetNode node) {
			int chunkPos = ChunkCoordIntPair.chunkXZ2Int(x >> 4, z >> 4);
			Chunk chunk = chunks.get(chunkPos);
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
		public Network getOrCreateNetwork(PipeNetNode node) {
			if (node.isReceiving()) throw new IllegalStateException("Cannot bind network to receiving node");

			if (node.network != 0L) {
				return this.getNetwork(node.network);
			} else {
				Network network = new Network(nextNetworkID++);
				this.networks.put(network.id, network);
				network.fluidType = node.fluidType;
				BlockPos.Pool pool = this.posPool;
				pool.reset();

				Set<ChunkCoordinates> positions = new HashSet<>();
				Set<ChunkCoordinates> temp = new HashSet<>();
				List<PipeNetNode> queue = new ArrayList<>();

				queue.add(node);

				while (!queue.isEmpty()) {
					PipeNetNode next = queue.remove(0);
					positions.add(pool.get(next.x, next.y, next.z));
					network.addNode(next);

					if (!next.isReceiving()) {
						next.getConnectedPositions(pool, temp);
						for (ChunkCoordinates pos : temp) {
							if (positions.contains(pos)) continue;
							PipeNetNode connected = this.getNode(pos.x, pos.y, pos.z);
							if (connected != null && (connected.fluidType == network.fluidType || connected.isReceiving())) {
								queue.add(connected);
							}
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
			for (PipeNetNode node : network.nodes) {
				node.network = 0L;
			}
		}
		public void invalidateAround(PipeNetNode added) {
			this.reusableNetworkList.clear();
			this.posPool.reset();
			if (added.network != 0L) this.reusableNetworkList.add(added.network);
			List<ChunkCoordinates> candidates = new ArrayList<>(16);
			added.getConnectedPositions(this.posPool, candidates);
			for (ChunkCoordinates pos : candidates) {
				PipeNetNode other = this.getNode(pos.x, pos.y, pos.z);
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
		public final Map<Integer, PipeNetNode> nodes = new HashMap<>();

		public void readFromNBT(NBTTagCompound nbt) {
			NBTTagList list = nbt.getTagList("Nodes");
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
				PipeNetNode node = PipeNetNode.create(this.worldObj, compound);
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

		// checking self is always faster but there are instances where a node is in another chunk
		private PipeNetNode getNodeGlobally(int x, int y, int z) {
			if (x >> 4 == this.x && z >> 4 == this.z) {
				return this.getNode(x & 15, y, z & 15);
			} else return this.parent.getNode(x, y, z);
		}

		public PipeNetNode getNode(int x, int y, int z) {
			int packed = (x << 12) | (z << 8) | y;
			return this.nodes.get(packed);
		}
		public void setNode(int x, int y, int z, PipeNetNode node) {
			int packed = (x << 12) | (z << 8) | y;
			if (node == null) {
				PipeNetNode prev = this.nodes.remove(packed); //TODO: Invalidate network connected to this pipe
				if (prev != null) this.parent.invalidateNetwork(prev.network);
			} else {
				this.nodes.put(packed, node); //TODO: Invalidate all networks in surrounding chunks
				this.parent.invalidateAround(node);
			}
		}
	}
	public static class Network {
		public final long id;
		public int fluidType = 0;

		public final Set<PipeNetNode> nodes = new HashSet<>();
		public final Set<IReceivingPipeNetNode> receivers = new TreeSet<>();

		public Network(long id) {
			this.id = id;
		}

		private void addNode(PipeNetNode node) {
			Objects.requireNonNull(node, "node");

			if (node.isReceiving()) this.receivers.add(node.asReceiving());
			else if (node.fluidType != this.fluidType) throw new IllegalStateException("Incompatible node added");
			else {
				this.nodes.add(node);
				node.network = this.id;
			}
		}

		public long pushFluid(int fluidType, long amount, int bar) {
			if (this.fluidType != fluidType || amount == 0L) return amount;
			//TODO: Distribute fluid among equal priority evenly?
			for (IReceivingPipeNetNode receiver : this.receivers) {
				amount = receiver.addFluid(fluidType, amount, bar);
				if (amount == 0L) break;
			}
			return amount;
		}
		public long getTotalCapacity(int fluidType, int bar) {
			if (this.fluidType != fluidType) return 0L;
			long amount = 0L;
			for (IReceivingPipeNetNode receiver : this.receivers) {
				amount += receiver.getCapacity(fluidType, bar);
			}
			return amount;
		}
		public long getRemainingCapacity(int fluidType, int bar) {
			if (this.fluidType != fluidType) return 0L;
			long amount = 0L;
			for (IReceivingPipeNetNode receiver : this.receivers) {
				amount += receiver.getRemainingCapacity(fluidType, bar);
			}
			return amount;
		}

		@Override
		public String toString() {
			return "PipeNet.Network#" + this.id;
		}
	}
}
