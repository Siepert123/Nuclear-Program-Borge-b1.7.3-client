package dev.siepert.nuclearprogram.util.collect;

public class SizedLongArrayList implements LongList {
	private final int maxSize;
	private final long[] values;
	private int index;

	public SizedLongArrayList(int size) {
		this.maxSize = size;
		this.values = new long[size];
		this.index = size;
	}

	@Override
	public long get(int index) {
		return this.values[index];
	}

	@Override
	public int indexOf(long value) {
		for (int i = 0; i < this.index; i++) {
			if (this.values[i] == value) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(long value) {
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
	public void add(long value) {
		if (this.index == this.maxSize) throw new IllegalStateException("Exceeded max size of " + this.maxSize);
		this.values[this.index] = value;
		this.index++;
	}

	@Override
	public void set(int index, long value) {
		this.values[index] = value;
	}

	@Override
	public void clear() {
		this.index = 0;
	}
}
