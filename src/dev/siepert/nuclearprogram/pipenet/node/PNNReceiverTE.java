package dev.siepert.nuclearprogram.pipenet.node;

import dev.siepert.nuclearprogram.pipenet.IReceivingPipeNetNode;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.te.IFluidReceiverTE;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class PNNReceiverTE extends PipeNetNode implements IReceivingPipeNetNode {
	public PNNReceiverTE(World world) {
		super(world);
		this.fluidType = -1;
	}

	private TileEntity te = null;
	private TileEntity getTileEntity() {
		if (this.te == null || this.te.isInvalid()) {
			this.te = this.worldObj.getBlockTileEntity(this.x, this.y, this.z);
		}
		return this.te;
	}
	private IFluidReceiverTE cast() {
		return this.getTileEntity() != null ? (IFluidReceiverTE) this.getTileEntity() : TileEntityProxy.DUMMY_FLUID_RECEIVER;
	}

	@Override
	public long getCapacity(int fluidID, int bar) {
		return this.cast().getFluidCapacity(fluidID, bar);
	}
	@Override
	public long getRemainingCapacity(int fluidID, int bar) {
		return this.cast().getRemainingFluidCapacity(fluidID, bar);
	}
	@Override
	public long addFluid(int fluidID, long amount, int bar) {
		return this.cast().addFluid(fluidID, amount, bar);
	}
	@Override
	public int getPriority() {
		return this.cast().getPriority();
	}
}
