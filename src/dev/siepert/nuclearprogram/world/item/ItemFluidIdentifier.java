package dev.siepert.nuclearprogram.world.item;

import dev.siepert.nuclearprogram.gui.GuiFluidIdentifier;
import dev.siepert.nuclearprogram.world.block.IFluidIdentifiable;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Collection;
import java.util.List;

public class ItemFluidIdentifier extends Item {
	public Icon itemTextureOverlay;

	public ItemFluidIdentifier(int itemID) {
		super(itemID);

		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Collection<ItemStack> items) {
		for (int i = 0; i < Fluid.ID_SIZE; i++) {
			if (Fluid.hasFluidIdentifier[i]) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.itemTexture = register.getTexture(this.getSimpleName(), 16, 16);
		this.itemTextureOverlay = register.getTexture(this.getSimpleName() + "_overlay", 16, 16);
	}

	@Override
	public void getTooltip(ItemStack stack, List<String> tooltip, boolean isAdvanced) {
		tooltip.add("Fluid: " + StringTranslate.getInstance().translateNamedKey(Fluid.getUnlocalizedName(Fluid.fluidsList[stack.getItemDamage()])));
	}

	@Override
	public Icon getTextureForPass(ItemStack stack, int pass) {
		return pass != 0 ? this.itemTextureOverlay : this.itemTexture;
	}

	@Override
	public int getColorForPass(ItemStack stack, int pass) {
		if (pass == 1) {
			return Fluid.colorLookup[stack.getItemDamage()] | 0xFF000000;
		}
		return 0xFFFFFFFF;
	}

	@Override
	public int getRenderPasses(ItemStack stack) {
		return 2;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block instanceof IFluidIdentifiable) {
			((IFluidIdentifiable)block).setFluidID(world, x, y, z, stack.getItemDamage());
			return true;
		} else return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ItemStack ret = stack.copy();
		Minecraft.getTheMinecraft().displayGuiScreen(new GuiFluidIdentifier(ret, player));
		return ret;
	}
}
