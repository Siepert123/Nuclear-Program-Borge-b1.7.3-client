package dev.siepert.nuclearprogram.world.mapdata;

import dev.siepert.nuclearprogram.weapon.NukeType;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.List;

public class WorldFalloutClouds extends MapDataBase {
	public static final String NAME = "WorldFalloutClouds";
	public WorldFalloutClouds(String name) {
		super(name);
	}

	public static WorldFalloutClouds get(World world) {
		WorldFalloutClouds data = (WorldFalloutClouds) world.loadMapData(WorldFalloutClouds.class, NAME);
		if (data == null) {
			data = new WorldFalloutClouds(NAME);
			world.setMapData(NAME, data);
		}
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("Clouds");
		this.clouds.clear();

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);
			this.clouds.add(new CloudData(tag.getInteger("x"), tag.getInteger("z"), tag.getFloat("radius")));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList(this.clouds.size());
		nbt.setTag("Clouds", list);
		for (CloudData cloud : this.clouds) {
			NBTTagCompound tag = new NBTTagCompound(3);
			tag.setInteger("x", cloud.x);
			tag.setInteger("z", cloud.z);
			tag.setFloat("radius", cloud.radius);
			list.appendTag(tag);
		}
	}

	public final ArrayList<CloudData> clouds = new ArrayList<>();

	public void add(Entity source, NukeType type) {
		int radius = type.getFalloutRadius();
		if (radius < 8) return;
		float adjusted = radius * 0.125F;
		int x = MathHelper.floor_double(source.posX * 0.125);
		int z = MathHelper.floor_double(source.posZ * 0.125);
		this.clouds.add(new CloudData(x, z,  adjusted));
	}

	public static WorldFalloutClouds cache = null;
	public static void tick(World world) {
		if (cache != null) cache.tick0(world);
	}
	private void tick0(World world) {
		if ((world.getWorldTime() % (20*60)) != 0) return;
		if (!this.clouds.isEmpty()) this.setDirty(true);
		this.clouds.forEach(CloudData::tick);
		this.clouds.removeIf(CloudData::isUseless);
	}

	// Values in half-chunks
	public static class CloudData {
		public int x, z;
		public float radius;

		public CloudData(int x, int z, float radius) {
			this.x = x;
			this.z = z;
			this.radius = radius;
		}

		public boolean isUseless() {
			return this.radius < 1.0F;
		}
		public void tick() {
			this.x += 2;
			this.z += 1;
			this.radius -= 0.1F;
		}

		public static final boolean DEBUG = true;
		public boolean inRange(int x, int z) {
			int d2 = (this.x-x)*(this.x-x)+(this.z-z)*(this.z-z);
			return d2 <= (this.radius * this.radius);
		}
		public static boolean anyInRange(List<CloudData> clouds, int x, int z) {
			//if (DEBUG) return true;
			for (CloudData cloud : clouds) if (cloud.inRange(x, z)) return true;
			return false;
		}
	}
}
