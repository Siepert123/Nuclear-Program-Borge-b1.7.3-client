package dev.siepert.nuclearprogram.util;

import dev.siepert.nuclearprogram.world.te.TileEntityRBMKColumn;

import java.util.Comparator;

public class RBMKComparator implements Comparator<TileEntityRBMKColumn> {
	@Override
	public int compare(TileEntityRBMKColumn o1, TileEntityRBMKColumn o2) {
		TileEntityRBMKColumn.Type t1 = o1.type;
		TileEntityRBMKColumn.Type t2 = o2.type;
		return t1.compareTo(t2);
	}
}
