package dev.siepert.nuclearprogram.util.collect;

public interface IntList {
	int get(int index);
	int indexOf(int value);
	int lastIndexOf(int value);
	int size();
	void add(int value);
	void set(int index, int value);
	void clear();
}
