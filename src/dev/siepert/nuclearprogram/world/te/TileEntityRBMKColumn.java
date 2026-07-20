package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.RBMKComparator;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftborge.loader.EnumFacing;

import java.util.*;

public class TileEntityRBMKColumn extends TileEntity {
	public TileEntityRBMKColumn() {

	}

	public final Type type = Type.determine(this);

	protected boolean firstTick = true;
	protected TileEntityRBMKColumn[] cachedNeighbours = new TileEntityRBMKColumn[4];
	protected int neighbourCount = 0;
	public double heat = 20.0;

	public void meltdown() {
		List<TileEntityRBMKColumn> affected = new ArrayList<>();
		this.propagateMeltdown(affected);
		Collections.shuffle(affected, new Random(2137L));
		affected.sort(new RBMKComparator());

		affected.forEach(TileEntityRBMKColumn::doMeltdown);
		affected.forEach(TileEntityRBMKColumn::doMeltdownFX);
	}

	protected void propagateMeltdown(List<TileEntityRBMKColumn> affected) {
		affected.add(this);
		int idx;

		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			idx = side.getHorizontalIndex();

			if (this.cachedNeighbours[idx] != null) {
				if (this.cachedNeighbours[idx].isInvalid()) this.cachedNeighbours[idx] = null; // should not happen but account for it regardless
				else if (!affected.contains(this.cachedNeighbours[idx])) this.cachedNeighbours[idx].propagateMeltdown(affected);
			}
		}
	}

	protected final void doMeltdown() {
		this.heat = 20.0;
		Random rnd = this.worldObj.rand;
		for (int i = 0; i < 7; i++) {
			if (rnd.nextFloat() > 0.25F) {
				this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord+i, this.zCoord, BlockInit.nukestone.blockID, rnd.nextInt(4));
			}
		}
	}
	protected void doMeltdownFX() {

	}

	@Override
	public void updateEntity() {
		if (this.firstTick) {
			this.initialize();
			this.firstTick = false;
		}

		if (this.heat < 20.05) {
			this.heat = 20.0;
		}

		if (this.heat > 1500.0) {
			this.meltdown();
		} else {
			this.spreadHeat();
			this.logicTick();
		}


		this.onInventoryChanged();
	}

	protected void logicTick() {

	}

	protected final void spreadHeat() {
		if (this.neighbourCount > 0 && this.heat > 20.0) {
			double dissipate = (this.heat - 20) * 0.05;
			for (int i = 0; i < 4; i++) {
				TileEntityRBMKColumn te = this.cachedNeighbours[i];
				if (te != null) te.heat += dissipate;
			}
			this.heat -= dissipate * this.neighbourCount;
		}
	}

	protected void initialize() {
		this.updateNeighbourColumns();
	}

	public void updateNeighbourColumns() {
		int ox, oz;
		this.neighbourCount = 0;
		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			int idx = side.getHorizontalIndex();
			if (this.cachedNeighbours[idx] != null && this.cachedNeighbours[idx].isInvalid()) {
				this.cachedNeighbours[idx] = null;
			}

			ox = this.xCoord + side.getOffsetX();
			oz = this.zCoord + side.getOffsetZ();

			TileEntity te = this.worldObj.getBlockTileEntity(ox, this.yCoord, oz);
			if (te instanceof TileEntityRBMKColumn) {
				this.cachedNeighbours[idx] = (TileEntityRBMKColumn) te;
				this.neighbourCount++;
			}
		}
	}

	public void debug(List<String> props) {
		props.add("heat: " + this.heat);
		props.add("neighbours: " + this.neighbourCount);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.heat = nbt.getDouble("heat");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setDouble("heat", this.heat);
	}

	public enum Type {
		BLANK,
		FUEL,
		BOILER,
		OTHER;

		public static Type determine(TileEntityRBMKColumn te) {
			return determine(te.getClass());
		}
		public static Type determine(Class<? extends TileEntityRBMKColumn> clazz) {
			if (clazz == TileEntityRBMKColumn.class) return BLANK;
			if (clazz == TileEntityRBMKFuel.class) return FUEL;
			if (clazz == TileEntityRBMKBoiler.class) return BOILER;
			return OTHER;
		}
	}
}
