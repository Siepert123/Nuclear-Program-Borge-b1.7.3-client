package dev.siepert.nuclearprogram.cablenet.node;

import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import dev.siepert.nuclearprogram.cablenet.IReceivingCableNetNode;
import dev.siepert.nuclearprogram.world.te.IEnergyReceiverTE;
import dev.siepert.nuclearprogram.world.te.TileEntityProxy;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class CNNBasicReceiver extends CableNetNode implements IReceivingCableNetNode {
	public CNNBasicReceiver(World world) {
		super(world);
	}

	private TileEntity te = null;
	private TileEntity getTileEntity() {
		if (this.te == null || this.te.isInvalid()) {
			this.te = this.worldObj.getBlockTileEntity(this.x, this.y, this.z);
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
