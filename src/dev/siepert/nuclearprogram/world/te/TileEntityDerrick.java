package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.FluidInit;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.PipeNetNode;
import dev.siepert.nuclearprogram.weapon.BackendExplosionHandler;
import dev.siepert.nuclearprogram.world.block.BlockCrudeDeposit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;

import java.util.Random;

public class TileEntityDerrick extends TileEntity implements IInventory, IEnergyReceiverTE {

	public TileEntityDerrick() {

	}

	public static final long MAX_ENERGY_STORED = 10_000L;
	public static final long ENERGY_PER_TICK = 250L;
	public long energy = 0L;
	public static final long TANK_CAPACITY = 16_000L;
	public long tankCrudeOil = 0L;
	public long tankNaturalGas = 0L;
	public final ItemStack[] inventory = new ItemStack[5];

	private int age = 0;
	private long totalGasBored = 0L;
	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			this.age++;
			if (this.age % 5 == 0) {
				int nextBlockToDrill = this.getBlockAtDrillTip();

				if (nextBlockToDrill == BlockInit.depositCrude.blockID || nextBlockToDrill == BlockInit.depositBedrockCrude.blockID) {
					int type = this.getMetadataAtDrillTip();
					if (type == BlockCrudeDeposit.OIL) {
						if (this.energy >= ENERGY_PER_TICK && (TANK_CAPACITY - this.tankCrudeOil >= 100) && (TANK_CAPACITY - this.tankNaturalGas >= 5)) {
							update = true;
							this.energy -= ENERGY_PER_TICK;
							this.tankCrudeOil += 100L;
							this.tankNaturalGas += 5L;
							if (this.age % 50 == 0) {
								this.effects();
							}
						}
					} else if (type == BlockCrudeDeposit.GAS) {
						if (this.energy >= ENERGY_PER_TICK && (TANK_CAPACITY - this.tankNaturalGas >= 100)) {
							update = true;
							this.energy -= ENERGY_PER_TICK;
							this.tankNaturalGas += 100L;
							this.totalGasBored += 100L;
							if (this.totalGasBored > 100_000L) {
								this.totalGasBored -= this.worldObj.rand.nextInt(50_000) + 50_000;
								this.sinkhole();
							}
						}
					}
				} else {
					int cost = nextBlockToDrill > 0 ? MathHelper.floor_float(Block.blocksList[nextBlockToDrill].getHardness() * 100) : 0;
					if (cost >= 0) {
						if (this.energy >= cost) {
							update = true;
							this.energy -= cost;
							this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord-(this.getDrillDepth()+1), this.zCoord, BlockInit.derrickPipe.blockID);
						}
					}
				}
				if (this.tankCrudeOil > 0 || this.tankNaturalGas > 0) {
					update = true;
					for (EnumFacing side : EnumFacing.HORIZONTALS) {
						PipeNetNode node = PipeNet.getNode(this.worldObj, this.xCoord + side.getOffsetX() * 2, this.yCoord, this.zCoord + side.getOffsetZ() * 2);
						if (node != null) {
							this.tankCrudeOil = node.pushFluid(FluidInit.crudeOil.fluidID, this.tankCrudeOil, 1);
							this.tankNaturalGas = node.pushFluid(FluidInit.naturalGas.fluidID, this.tankNaturalGas, 1);
						}
					}
				}
			}
		}

		if (update) this.onInventoryChanged();
	}

	private void effects() {
		this.worldObj.playSoundEffect(
				this.xCoord + 0.5, this.yCoord + 5.5, this.zCoord + 0.5,
				"random.splash", 2.5F, 0.7F + this.worldObj.rand.nextFloat() * 0.1F
		);
	}
	private void sinkhole() {
		EntityPlayer player = Minecraft.getTheMinecraft().thePlayer;
		if (player != null && player.getDistance(this.xCoord, this.yCoord, this.zCoord) < 512) {
			BackendExplosionHandler.shockwaveTicks = 40;
		}
		Random rnd = new Random();
		int x, z;
		do {
			x = rnd.nextInt(256) - 128;
			z = rnd.nextInt(256) - 128;
		} while (Math.abs(x) < 5 && Math.abs(z) < 5);

		this.spawnSinkhole(this.xCoord + x, this.zCoord + z);
	}
	private void spawnSinkhole(int x, int z) {
		System.out.println("pretend as if something catastrophic is happening at X: " + x + ", Z: " + z);
	}

	public int cachedDrillDepth = -1;
	public int getDrillDepth() {
		if (this.cachedDrillDepth == -1) {
			this.cachedDrillDepth = 0;
			while (this.worldObj.getBlockId(this.xCoord, this.yCoord-(this.cachedDrillDepth+1), this.zCoord) == BlockInit.derrickPipe.blockID) {
				this.cachedDrillDepth++;
			}
		}
		return this.cachedDrillDepth;
	}
	public int getBlockAtDrillTip() {
		int depth = this.getDrillDepth();
		return this.worldObj.getBlockId(this.xCoord, this.yCoord - (depth+1), this.zCoord);
	}
	public int getMetadataAtDrillTip() {
		int depth = this.getDrillDepth();
		return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - (depth+1), this.zCoord);
	}

	public int getDrillDepthScaled(int h) {
		return (this.getDrillDepth() * h / this.yCoord)+1;
	}
	public int getCrudeOilFillScaled(int h) {
		return Math.toIntExact(this.tankCrudeOil * h / (TANK_CAPACITY+1))+1;
	}
	public int getNaturalGasFillScaled(int h) {
		return Math.toIntExact(this.tankNaturalGas * h / (TANK_CAPACITY+1))+1;
	}
	public int getEnergyScaled(int h) {
		return Math.toIntExact(this.energy * h / (MAX_ENERGY_STORED +1))+1;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("energy", this.energy);
		nbt.setLong("tankCrudeOil", this.tankCrudeOil);
		nbt.setLong("tankNaturalGas", this.tankNaturalGas);
		nbt.setLong("totalGasBored", this.totalGasBored);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.energy = nbt.getLong("energy");
		this.tankCrudeOil = nbt.getLong("tankCrudeOil");
		this.tankNaturalGas = nbt.getLong("tankNaturalGas");
		this.totalGasBored = nbt.getLong("totalGasBored");
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
	public int getPriority() {
		return TileEntityProxy.MACHINE_PRIORITY;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}
	@Override
	public ItemStack decrStackSize(int slot, int count) {
		if (this.inventory[slot] != null) {
			ItemStack stack;
			if (this.inventory[slot].stackSize <= count) {
				stack = this.inventory[slot];
				this.inventory[slot] = null;
			} else {
				stack = this.inventory[slot].splitStack(count);
				if (this.inventory[slot].stackSize == 0) {
					this.inventory[slot] = null;
				}
			}
			return stack;
		} else {
			return null;
		}
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
	}
	@Override
	public String getInvName() {
		return "Derrick";
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
