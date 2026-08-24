package dev.siepert.nuclearprogram.world.fluid;

import java.util.ArrayList;
import java.util.List;

public final class FluidStack {
	public int fluidType;
	public long amount;
	public int bar;

	public FluidStack(int fluidType, long amount, int bar) {
		this.fluidType = fluidType;
		this.amount = amount;
		this.bar = bar;
	}
	public FluidStack(int fluidType, long amount) {
		this(fluidType, amount, 1);
	}
	public FluidStack() {
		this(0, 0);
	}

	public FluidStack copy() {
		return new FluidStack(this.fluidType, this.amount, this.bar);
	}
	public FluidStack pooled() {
		return GLOBAL_POOL.get(this.fluidType, this.amount, this.bar);
	}

	public static final Pool GLOBAL_POOL = new Pool();
	public static final class Pool {
		private final List<FluidStack> pool = new ArrayList<>();
		private int index = 0;

		public FluidStack get(int fluidType, long amount, int bar) {
			if (this.pool.size() == this.index) this.pool.add(new FluidStack());
			FluidStack stack = this.pool.get(this.index++);
			stack.fluidType = fluidType;
			stack.amount = amount;
			stack.bar = bar;
			return stack;
		}

		public void reset() {
			this.index = 0;
		}
		public void drain() {
			this.reset();
			this.pool.clear();
		}

		public int getSize() {
			return this.pool.size();
		}
		public int getIndex() {
			return this.index;
		}
	}
}
