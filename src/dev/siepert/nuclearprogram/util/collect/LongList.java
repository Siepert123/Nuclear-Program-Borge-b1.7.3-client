package dev.siepert.nuclearprogram.util.collect;

public interface LongList {
	long get(int index);
	int indexOf(long value);
	int lastIndexOf(long value);
	int size();
	void add(long value);
	void set(int index, long value);
	void clear();
}
