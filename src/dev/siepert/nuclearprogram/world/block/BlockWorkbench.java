package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.gui.GuiWorkbench;
import dev.siepert.nuclearprogram.init.BlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

public class BlockWorkbench extends Block {
	public static final String[] VARIANTS = {
			"Iron",
			"Steel",
	};

	public static final int IRON = 0;
	public static final int STEEL = 1;

	public final Icon[] blockTexturesFront = new Icon[VARIANTS.length];
	public final Icon[] blockTexturesSide = new Icon[VARIANTS.length];
	public final Icon[] blockTexturesTop = new Icon[VARIANTS.length];

	public BlockWorkbench(int blockID) {
		super(blockID, Material.wood);
		this.setStepSound(soundWoodFootstep);
		this.setHardness(3.5F);
		this.setResistance(10.0F);
		this.setHarvestLevel("axe", -1);
	}

	@Override
	public void registerIcons(IconRegister register) {
		this.blockTexture = Block.planks.blockTexture;

		for (int i = 0; i < VARIANTS.length; i++) {
			this.blockTexturesFront[i] = register.getTexture(this.getSimpleName() + VARIANTS[i] + "_front", 16, 16);
			this.blockTexturesSide[i] = register.getTexture(this.getSimpleName() + VARIANTS[i] + "_side", 16, 16);
		}

		this.blockTexturesTop[IRON] = Block.blockIron.blockTexture;
		this.blockTexturesTop[STEEL] = BlockInit.blockMetal.getBlockIconFromSideAndMetadata(2, BlockMetal.STEEL);
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(int side, int meta) {
		if (side == Side.DOWN) return this.blockTexture;
		if (side == Side.UP) return this.blockTexturesTop[meta];
		if ((side & 1) == 0) return this.blockTexturesFront[meta];
		else return this.blockTexturesSide[meta];
	}

	@Override
	protected int damageDropped(int meta) {
		return meta;
	}

	@Override
	public boolean isToolValid(int meta, String tool) {
		return "pickaxe".equals(tool) || "axe".equals(tool);
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.isSneaking()) return false;
		if (!world.multiplayerWorld && player instanceof EntityPlayerSP) {
			Minecraft.getTheMinecraft().displayGuiScreen(new GuiWorkbench(player.inventory, world, x, y, z));
		}
		return true;
	}
}
