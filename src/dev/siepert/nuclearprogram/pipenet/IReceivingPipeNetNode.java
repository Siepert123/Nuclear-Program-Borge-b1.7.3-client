package dev.siepert.nuclearprogram.pipenet;

public interface IReceivingPipeNetNode extends Comparable<IReceivingPipeNetNode> {
	long getCapacity(int fluidID, int bar);
	long getRemainingCapacity(int fluidID, int bar);
	long addFluid(int fluidID, long amount, int bar);
	int getPriority();

	@Override
	default int compareTo(IReceivingPipeNetNode o) {
		if (this == o) return 0;
		if (this.getPriority() != o.getPriority()) return Integer.compare(this.getPriority(), o.getPriority());
		return Integer.compare(System.identityHashCode(this), System.identityHashCode(o));
	}
}
