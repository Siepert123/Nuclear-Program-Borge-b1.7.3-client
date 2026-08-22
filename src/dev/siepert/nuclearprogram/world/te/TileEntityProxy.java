package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.src.TileEntity;

public abstract class TileEntityProxy extends TileEntity {
	public static TileEntityProxy create(boolean fluid, boolean energy) {
		if (fluid) {
			return energy ? new Proxy11() : new Proxy10();
		} else {
			return energy ? new Proxy01() : new Proxy00();
		}
	}

	protected int[] core = null;
	protected int[] getCorePos() {
		if (this.core == null) {
			this.core = new int[]{this.xCoord, this.yCoord, this.zCoord};
			((BlockMulti)this.getBlockType()).findCore(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.core);
		}
		return this.core;
	}

	private TileEntityProxy() {}

	public static final class Proxy00 extends TileEntityProxy {
		public Proxy00() {}
	}
	public static final class Proxy01 extends TileEntityProxy {
		public Proxy01() {}
	}
	public static final class Proxy10 extends TileEntityProxy {
		public Proxy10() {}
	}
	public static final class Proxy11 extends TileEntityProxy {
		public Proxy11() {}
	}
}
