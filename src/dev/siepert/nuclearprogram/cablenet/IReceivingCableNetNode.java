package dev.siepert.nuclearprogram.cablenet;

public interface IReceivingCableNetNode extends Comparable<IReceivingCableNetNode> {
	long getCapacity();
	long getRemainingCapacity();
	long addEnergy(long amount);
	int getPriority();

	@Override
	default int compareTo(IReceivingCableNetNode o) {
		if (this == o) return 0;
		if (this.getPriority() != o.getPriority()) return Integer.compare(this.getPriority(), o.getPriority());
		return Integer.compare(System.identityHashCode(this), System.identityHashCode(o));
	}
}
