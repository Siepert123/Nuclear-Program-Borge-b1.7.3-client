package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.gui.GuiHeatexBoiler;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNReceiverTE;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityHeatexBoiler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.util.List;

public class BlockHeatexBoiler extends BlockContainer implements IFluidIdentifiable, IOverlayInfo {
	public BlockHeatexBoiler(int blockID, Material material) {
		super(blockID, material);
	}

	@Override
	protected TileEntity getBlockEntity(int meta) {
		return new TileEntityHeatexBoiler();
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		PipeNet.setNode(world, x, y, z, new PNNReceiverTE(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		PipeNet.setNode(world, x, y, z, null);
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking() && player.getCurrentEquippedItem() != null) return false;
		if (!world.multiplayerWorld) {
			TileEntityHeatexBoiler te = (TileEntityHeatexBoiler) world.getBlockTileEntity(x, y, z);
			Minecraft.getTheMinecraft().displayGuiScreen(new GuiHeatexBoiler(player.inventory, te));
		}
		return true;
	}

	@Override
	public void setFluidID(World world, int x, int y, int z, int fluidID) {
		if (Fluid.traitCoolable[fluidID] != null) {
			TileEntityHeatexBoiler te = (TileEntityHeatexBoiler) world.getBlockTileEntity(x, y, z);
			te.fluidType = fluidID;
			te.fluidTypeOut = Fluid.traitCoolable[fluidID].coolsTo;
			te.onInventoryChanged();
		}
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		TileEntityHeatexBoiler te = (TileEntityHeatexBoiler) world.getBlockTileEntity(x, y, z);
		information.add("-> " + Fluid.getLocalizedName(Fluid.fluidsList[te.fluidType]) + " " + te.tankCoolantIn + "mB");
		colors.add(0xFFFFFF);
		information.add("<- " + Fluid.getLocalizedName(Fluid.fluidsList[te.fluidTypeOut]) + " " + te.tankCoolantOut + "mB");
		colors.add(0xFFFFFF);
		information.add("-> Water " + te.tankWater + "mB");
		colors.add(0xFFFFFF);
		information.add("<- Steam " + te.tankSteam + "mB");
		colors.add(0xFFFFFF);
	}
}
