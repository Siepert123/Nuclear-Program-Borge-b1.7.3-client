package dev.siepert.nuclearprogram.pipenet;

import dev.siepert.nuclearprogram.pipenet.node.PNNBasic;
import dev.siepert.nuclearprogram.pipenet.node.PNNMultiblockProxy;
import dev.siepert.nuclearprogram.util.BlockPos;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

public abstract class PipeNetNode {
	private static final HashMap<String, Class<? extends PipeNetNode>> nameToClassMap = new HashMap<>();
	private static final HashMap<Class<? extends PipeNetNode>, String> classToNameMap = new HashMap<>();
	private static final HashMap<Class<? extends PipeNetNode>, Function<World, ? extends PipeNetNode>> classToCtorMap = new HashMap<>();

	public static void register(Class<? extends PipeNetNode> clazz, String name, Function<World, ? extends PipeNetNode> ctor) {
		if (nameToClassMap.containsKey(name)) throw new IllegalArgumentException("Duplicate PipeNetNode ID: " + name);
		if (classToNameMap.containsKey(clazz)) throw new IllegalArgumentException("Duplicate PipeNetNode type: " + clazz.getName());

		nameToClassMap.put(name, clazz);
		classToNameMap.put(clazz, name);
		classToCtorMap.put(clazz, ctor);
	}

	public static void doRegistries() {
		register(PNNBasic.class, "basic", PNNBasic::new);
		register(PNNMultiblockProxy.class, "proxy", PNNMultiblockProxy::new);
	}

	public static PipeNetNode create(World world, NBTTagCompound nbt) {
		String id = nbt.getString("id");
		Function<World, ? extends PipeNetNode> ctor = classToCtorMap.get(nameToClassMap.get(id));
		if (ctor == null) {
			System.err.println("Invalid PipeNetNode id	: " + id);
			return null;
		}
		PipeNetNode node = ctor.apply(world);
		node.readFromNBT(nbt);
		return node;
	}

	private final IReceivingPipeNetNode receiving = this instanceof IReceivingPipeNetNode ? (IReceivingPipeNetNode) this : null;
	public final boolean isReceiving() {
		return this.receiving != null;
	}
	public final IReceivingPipeNetNode asReceiving() {
		return this.receiving;
	}

	public final World worldObj;
	public PipeNetNode(World world) {
		this.worldObj = world;
	}

	public int x, y, z;
	public int fluidType;
	public long network = 0L;

	public void readFromNBT(NBTTagCompound nbt) {
		this.x = nbt.getInteger("x");
		this.y = nbt.getInteger("y");
		this.z = nbt.getInteger("z");
		this.fluidType = nbt.getShort("fluidType") & 0xFFFF;
	}
	public void writeToNBT(NBTTagCompound nbt) {
		String id = classToNameMap.get(this.getClass());
		if (id == null) {
			throw new RuntimeException("PipeNetNode " + this.getClass().getName() + " is unregistered!! Pls fix!!");
		} else {
			nbt.setString("id", id);
			nbt.setInteger("x", this.x);
			nbt.setInteger("y", this.y);
			nbt.setInteger("z", this.z);
			nbt.setShort("fluidType", (short) this.fluidType);
		}
	}
	public final PipeNetNode positioned(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	public boolean canConnect(int fluidType) {
		return this.fluidType == fluidType;
	}
	public boolean canConnect(PipeNetNode other) {
		return this.canConnect(other.fluidType) || other.isReceiving();
	}

	public void getConnectedPositions(BlockPos.Pool pool, Collection<ChunkCoordinates> positions) {
		positions.add(pool.get(this.x+1, this.y, this.z));
		positions.add(pool.get(this.x, this.y+1, this.z));
		positions.add(pool.get(this.x, this.y, this.z+1));
		positions.add(pool.get(this.x-1, this.y, this.z));
		positions.add(pool.get(this.x, this.y-1, this.z));
		positions.add(pool.get(this.x, this.y, this.z-1));
	}

	public final long pushFluid(int fluidType, long amount, int bar) {
		if (this.isReceiving()) {
			return this.asReceiving().addFluid(fluidType, amount, bar);
		} else {
			PipeNet.WorldData data = PipeNet.getData(this.worldObj);
			return data.getOrCreateNetwork(this).pushFluid(fluidType, amount, bar);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[network=" + this.network + ",fluidType=" + this.fluidType + "]";
	}
}
