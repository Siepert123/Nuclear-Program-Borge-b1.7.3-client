package dev.siepert.nuclearprogram.cablenet;

import dev.siepert.nuclearprogram.cablenet.node.CNNBasic;
import dev.siepert.nuclearprogram.cablenet.node.CNNBasicReceiver;
import dev.siepert.nuclearprogram.cablenet.node.CNNMultiblockProxy;
import dev.siepert.nuclearprogram.util.BlockPos;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class CableNetNode {
	private static final Map<String, Class<? extends CableNetNode>> nameToClassMap = new HashMap<>();
	private static final Map<Class<? extends CableNetNode>, String> classToNameMap = new HashMap<>();
	private static final Map<Class<? extends CableNetNode>, Function<World, ? extends CableNetNode>> classToCtorMap = new HashMap<>();

	public static void register(Class<? extends CableNetNode> clazz, String name, Function<World, ? extends CableNetNode> ctor) {
		if (nameToClassMap.containsKey(name)) throw new IllegalArgumentException("Duplicate CableNetNode id: " + name);
		if (classToNameMap.containsKey(clazz)) throw new IllegalArgumentException("Duplicate CableNetNode type: " + clazz.getName());

		nameToClassMap.put(name, clazz);
		classToNameMap.put(clazz, name);
		classToCtorMap.put(clazz, ctor);
	}

	public static void doRegistries() {
		register(CNNBasic.class, "basic", CNNBasic::new);
		register(CNNMultiblockProxy.class, "proxy", CNNMultiblockProxy::new);
		register(CNNBasicReceiver.class, "receiver", CNNBasicReceiver::new);
	}

	public static CableNetNode create(World world, NBTTagCompound nbt) {
		String id = nbt.getString("id");
		Function<World, ? extends CableNetNode> ctor = classToCtorMap.get(nameToClassMap.get(id));
		if (ctor == null) {
			System.err.println("Invalid CableNetNode id: " + id);
			return null;
		}
		CableNetNode node = ctor.apply(world);
		node.readFromNBT(nbt);
		return node;
	}

	private final IReceivingCableNetNode receiving = this instanceof IReceivingCableNetNode ? (IReceivingCableNetNode) this : null;
	public final boolean isReceiving() {
		return this.receiving != null;
	}
	public final IReceivingCableNetNode asReceiving() {
		return this.receiving;
	}

	public final World worldObj;
	public CableNetNode(World world) {
		this.worldObj = world;
	}

	public int x, y, z;
	public long network;

	public void readFromNBT(NBTTagCompound nbt) {
		this.x = nbt.getInteger("x");
		this.y = nbt.getInteger("y");
		this.z = nbt.getInteger("z");
	}
	public void writeToNBT(NBTTagCompound nbt) {
		String id = classToNameMap.get(this.getClass());
		if (id == null) {
			throw new RuntimeException("CableNetNode " + this.getClass().getName() + " is unregistered!! Pls fix!!");
		} else {
			nbt.setString("id", id);
			nbt.setInteger("x", this.x);
			nbt.setInteger("y", this.y);
			nbt.setInteger("z", this.z);
		}
	}

	public final CableNetNode positioned(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public void getConnectedPositions(BlockPos.Pool pool, Collection<ChunkCoordinates> positions) {
		positions.add(pool.get(this.x+1, this.y, this.z));
		positions.add(pool.get(this.x, this.y+1, this.z));
		positions.add(pool.get(this.x, this.y, this.z+1));
		positions.add(pool.get(this.x-1, this.y, this.z));
		positions.add(pool.get(this.x, this.y-1, this.z));
		positions.add(pool.get(this.x, this.y, this.z-1));
	}

	public final long pushEnergy(long amount) {
		if (this.isReceiving()) {
			return this.asReceiving().addEnergy(amount);
		} else {
			CableNet.WorldData data = CableNet.getData(this.worldObj);
			return data.getOrCreateNetwork(this).pushEnergy(amount);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[network=" + this.network + "]";
	}
}
