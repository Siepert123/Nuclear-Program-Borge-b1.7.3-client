package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.recipe.template.MachineRecipesManager;
import dev.siepert.nuclearprogram.recipe.template.RecipeGeneric;
import dev.siepert.nuclearprogram.recipe.template.RecipesManufactory;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.capability.Capability;
import net.minecraftborge.loader.capability.CapabilityItemHandler;
import net.minecraftborge.loader.capability.IItemHandlerModifiable;

import java.util.Arrays;

public class TileEntityManufactory extends TileEntityMachineBase implements IInventory, IItemHandlerModifiable, IEnergyReceiverTE {
	public static final String WORKSTATION = "Manufactory";
	public static final MachineRecipesManager<RecipeGeneric, RecipesManufactory> MANAGER
			= new MachineRecipesManager<>(RecipesManufactory.INSTANCE).setSlots(0, 9);

	public final ItemStack[] inventory = new ItemStack[10];
	public static final long MAX_ENERGY = 100_000L;
	public long energy = 0L;
	public RecipeGeneric recipe;
	public int progress = 0;

	public TileEntityManufactory() {

	}

	@Override
	public void updateEntity() {
		boolean update = false;

		if (!this.worldObj.multiplayerWorld) {
			if (MANAGER.matches(this.recipe, this.energy, this.inventory) && this.hasOutputSpace()) {
				if (this.progress > 0 || this.energy >= Math.min(this.recipe.recipeTicks, 10) * this.recipe.energyCost) {
					update = true;
					this.energy -= this.recipe.energyCost;
					this.progress++;
					if (this.progress >= this.recipe.recipeTicks) {
						this.progress = 0;
						if (this.inventory[9] == null) this.inventory[9] = this.recipe.itemsOut.get(0).copy();
						else this.inventory[9].stackSize += this.recipe.itemsOut.get(0).stackSize;
						MANAGER.extract(this.recipe, this.inventory);
					}
				}
			} else if (this.progress != 0) {
				update = true;
				this.progress = 0;
			}
		}

		if (update) this.onInventoryChanged();
	}

	private boolean hasOutputSpace() {
		if (this.inventory[9] == null) return true;
		if (this.recipe == null) return false;
		if (this.inventory[9].stackSize + this.recipe.itemsOut.get(0).stackSize > this.inventory[9].getMaxStackSize()) return false;
		return this.inventory[9].isItemEqual(this.recipe.itemsOut.get(0));
	}
	public int getEnergyScaled(int h) {
		return Math.toIntExact((this.energy * h / (MAX_ENERGY + 1)) + 1);
	}
	public int getProgressScaled(int w) {
		if (this.recipe == null) return 0;
		return (this.progress * w / (this.recipe.recipeTicks+1))+1;
	}

	@Override
	public void setRecipeID(int recipeID) {
		this.recipe = MANAGER.getRecipe(recipeID);
		this.onInventoryChanged();
	}
	@Override
	public MachineRecipesManager<?, ?> getRecipesManager() {
		return MANAGER;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("energy", this.energy);
		if (this.recipe != null) {
			nbt.setString("recipe", this.recipe.name);
		}
		nbt.setInteger("progress", this.progress);

		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.getSlots(); i++) {
			if (this.inventory[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("slot", (byte) i);
				this.inventory[i].writeToNBT(compound);
				items.setTag(compound);
			}
		}
		nbt.setTag("Inventory", items);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.energy = nbt.getLong("energy");
		if (nbt.hasKey("recipe")) {
			this.recipe = MANAGER.getRecipe(nbt.getString("recipe"));
		} else this.recipe = null;
		this.progress = nbt.getInteger("progress");

		Arrays.fill(this.inventory, null);
		NBTTagList items = nbt.getTagList("Inventory");
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound compound = (NBTTagCompound) items.tagAt(i);
			byte slot = compound.getByte("slot");
			if (slot >= 0 && slot < this.getSlots()) {
				this.inventory[slot] = new ItemStack(compound);
			}
		}
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		this.inventory[slot] = stack;
		this.onInventoryChanged();
	}
	@Override
	public int getSlots() {
		return this.inventory.length;
	}
	@Override
	public int getSizeInventory() {
		return this.getSlots();
	}
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (amount <= 0) return null;
		ItemStack original = this.getStackInSlot(slot);
		if (original == null || original.stackSize <= 0) return null;
		if (amount >= original.stackSize) {
			this.setStackInSlot(slot, null);
			return original;
		}
		ItemStack copy = original.splitStack(amount);
		this.setStackInSlot(slot, original);
		return copy;
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.setStackInSlot(slot, stack);
	}
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {if (stack == null || stack.stackSize <= 0) return null;
		if (slot >= 9) return stack;
		if (!this.isItemValid(slot, stack)) return stack;
		ItemStack original = this.getStackInSlot(slot);
		if (original == null || original.stackSize <= 0) {
			if (!simulate) this.setStackInSlot(slot, stack.copy());
			return null;
		}
		if (!original.isItemEqual(stack)) return stack;
		int space = Math.min(this.getMaxStackSize(slot), original.getMaxStackSize()) - original.stackSize;
		if (space >= stack.stackSize) {
			if (!simulate) {
				original.stackSize += stack.stackSize;
				this.setStackInSlot(slot, original);
			}
			return null;
		}
		ItemStack copy = stack.copy();
		copy.stackSize -= space;
		if (!simulate) {
			original.stackSize += space;
			this.setStackInSlot(slot, original);
		}
		return copy;
	}
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot != 9) return null;
		if (amount <= 0) return null;
		ItemStack original = this.getStackInSlot(slot);
		if (original == null || original.stackSize <= 0) return null;
		if (amount >= original.stackSize) {
			if (!simulate) {
				this.setStackInSlot(slot, null);
				return original;
			}
			return original.copy();
		}
		if (simulate) {
			ItemStack copy = original.copy();
			return copy.splitStack(amount);
		} else {
			ItemStack copy = original.splitStack(amount);
			this.setStackInSlot(slot, original);
			return copy;
		}
	}
	@Override
	public int getMaxStackSize(int slot) {
		return this.getInventoryStackLimit();
	}
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return slot < 9 && MANAGER.matchesSlot(this.recipe, stack, slot);
	}

	@Override
	public long getEnergyCapacity() {
		return MAX_ENERGY;
	}
	@Override
	public long getRemainingEnergyCapacity() {
		return MAX_ENERGY - this.energy;
	}
	@Override
	public long addEnergy(long amount) {
		this.onInventoryChanged();
		long remain = amount - (MAX_ENERGY - this.energy);
		if (remain <= 0L) {
			this.energy += amount;
			return 0L;
		} else {
			this.energy = MAX_ENERGY;
			return remain;
		}
	}
	@Override
	public int getPriority() {
		return TileEntityProxy.MACHINE_PRIORITY;
	}

	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}
	public String getInvName() {
		return "Manufactory";
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing context) {
		if (capability == CapabilityItemHandler.CAPABILITY) return true;
		return super.hasCapability(capability, context);
	}
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing context) {
		if (capability == CapabilityItemHandler.CAPABILITY) return capability.cast(this);
		return super.getCapability(capability, context);
	}
}
