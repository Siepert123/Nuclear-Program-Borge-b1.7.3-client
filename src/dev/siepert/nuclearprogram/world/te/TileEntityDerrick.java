package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

public class TileEntityDerrick extends TileEntity implements IFluidReceiverTE {

	public TileEntityDerrick() {

	}

	private int age = 0;
	private boolean sending = false;
	@Override
	public void updateEntity() {
		if (this.age++ % 20 == 0) {
			this.sending = true;
			for (EnumFacing side : EnumFacing.HORIZONTALS) {
				PipeNetNode node = PipeNet.getNode(this.worldObj, this.xCoord + side.getOffsetX() * 2, this.yCoord, this.zCoord + side.getOffsetZ() * 2);
				if (node != null) {
					node.pushFluid(FluidInit.water.fluidID, 1000L, 1);
				}
			}
			this.sending = false;
		}
	}

	@Override
	public long getCapacity(int fluidType, int bar) {
		return 0;
	}
	@Override
	public long getRemainingCapacity(int fluidType, int bar) {
		return 0;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (this.sending) return amount;
		System.out.println("Consumed " + amount + "mB of " + StringTranslate.getInstance().translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[fluidType])));
		return 0;
	}
	@Override
	public int getPriority() {
		return 0;
	}
}
