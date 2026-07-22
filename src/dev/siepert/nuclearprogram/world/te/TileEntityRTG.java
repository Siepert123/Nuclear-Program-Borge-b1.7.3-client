package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.recipe.RTGFuelRecipes;
import net.minecraft.src.*;
import net.minecraftborge.loader.tag.ItemTags;

public class TileEntityRTG extends TileEntity implements IInventory {
	public static final int MAX_ENERGY_STORED = 10_000;

	public TileEntityRTG() {

	}

	public final ItemStack[] inventory = new ItemStack[2];
	public int energy = 0;
	public int depletion = 0;
	public int maxDepletion = 0;
	public int energyPerTick = 0;
	public ItemStack currentlyDepleting = null;

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (this.currentlyDepleting == null && this.inventory[0] != null) {
				RTGFuelRecipes recipes = RTGFuelRecipes.instance();
				RTGFuelRecipes.Recipe recipe = recipes.getRecipe(this.inventory[0]);
				if (recipe != null && (recipe.product == null || this.inventory[1] == null ||
						(ItemTags.matches(this.inventory[1], recipe.product) &&
								this.inventory[1].stackSize + recipe.product.stackSize <= this.inventory[1].getMaxStackSize()))) {
					this.maxDepletion = recipe.duration;
					this.energyPerTick = recipe.production;
					this.currentlyDepleting = this.inventory[0].copy();
					this.currentlyDepleting.stackSize = 1;
					if (--this.inventory[0].stackSize <= 0) {
						this.inventory[0] = null;
					}
					update = true;
				}
			}

			if (this.currentlyDepleting != null) {
				this.depletion++;
				this.energy = Math.min(this.energy + this.energyPerTick, MAX_ENERGY_STORED);
				if (this.depletion >= this.maxDepletion) {
					this.depletion = 0;
					this.maxDepletion = 0;
					this.energyPerTick = 0;
					RTGFuelRecipes.Recipe recipe = RTGFuelRecipes.instance().getRecipe(this.currentlyDepleting);
					this.currentlyDepleting = null;
					if (recipe != null && recipe.product != null) {
						if (this.inventory[1] == null) {
							this.inventory[1] = recipe.product.copy();
						} else {
							this.inventory[1].stackSize += recipe.product.stackSize;
						}
					}
				}
				update = true;
			}
		}

		if (update) this.onInventoryChanged();
	}

	public int getScaledDepletion(int width) {
		return this.depletion * width / this.maxDepletion;
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
		return "Thermoelectric Generator";
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.inventory.length; i++) {
			if (this.inventory[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				this.inventory[i].writeToNBT(compound);
				compound.setByte("slot", (byte) i);
				items.setTag(compound);
			}
		}
		nbt.setTag("Inventory", items);
		nbt.setInteger("energy", this.energy);
		if (this.currentlyDepleting != null) {
			nbt.setInteger("depletion", this.depletion);
			nbt.setTag("CurrentlyDepleting", this.currentlyDepleting.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList items = nbt.getTagList("Inventory");
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound compound = (NBTTagCompound) items.tagAt(i);
			byte slot = compound.getByte("slot");
			if (slot >= 0 && slot < this.inventory.length) {
				this.inventory[slot] = new ItemStack(compound);
			}
		}
		this.energy = nbt.getInteger("energy");
		if (nbt.hasKey("CurrentlyDepleting", NBTBase.COMPOUND)) {
			this.currentlyDepleting = new ItemStack(nbt.getCompoundTag("CurrentlyDepleting"));
			this.depletion = nbt.getInteger("depletion");
			RTGFuelRecipes.Recipe recipe = RTGFuelRecipes.instance().getRecipe(this.currentlyDepleting);
			if (recipe != null) {
				this.maxDepletion = recipe.duration;
				this.energyPerTick = recipe.production;
			} else {
				this.currentlyDepleting = null;
				this.depletion = 0;
			}
		}
	}
}
