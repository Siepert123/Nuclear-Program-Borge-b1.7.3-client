package dev.siepert.nuclearprogram.util;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class WorldMap<V> {
	private final List<WeakReference<World>> keys;
	private final List<V> values;

	public WorldMap() {
		this.keys = new ArrayList<>();
		this.values = new ArrayList<>();
	}
	public WorldMap(int initialCapacity) {
		this.keys = new ArrayList<>(initialCapacity);
		this.values = new ArrayList<>(initialCapacity);
	}

	public V get(IBlockAccess world) {
		for (int i = 0; i < this.keys.size(); i++) {
			if (this.keys.get(i).get() == world) return this.values.get(i);
		}
		return null;
	}
	public V get(World world) {
		for (int i = 0; i < this.keys.size(); i++) {
			if (this.keys.get(i).get() == world) return this.values.get(i);
		}
		return null;
	}

	public void put(World world, V value) {
		this.keys.add(new WeakReference<>(world));
		this.values.add(value);
	}
	public V computeIfAbsent(World world, Function<World, V> sup) {
		V ret = this.get(world);
		if (ret == null) {
			ret = sup.apply(world);
			this.put(world, ret);
		}
		return ret;
	}

	public void optimize() {
		for (int i = this.keys.size() - 1; i >= 0; i--) {
			if (this.keys.get(i).get() == null) {
				this.keys.remove(i);
				this.values.remove(i);
			}
		}
	}
}
