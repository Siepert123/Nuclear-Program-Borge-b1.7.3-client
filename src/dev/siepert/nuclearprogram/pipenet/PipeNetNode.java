package dev.siepert.nuclearprogram.pipenet;

import dev.siepert.nuclearprogram.pipenet.node.PPNBasic;
import dev.siepert.nuclearprogram.util.BlockPos;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.NBTTagCompound;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;

public abstract class PipeNetNode {
	private static final HashMap<String, Class<? extends PipeNetNode>> nameToClassMap = new HashMap<>();
	private static final HashMap<Class<? extends PipeNetNode>, String> classToNameMap = new HashMap<>();
	private static final HashMap<Class<? extends PipeNetNode>, Supplier<? extends PipeNetNode>> classToCtorMap = new HashMap<>();

	public static void register(Class<? extends PipeNetNode> clazz, String name, Supplier<? extends PipeNetNode> ctor) {
		if (nameToClassMap.containsKey(name)) throw new IllegalArgumentException("Duplicate PipeNetNode ID: " + name);
		if (classToNameMap.containsKey(clazz)) throw new IllegalArgumentException("Duplicate PipeNetNode type: " + clazz.getName());

		nameToClassMap.put(name, clazz);
		classToNameMap.put(clazz, name);
		classToCtorMap.put(clazz, ctor);
	}

	public static void doRegistries() {
		register(PPNBasic.class, "basic", PPNBasic::new);
	}

	public static PipeNetNode create(NBTTagCompound nbt) {
		String id = nbt.getString("id");
		Supplier<? extends PipeNetNode> ctor = classToCtorMap.get(nameToClassMap.get(id));
		if (ctor == null) {
			System.err.println("Invalid PipeNetNode ID: " + id);
			return null;
		}
		PipeNetNode node = ctor.get();
		node.readFromNBT(nbt);
		return node;
	}

	public int x, y, z;
	public int fluidType;

	public void readFromNBT(NBTTagCompound nbt) {
		this.x = nbt.getInteger("x");
		this.y = nbt.getInteger("y");
		this.z = nbt.getInteger("z");
		this.fluidType = nbt.getShort("fluidType") & 0xFFFF;
	}
	public void writeToNBT(NBTTagCompound nbt) {
		String id = classToNameMap.get(this.getClass());
		System.out.println("saving " + id);
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

	public void getConnectedPositions(Collection<ChunkCoordinates> positions) {
		positions.add(BlockPos.pooled(this.x+1, this.y, this.z));
		positions.add(BlockPos.pooled(this.x, this.y+1, this.z));
		positions.add(BlockPos.pooled(this.x, this.y, this.z+1));
		positions.add(BlockPos.pooled(this.x-1, this.y, this.z));
		positions.add(BlockPos.pooled(this.x, this.y-1, this.z));
		positions.add(BlockPos.pooled(this.x, this.y, this.z-1));
	}
}
