package dev.siepert.nuclearprogram.world.te;

import dev.siepert.nuclearprogram.recipe.BuilderFurnaceRecipes;
import dev.siepert.nuclearprogram.world.block.BlockFurnaceBuilder;
import net.minecraft.src.*;
import net.minecraftborge.loader.IFurnace;
import net.minecraftborge.loader.ITickingTile;

import java.util.Arrays;

public class TileEntityFurnaceBuilder extends TileEntity implements IInventory, IFurnace, ITickingTile {
	private final ItemStack[] inventory = new ItemStack[4];
	public int furnaceBurnTime = 0;
	public int currentItemBurnTime = 0;
	public int furnaceCookTime = 0;
	public int maxRecipeTime = 0;

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
		if (stack != null && stack.stackSize > Math.min(this.getInventoryStackLimit(), stack.getMaxStackSize())) {
			stack.stackSize = Math.min(this.getInventoryStackLimit(), stack.getMaxStackSize());
		}
	}

	@Override
	public String getInvName() {
		return "Builder's Furnace";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
	}

	public int getMaxRecipeTime() {
		return this.lastRecipe != null ? this.lastRecipe.recipeTime : this.maxRecipeTime;
	}

	public int getCookProgressScaled(int width) {
		int max = this.getMaxRecipeTime();
		if (max == 0) return 0;
		return this.furnaceCookTime * width / max;
	}
	public int getBurnTimeRemainingScaled(int height) {
		if (this.currentItemBurnTime == 0) return 0;
		return this.furnaceBurnTime * height / this.currentItemBurnTime;
	}
	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	private BuilderFurnaceRecipes.Recipe lastRecipe = null;

	@Override
	public void updateEntity() {
		boolean wasBurning = this.isBurning();
		boolean update = false;
		if (this.furnaceBurnTime > 0) {
			this.furnaceBurnTime--;
		}

		if (!this.worldObj.multiplayerWorld) {
			if (this.furnaceBurnTime == 0 && this.canSmelt()) {
				this.currentItemBurnTime = this.furnaceBurnTime = this.getBurnTime(this.inventory[1]);
				if (this.furnaceBurnTime > 0) {
					update = true;
					pleb:
					if (this.inventory[1] != null) {
						if (this.inventory[1].getItem().hasContainerItem()) {
							this.inventory[1] = this.inventory[1].createCraftingRemainder();
							break pleb;
						}

						this.inventory[1].stackSize--;
						if (this.inventory[1].stackSize <= 0) {
							this.inventory[1] = null;
						}
					}
				}
			}

			if (this.isBurning() && this.canSmelt()) {
				this.furnaceCookTime++;
				if (this.furnaceCookTime >= this.lastRecipe.recipeTime) {
					this.furnaceCookTime = 0;
					this.smeltItem();
				}
				update = true;
			} else {
				this.furnaceCookTime = 0;
			}

			if (wasBurning != this.isBurning()) {
				update = true;
				BlockFurnaceBuilder.updateFurnaceBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.isBurning());
			}
		}

		if (update) {
			this.onInventoryChanged();
		}
	}

	private boolean canSmelt() {
		if (this.inventory[0] == null) {
			this.lastRecipe = null;
			return false;
		} else {
			if (this.lastRecipe != null) {
				int pack = BuilderFurnaceRecipes.pack(this.inventory[0]);
				if (pack == this.lastRecipe.recipeID) return true;
				this.lastRecipe = null;
			}

			BuilderFurnaceRecipes.Result result = BuilderFurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);
			if (result == null) return false;
			if (this.inventory[2] != null) {
				if (!this.inventory[2].isItemEqual(result.item)) return false;
				if (this.inventory[2].stackSize + result.item.stackSize > this.getInventoryStackLimit()) return false;
				if (this.inventory[2].stackSize + result.item.stackSize > this.inventory[2].getMaxStackSize()) return false;
			}
			this.lastRecipe = new BuilderFurnaceRecipes.Recipe(
					BuilderFurnaceRecipes.pack(this.inventory[0]),
					result.recipeTime
			);
			return true;
		}
	}

	public void smeltItem() {
		if (this.canSmelt()) {
			this.lastRecipe = null;
			ItemStack result = BuilderFurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]).item;
			if (result == null) {
				System.out.println("Null smelting result?");
				return;
			}
			if (this.inventory[2] == null) {
				this.inventory[2] = result.copy();
			} else if (this.inventory[2].itemID == result.itemID) {
				this.inventory[2].stackSize += result.stackSize;
			}

			this.inventory[0].stackSize--;
			if (this.inventory[0].stackSize <= 0) {
				this.inventory[0] = null;
			}
		}
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

		this.furnaceBurnTime = nbt.getShort("burnTime");
		this.currentItemBurnTime = nbt.getShort("burnTimeMax");
		this.furnaceCookTime = nbt.getShort("cookTime");
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

		if (this.furnaceBurnTime > 0) {
			nbt.setShort("burnTime", (short) this.furnaceBurnTime);
			nbt.setShort("burnTimeMax", (short) this.currentItemBurnTime);
		}
		nbt.setShort("cookTime", (short) this.furnaceCookTime);
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
