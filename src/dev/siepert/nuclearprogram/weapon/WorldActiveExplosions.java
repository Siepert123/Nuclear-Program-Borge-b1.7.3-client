package dev.siepert.nuclearprogram.weapon;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.src.MapDataBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;

import java.util.ArrayList;

public class WorldActiveExplosions extends MapDataBase {
	public WorldActiveExplosions(String key) {
		super(key);
	}

	public static WorldActiveExplosions get(World world) {
		WorldActiveExplosions data = (WorldActiveExplosions) world.loadMapData(WorldActiveExplosions.class, "WorldActiveExplosions");
		if (data == null) {
			System.out.println("Data is null");
			data = new WorldActiveExplosions("WorldActiveExplosions");
			world.setMapData("WorldActiveExplosions", data);
		}
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.explosions.clear();

		NBTTagList list = nbt.getTagList("Explosions");

		for (int i = 0; i < list.tagCount(); i++) {
			this.explosions.add(new BackendExplosionHandler((NBTTagCompound) list.tagAt(i)));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList(this.explosions.size());

		for (BackendExplosionHandler ex : this.explosions) {
			NBTTagCompound tag = new NBTTagCompound();
			ex.writeNBT(tag);
			list.setTag(tag);
		}

		nbt.setTag("Explosions", list);
	}

	private final ArrayList<BackendExplosionHandler> explosions = new ArrayList<>();

	public static WorldActiveExplosions cache = null;
	public static void tick() {
		if (cache != null) cache.tick0();
	}
	public void tick0() {
		this.setDirty(true);
		for (BackendExplosionHandler batched : this.explosions) {
			batched.cacheChunksTick(NuclearProgram.EXPLOSION_MS_BUDGET);
			batched.destructionTick(NuclearProgram.EXPLOSION_MS_BUDGET);
		}

		this.explosions.removeIf(BackendExplosionHandler::isComplete);
	}

	public void setWorld(World world) {
		for (BackendExplosionHandler batched : this.explosions) {
			batched.worldObj = world;
		}
	}
	public void add(BackendExplosionHandler batched) {
		this.explosions.add(batched);
		this.setDirty(true);
	}
}
