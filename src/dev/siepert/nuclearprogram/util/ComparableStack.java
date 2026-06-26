package dev.siepert.nuclearprogram.util;

import net.minecraft.src.Item;

public class ComparableStack implements Comparable<ComparableStack> {
	public int itemID;
	public int damage;

	public ComparableStack(int itemID, int damage) {
		this.itemID = itemID;
		this.damage = damage;
	}
	public ComparableStack() {
		this(0, 0);
	}

	public Item getItem() {
		return Item.itemsList[this.itemID];
	}

	public ComparableStack copy() {
		return new ComparableStack(this.itemID, this.damage);
	}

	@Override
	public int compareTo(ComparableStack o) {
		if (this.itemID < o.itemID) return -1;
		if (this.itemID > o.itemID) return 1;
		return Integer.compare(this.damage, o.damage);
	}
}
