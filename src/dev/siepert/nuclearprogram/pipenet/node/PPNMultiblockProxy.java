package dev.siepert.nuclearprogram.pipenet.node;

import dev.siepert.nuclearprogram.pipenet.IReceivingPipeNetNode;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.te.IFluidReceiverTE;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.World;

public class PPNMultiblockProxy extends PipeNetNode implements IReceivingPipeNetNode {
	public PPNMultiblockProxy(World world) {
		super(world);
		this.fluidType = -1;
	}

	private TileEntityProxy te = null;
	private TileEntityProxy getTileEntity() {
		if (this.te == null || this.te.isInvalid()) {
			this.te = (TileEntityProxy) this.worldObj.getBlockTileEntity(this.x, this.y, this.z);
		}
		return this.te;
	}
	private IFluidReceiverTE cast() {
		return this.getTileEntity() != null ? (IFluidReceiverTE) this.getTileEntity() : TileEntityProxy.DUMMY_FLUID_RECEIVER;
	}

	@Override
	public long getCapacity(int fluidID, int bar) {
		return this.cast().getCapacity(fluidID, bar);
	}
	@Override
	public long getRemainingCapacity(int fluidID, int bar) {
		return this.cast().getRemainingCapacity(fluidID, bar);
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
