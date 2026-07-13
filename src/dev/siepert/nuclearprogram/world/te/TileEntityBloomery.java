package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.world.block.BlockBloomery;
import net.minecraft.src.*;
import net.minecraftborge.loader.IFurnace;
import net.minecraftborge.loader.ITickingTile;

import java.util.Arrays;

public class TileEntityBloomery extends TileEntity implements IInventory, IFurnace, ITickingTile {
	private final ItemStack[] inventory = new ItemStack[4]; // 0 - fuel; 1 - iron ore; 2 - result; 3 - slag
	public int fuelHeap = 0;
	public int recipeTicks = 0;
	public int cooldown = 0;
	public static final int COAL_TICKS = 500;
	public static final int RECIPE_TICKS = COAL_TICKS * 4;
	public static final int MAX_FUEL_HEAP = RECIPE_TICKS * 2;

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
		if (stack != null && stack.stackSize > Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit())) {
			stack.stackSize = Math.min(stack.getMaxStackSize(), this.getInventoryStackLimit());
		}
	}

	@Override
	public String getInvName() {
		return "Bloomery";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	public int pipeLength = 0;
	private boolean fx = true;
	@Override
	public void updateEntity() {
		if ((this.fx = !this.fx) && (this.worldObj != null && this.worldObj.multiplayerWorld ? (this.getBlockType() == BlockInit.bloomeryLit) : this.visuallyBurning())) {
			this.worldObj.spawnParticle("nuclear_program/pollution",
					this.xCoord + 0.5, this.yCoord + this.pipeLength + 0.875, this.zCoord + 0.5,
					0.25, 0.0, 0.0
			);
		}

		boolean update = false;
		if (!this.worldObj.multiplayerWorld) {
			if (this.fuelHeap + COAL_TICKS <= MAX_FUEL_HEAP) {
				if (this.inventory[0] != null && this.inventory[0].itemID == Item.coal.shiftedIndex) {
					this.inventory[0].stackSize--;
					if (this.inventory[0].stackSize <= 0) this.inventory[0] = null;
					this.fuelHeap += COAL_TICKS;
					update = true;
				}
			}

			boolean wasHot = this.visuallyBurning();
			if (this.canSmelt()) {
				update = true;
				this.cooldown = 3;
				this.fuelHeap--;
				if (++this.recipeTicks >= RECIPE_TICKS) {
					this.recipeTicks = 0;

					this.inventory[1].stackSize--;
					if (this.inventory[1].stackSize == 0) this.inventory[1] = null;

					if (this.inventory[2] == null) this.inventory[2] = new ItemStack(ItemInit.ingotSteel);
					else this.inventory[2].stackSize++;
					if (this.inventory[3] == null) this.inventory[3] = new ItemStack(Block.gravel);
					else this.inventory[3].stackSize++;
				}
			} else if (this.recipeTicks > 0) {
				update = true;
				this.recipeTicks--;
				if (this.cooldown > 0) this.cooldown--;
			} else if (this.cooldown > 0) {
				update = true;
				this.cooldown--;
			}

			if (wasHot != this.visuallyBurning()) {
				BlockBloomery.updateFurnaceBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.visuallyBurning());
			}
		}
		if (update) this.onInventoryChanged();
	}

	public boolean visuallyBurning() {
		return this.cooldown > 0;
	}
	private boolean canSmelt() {
		if (this.pipeLength == 0) return false;
		if (this.fuelHeap == 0) return false;
		return (this.inventory[2] == null || this.inventory[2].stackSize < this.getInventoryStackLimit())
				&& (this.inventory[3] == null || this.inventory[3].stackSize < this.getInventoryStackLimit())
				&& (this.inventory[1] != null && this.inventory[1].itemID == Block.oreIron.blockID);
	}

	public void updatePipeLen() {
		int y = 1;
		while (this.worldObj.getBlockId(this.xCoord, this.yCoord + y, this.zCoord) == BlockInit.bloomeryPipe.blockID) y++;
		this.pipeLength = y - 1;
	}

	public int getBurnTime(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(this, stack);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		Arrays.fill(this.inventory, null);
		NBTTagList items = nbt.getTagList("Inventory");
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound compound = (NBTTagCompound) items.tagAt(i);
			byte slot = compound.getByte("slot");
			if (slot >= 0 && slot < this.getSizeInventory()) {
				this.inventory[slot] = new ItemStack(compound);
			}
		}

		this.pipeLength = nbt.getShort("pipeLen");
		this.fuelHeap = nbt.getInteger("fuelHeap");
		this.recipeTicks = nbt.getShort("recipeTicks");
		this.cooldown = nbt.getByte("cooldown");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); i++) {
			if (this.inventory[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("slot", (byte) i);
				this.inventory[i].writeToNBT(compound);
				items.setTag(compound);
			}
		}
		nbt.setTag("Inventory", items);

		nbt.setShort("pipeLen", (short) this.pipeLength);
		nbt.setInteger("fuelHeap", this.fuelHeap);
		nbt.setShort("recipeTicks", (short) this.recipeTicks);
		nbt.setByte("cooldown", (byte) this.cooldown);
	}

	@Override
	public World getFurnaceWorld() {
		return this.worldObj;
	}
	@Override
	public int getFurnaceX() {
		return this.xCoord;
	}
	@Override
	public int getFurnaceY() {
		return this.yCoord;
	}
	@Override
	public int getFurnaceZ() {
		return this.zCoord;
	}
}
