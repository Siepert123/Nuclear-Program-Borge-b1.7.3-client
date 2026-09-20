package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import net.minecraft.client.Minecraft;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.TrackedSound;

import java.util.Objects;

public class TileEntityAirStove extends TileEntityMachineBase implements IFluidReceiverTE {
	public int fluidType = FluidInit.creosote.fluidID;
	public static final long TANK_CAPACITY_FUEL = 8000L;
	public static final long TANK_CAPACITY_AIR = 64000L;
	public long tankHeatSource = 0L;
	public long tankAirIn = 0L;
	public long tankAirOut = 0L;

	public TileEntityAirStove() {

	}

	private boolean boiling = false;
	private TrackedSound loop = null;

	private void play() {
		if (this.boiling) return;
		this.boiling = true;

		if (this.loop == null) {
			this.loop = Minecraft.getTheMinecraft().sndManager.playTrackedSound("machine.boiler",
					this.xCoord + 0.5F, this.yCoord + 1.5F, this.zCoord + 0.5F,
					1.0F, 1.0F, true
			);
			if (this.loop == null) this.loop = NuclearProgram.getLastTrackedSound();
			Objects.requireNonNull(this.loop, "TileEntityAirStove.loop");
		}

		this.loop.setVolume(1.0F);
	}
	private void unplay() {
		if (!this.boiling) return;
		this.boiling = false;

		this.loop.setVolume(0.0F);
	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			int lim = 64;
			while (this.tankAirIn >= 100L && (TANK_CAPACITY_AIR - this.tankAirOut) >= 100L && this.tankHeatSource > 0L && lim-- > 0) {
				update = true;
				this.tankHeatSource--;
				this.tankAirIn -= 100L;
				this.tankAirOut += 100L;
			}
			if (lim == 64) this.unplay();
			else this.play();
			if (this.tankAirOut > 0L) {
				long old = this.tankAirOut;
				EnumFacing side = EnumFacing.VALUES[this.getBlockMetadata() - BlockMulti.OFFSET];
				PipeNetNode node1 = PipeNet.getNode(this.worldObj, this.xCoord + side.getOffsetZ() * 3, this.yCoord + 2, this.zCoord + side.getOffsetX() * 3);
				PipeNetNode node2 = PipeNet.getNode(this.worldObj, this.xCoord - side.getOffsetZ() * 3, this.yCoord + 2, this.zCoord - side.getOffsetX() * 3);
				if (node1 != null) this.tankAirOut = node1.pushFluid(FluidInit.airBlast.fluidID, this.tankAirOut, 1);
				if (node2 != null) this.tankAirOut = node2.pushFluid(FluidInit.airBlast.fluidID, this.tankAirOut, 1);
				if (old != this.tankAirOut) update = true;
			}
		}

		if (update) this.onInventoryChanged();
	}

	public void setFuelType(int fluidID) {
		if (this.worldObj.multiplayerWorld) {
		} else if (fluidID == FluidInit.naturalGas.fluidID) {
			this.fluidType = fluidID;
			this.onInventoryChanged();
		} else if (fluidID == FluidInit.creosote.fluidID) {
			this.fluidType = fluidID;
			this.onInventoryChanged();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("fluidType", this.fluidType);
		nbt.setLong("tankHeatSource", this.tankHeatSource);
		nbt.setLong("tankAirIn", this.tankAirIn);
		nbt.setLong("tankAirOut", this.tankAirOut);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.fluidType = nbt.getInteger("fluidType");
		this.tankHeatSource = nbt.getLong("tankHeatSource");
		this.tankAirIn = nbt.getLong("tankAirIn");
		this.tankAirOut = nbt.getLong("tankAirOut");
	}

	@Override
	public long getFluidCapacity(int fluidType, int bar) {
		if (bar != 1) return 0L;
		if (fluidType == this.fluidType) return TANK_CAPACITY_FUEL;
		if (fluidType == FluidInit.air.fluidID) return TANK_CAPACITY_AIR;
		return 0L;
	}
	@Override
	public long getRemainingFluidCapacity(int fluidType, int bar) {
		if (bar != 1) return 0L;
		if (fluidType == this.fluidType) return TANK_CAPACITY_FUEL - this.tankHeatSource;
		if (fluidType == FluidInit.air.fluidID) return TANK_CAPACITY_AIR - this.tankAirIn;
		return 0L;
	}
	@Override
	public long addFluid(int fluidType, long amount, int bar) {
		if (bar != 1) return amount;
		if (fluidType == this.fluidType) {
			this.onInventoryChanged();
			long remain = amount - (TANK_CAPACITY_FUEL - this.tankHeatSource);
			if (remain <= 0L) {
				this.tankHeatSource += amount;
				return 0L;
			} else {
				this.tankHeatSource = TANK_CAPACITY_FUEL;
				return remain;
			}
		}
		if (fluidType == FluidInit.air.fluidID) {
			this.onInventoryChanged();
			long remain = amount - (TANK_CAPACITY_AIR - this.tankAirIn);
			if (remain <= 0L) {
				this.tankAirIn += amount;
				return 0L;
			} else {
				this.tankAirIn = TANK_CAPACITY_AIR;
				return remain;
			}
		}
		return amount;
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.ENGINE_PRIORITY;
	}
}
