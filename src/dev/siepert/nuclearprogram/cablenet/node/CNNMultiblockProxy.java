package dev.siepert.nuclearprogram.cablenet.node;

import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import dev.siepert.nuclearprogram.cablenet.IReceivingCableNetNode;
import dev.siepert.nuclearprogram.world.te.IEnergyReceiverTE;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.World;

public class CNNMultiblockProxy extends CableNetNode implements IReceivingCableNetNode {
	public CNNMultiblockProxy(World world) {
		super(world);
	}

	private TileEntityProxy te = null;
	private TileEntityProxy getTileEntity() {
		if (this.te == null || this.te.isInvalid()) {
			this.te = (TileEntityProxy) this.worldObj.getBlockTileEntity(this.x, this.y, this.z);
		}
		return this.te;
	}
	private IEnergyReceiverTE cast() {
		return this.getTileEntity() != null ? (IEnergyReceiverTE) this.getTileEntity() : TileEntityProxy.DUMMY_ENERGY_RECEIVER;
	}

	@Override
	public long getCapacity() {
		return this.cast().getEnergyCapacity();
	}
	@Override
	public long getRemainingCapacity() {
		return this.cast().getRemainingEnergyCapacity();
	}
	@Override
	public long addEnergy(long amount) {
		return this.cast().addEnergy(amount);
	}
	@Override
	public int getPriority() {
		return this.cast().getPriority();
	}
}
