package dev.siepert.nuclearprogram.gui;

import dev.siepert.nuclearprogram.world.te.TileEntityFurnaceBuilder;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraftborge.loader.ByteArrayReader;
import net.minecraftborge.loader.IModGUIFactory;

public enum NuclearProgramRemoteGUI implements IModGUIFactory {
	INSTANCE;

	private final ByteArrayReader reader = new ByteArrayReader();

	@Override
	public GuiContainer createGUI(int id, EntityPlayer player, byte[] extraData) {
		this.reader.setData(extraData);
		switch (id) {
			case 0:
				return new GuiWorkbench(player.inventory, player.worldObj, this.reader.readInt(), this.reader.readUnsignedByte(), this.reader.readInt());
			case 1:
				return new GuiFurnaceBuilder(player.inventory, new TileEntityFurnaceBuilder());
			default:
				return null;
		}
	}
}
