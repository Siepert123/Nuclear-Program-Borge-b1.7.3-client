package dev.siepert.nuclearprogram.pipenet.node;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.IReceivingPipeNetNode;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.World;

import java.util.Random;

public class PNNFlareBulb extends PipeNetNode implements IReceivingPipeNetNode {
	private static final Random rnd = new Random(2137L);

	public PNNFlareBulb(World world) {
		super(world);
	}

	@Override
	public long getCapacity(int fluidID, int bar) {
		return 50;
	}

	@Override
	public long getRemainingCapacity(int fluidID, int bar) {
		return 50;
	}

	@Override
	public long addFluid(int fluidID, long amount, int bar) {
		if (amount <= 0) return 0;
		if (fluidID == FluidInit.naturalGas.fluidID) {
			this.worldObj.spawnParticle("flame",
					this.x + rnd.nextFloat(), this.y + rnd.nextFloat(), this.z + rnd.nextFloat(),
					0.0, 0.0, 0.0
			);
			return Math.max(amount - 50, 0);
		} else return amount;
	}

	@Override
	public int getPriority() {
		return TileEntityProxy.VOID_PRIORITY;
	}

	@Override
	public boolean canConnect(int fluidType) {
		return fluidType == FluidInit.naturalGas.fluidID;
	}
}
