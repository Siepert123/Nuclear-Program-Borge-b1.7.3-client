package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.gui.GuiHeatexBoiler;
import dev.siepert.nuclearprogram.pipenet.PipeNet;
import dev.siepert.nuclearprogram.pipenet.node.PNNReceiverTE;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.fluid.Fluid;
import dev.siepert.nuclearprogram.world.te.TileEntityHeatexBoiler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.List;

public class BlockHeatexBoiler extends BlockContainer implements IFluidIdentifiable {
	public Icon blockTextureTop, blockTextureBottom;

	public BlockHeatexBoiler(int blockID, Material material) {
		super(blockID, material);

		BlockFluidPipe.enableConnection(blockID);
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
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		this.blockTextureTop = register.getTexture(this.getSimpleName() + "_top", 16, 16);
		this.blockTextureBottom = register.getTexture(this.getSimpleName() + "_bottom", 16, 16);
	}

	@Override
	public Icon getBlockIconFromSide(int side) {
		if (side == Side.UP) return this.blockTextureTop;
		if (side == Side.DOWN) return this.blockTextureBottom;
		return this.blockTexture;
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
}
