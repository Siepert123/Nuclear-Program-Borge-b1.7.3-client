package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.FluidInit;
import net.minecraft.src.*;

import java.util.Objects;

public class TileEntityOilDistilleryController extends TileEntityMachineBase implements IInventory, IEnergyReceiverTE, IFluidReceiverTE {
	// These are genuinely just random values,
	// so I should probably rebalance these at some point.
	public static final int[] DISTRIBUTIONS_3 = {40, 30, 30};
	public static final int[] DISTRIBUTIONS_4 = {30, 20, 30, 20};
	public static final int[] DISTRIBUTIONS_6 = {20, 20, 10, 20, 20, 10};

	public TileEntityOilDistilleryController() {

	}

	public static final long MAX_ENERGY_STORED = 16_000L;
	public static final long BASE_ENERGY_CONSUMPTION = 1000L;
	public long energy = 0L;
	public static final long TANK_CAPACITY = 8_000L;
	public long tankCrudeOil = 0L;

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			distillation:
			if (this.tankCrudeOil >= 100L) {
				switch (this.getSegmentCount()) {
					case 3:
						if (this.energy >= BASE_ENERGY_CONSUMPTION) {
							this.cacheSegments(3);
							if (this.segmentCache[0].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_3[0]) break distillation;
							if (this.segmentCache[1].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_3[1]) break distillation;
							if (this.segmentCache[2].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_3[2]) break distillation;

							update = true;
							this.energy -= BASE_ENERGY_CONSUMPTION;
							this.tankCrudeOil -= 100L;
							this.segmentCache[0].tank += DISTRIBUTIONS_3[0];
							this.segmentCache[1].tank += DISTRIBUTIONS_3[1];
							this.segmentCache[2].tank += DISTRIBUTIONS_3[2];
						}
						break;
					case 4:
						if (this.energy >= BASE_ENERGY_CONSUMPTION * 2) {
							this.cacheSegments(4);
							if (this.segmentCache[0].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_4[0]) break distillation;
							if (this.segmentCache[1].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_4[1]) break distillation;
							if (this.segmentCache[2].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_4[2]) break distillation;
							if (this.segmentCache[3].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_4[3]) break distillation;

							update = true;
							this.energy -= BASE_ENERGY_CONSUMPTION * 2;
							this.tankCrudeOil -= 100L;
							this.segmentCache[0].tank += DISTRIBUTIONS_4[0];
							this.segmentCache[1].tank += DISTRIBUTIONS_4[1];
							this.segmentCache[2].tank += DISTRIBUTIONS_4[2];
							this.segmentCache[3].tank += DISTRIBUTIONS_4[3];
						}
						break;
					case 6:
						if (this.energy >= BASE_ENERGY_CONSUMPTION * 4) {
							this.cacheSegments(6);
							if (this.segmentCache[0].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_6[0]) break distillation;
							if (this.segmentCache[1].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_6[1]) break distillation;
							if (this.segmentCache[2].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_6[2]) break distillation;
							if (this.segmentCache[3].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_6[3]) break distillation;
							if (this.segmentCache[4].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_6[4]) break distillation;
							if (this.segmentCache[5].tank > TileEntityOilDistillerySegment.TANK_CAPACITY - DISTRIBUTIONS_6[5]) break distillation;

							update = true;
							this.energy -= BASE_ENERGY_CONSUMPTION * 4;
							this.tankCrudeOil -= 100L;
							this.segmentCache[0].tank += DISTRIBUTIONS_6[0];
							this.segmentCache[1].tank += DISTRIBUTIONS_6[1];
							this.segmentCache[2].tank += DISTRIBUTIONS_6[2];
							this.segmentCache[3].tank += DISTRIBUTIONS_6[3];
							this.segmentCache[4].tank += DISTRIBUTIONS_6[4];
							this.segmentCache[5].tank += DISTRIBUTIONS_6[5];
						}
						break;
				}
			}
		}

		if (update) this.onInventoryChanged();
	}

	public int cachedSegmentCount = -1;
	public TileEntityOilDistillerySegment[] segmentCache = null;
	public int getSegmentCount() {
		if (this.cachedSegmentCount == -1) {
			this.cachedSegmentCount = 0;
			while (this.worldObj.getBlockId(this.xCoord, this.yCoord + (this.cachedSegmentCount*2) + 1, this.zCoord) == BlockInit.oilDistillerySegment.blockID
					&& this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + (this.cachedSegmentCount*2) + 1, this.zCoord) >= 12) {
				this.cachedSegmentCount++;
			}
			this.segmentCache = new TileEntityOilDistillerySegment[this.cachedSegmentCount];
			this.setSegmentFluidTypes();
		}
		return this.cachedSegmentCount;
	}
	public boolean isValidSegmentCount() {
		int count = this.getSegmentCount();
		return count == 3 || count == 4 || count == 6;
	}
	private void setSegmentFluidTypes() {
		int count = this.getSegmentCount();
		int[] types = new int[count];
		switch (count) {
			case 3:
				types[0] = FluidInit.heavyOil.fluidID;
				types[1] = FluidInit.naphtha.fluidID;
				types[2] = FluidInit.petroleumGas.fluidID;
				break;
			case 4:
				types[0] = FluidInit.heavyOil.fluidID;
				types[1] = FluidInit.diesel.fluidID;
				types[2] = FluidInit.naphtha.fluidID;
				types[3] = FluidInit.petroleumGas.fluidID;
				break;
			case 6:
				types[0] = FluidInit.heavyOil.fluidID;
				types[1] = FluidInit.diesel.fluidID;
				types[2] = FluidInit.kerosene.fluidID;
				types[3] = FluidInit.naphtha.fluidID;
				types[4] = FluidInit.gasoline.fluidID;
				types[5] = FluidInit.petroleumGas.fluidID;
				break;
		}
		for (int i = 0; i < count; i++) {
			TileEntityOilDistillerySegment te = this.getSegment(i);
			Objects.requireNonNull(te, "segment #" + (i+1));
			if (te.fluidType == types[i]) continue;
			if (te.fluidType != 0L) te.tank = 0L;
			te.fluidType = types[i];
			te.onInventoryChanged();
		}
	}
	public TileEntityOilDistillerySegment getSegment(int i) {
		if (this.segmentCache == null) return null;
		if (this.segmentCache[i] == null || this.segmentCache[i].isInvalid()) {
			this.segmentCache[i] = (TileEntityOilDistillerySegment) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + i*2 + 1, this.zCoord);
		}
		return this.segmentCache[i];
	}
	private void cacheSegments(int range) {
		for (int i = 0; i < range; i++) this.getSegment(i);
	}

	public int getCrudeOilFillScaled(int h) {
		return Math.toIntExact(this.tankCrudeOil * h / (TANK_CAPACITY+1))+1;
	}
	public int getEnergyScaled(int h) {
		return Math.toIntExact(this.energy * h / (MAX_ENERGY_STORED +1))+1;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("energy", this.energy);
		nbt.setLong("tankCrudeOil", this.tankCrudeOil);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.energy = nbt.getLong("energy");
		this.tankCrudeOil = nbt.getLong("tankCrudeOil");
	}

	@Override
	public long getEnergyCapacity() {
		return MAX_ENERGY_STORED;
	}
	@Override
	public long getRemainingEnergyCapacity() {
		return MAX_ENERGY_STORED - this.energy;
	}
	@Override
	public long addEnergy(long amount) {
		long remain = amount - this.getRemainingEnergyCapacity();
		if (remain <= 0) {
			this.energy += amount;
			return 0L;
		} else {
			this.energy = MAX_ENERGY_STORED;
			return remain;
		}
	}
	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		return fluidType == FluidInit.crudeOil.fluidID && bar == 1 ? TANK_CAPACITY : 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		return fluidType == FluidInit.crudeOil.fluidID && bar == 1 ? (TANK_CAPACITY - this.tankCrudeOil) : 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (fluidType == FluidInit.crudeOil.fluidID && bar == 1) {
			long remain = amount - (TANK_CAPACITY - this.tankCrudeOil);
			if (remain <= 0) {
				this.tankCrudeOil += amount;
				return 0L;
			} else {
				this.tankCrudeOil = TANK_CAPACITY;
				return remain;
			}
		} else return amount;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.MACHINE_PRIORITY;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}
	@Override
	public ItemStack getStackInSlot(int slot) {
		return null;
	}
	@Override
	public ItemStack decrStackSize(int slot, int count) {
		return null;
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {

	}
	@Override
	public String getInvName() {
		return "Oil Distillery";
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}
}
