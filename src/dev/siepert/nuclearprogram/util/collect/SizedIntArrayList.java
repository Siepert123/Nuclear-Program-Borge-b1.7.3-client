package dev.siepert.nuclearprogram.util.collect;

public class SizedIntArrayList implements IntList {
	private final int maxSize;
	private final int[] values;
	private int index;

	public SizedIntArrayList(int size) {
		this.maxSize = size;
		this.values = new int[size];
		this.index = 0;
	}

	@Override
	public int get(int index) {
		return this.values[index];
	}

	@Override
	public int indexOf(int value) {
		for (int i = 0; i < this.index; i++) {
			if (this.values[i] == value) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(int value) {
		for (int i = this.index-1; i >= 0; i--) {
			if (this.values[i] == value) return i;
		}
		return -1;
	}

	@Override
	public int size() {
		return this.index;
	}

	@Override
	public void add(int value) {
		if (this.index == this.maxSize) throw new IllegalStateException("Exceeded max size of " + this.maxSize);
		this.values[this.index] = value;
		this.index++;
	}

	@Override
	public void set(int index, int value) {
		this.values[index] = value;
	}

	@Override
	public void clear() {
		this.index = 0;
	}
}
