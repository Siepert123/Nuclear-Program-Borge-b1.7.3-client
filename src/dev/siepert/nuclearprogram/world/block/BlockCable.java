package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.cablenet.CableNet;
import dev.siepert.nuclearprogram.cablenet.CableNetNode;
import dev.siepert.nuclearprogram.cablenet.node.CNNBasic;
import dev.siepert.nuclearprogram.init.ItemInit;
import dev.siepert.nuclearprogram.util.NPMth;
import dev.siepert.nuclearprogram.util.collect.IntList;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockCable;
import net.minecraft.src.*;
import net.minecraftborge.loader.EnumFacing;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;
import net.minecraftborge.loader.Side;

import java.util.List;

public class BlockCable extends Block implements IOverlayInfo {
	public static final boolean[] canConnectCable = new boolean[blocksList.length];
	public static final int[] canConnectCableMetaMask = new int[blocksList.length];

	public static boolean canConnectCable(int block, int meta) {
		if (canConnectCable[block]) {
			return (canConnectCableMetaMask[block] & (1 << meta)) != 0;
		} else return false;
	}

	public static void enableConnection(int block, int meta) {
		canConnectCable[block] = true;
		canConnectCableMetaMask[block] |= (1 << meta);
	}
	public static void disableConnection(int block, int meta) {
		canConnectCableMetaMask[block] &= ~(1 << meta);
		canConnectCable[block] = canConnectCableMetaMask[block] != 0;
	}

	public static void enableConnection(int block) {
		canConnectCable[block] = true;
		canConnectCableMetaMask[block] = 0xFFFF;
	}

	public Icon blockTextureVertical;
	public Icon blockTextureHorizontal;

	public BlockCable(int blockID) {
		super(blockID, NPMaterials.cable);

		enableConnection(blockID);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float size = 0.1875F;
		this.setBlockBounds(0.5F-size, 0.5F-size, 0.5F-size, 0.5F+size, 0.5F+size, 0.5F+size);
		int ox, oy, oz;
		int count = 0;
		EnumFacing first = null;
		for (EnumFacing side : EnumFacing.VALUES) {
			ox = x + side.getOffsetX();
			oy = y + side.getOffsetY();
			oz = z + side.getOffsetZ();
			if (canConnectCable(world.getBlockId(ox, oy, oz), world.getBlockMetadata(ox, oy, oz))) {
				if (first == null) first = side;
				count++;

				switch (side) {
					case UP:
						this.maxY = 1.0;
						break;
					case DOWN:
						this.minY = 0.0;
						break;
					case NORTH:
						this.minX = 0.0;
						break;
					case EAST:
						this.minZ = 0.0;
						break;
					case SOUTH:
						this.maxX = 1.0;
						break;
					case WEST:
						this.maxZ = 1.0;
						break;
				}
			}
		}

		if (count == 1) {
			EnumFacing side = first.getOpposite();
			switch (side) {
				case UP:
					this.maxY = 1.0;
					break;
				case DOWN:
					this.minY = 0.0;
					break;
				case NORTH:
					this.minX = 0.0;
					break;
				case EAST:
					this.minZ = 0.0;
					break;
				case SOUTH:
					this.maxX = 1.0;
					break;
				case WEST:
					this.maxZ = 1.0;
					break;
			}
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void registerIcons(IconRegister register) {
		super.registerIcons(register);
		this.blockTextureVertical = register.getTexture(this.getSimpleName() + "Vertical", 16, 16);
		this.blockTextureHorizontal = register.getTexture(this.getSimpleName() + "Horizontal", 16, 16);
	}

	public static int pass = 0;
	@Override
	public Icon getBlockIconFromSide(int side) {
		if (pass != 0) return this.blockTexture;
		else {
			if (axis == -1) return this.blockTexture;
			else {
				int beam = Side.getAxis(axis);
				switch (beam) {
					case Side.Y:
						if (side == Side.UP || side == Side.DOWN) return this.blockTexture;
						else return this.blockTextureVertical;
					case Side.X:
						if (side == Side.POS_X || side == Side.NEG_X) return this.blockTexture;
						else return this.blockTextureHorizontal;
					case Side.Z:
						if (side == Side.POS_Z || side == Side.NEG_Z) return this.blockTexture;
						else if (side == Side.UP || side == Side.DOWN) return this.blockTextureVertical;
						else return this.blockTextureHorizontal;
					default: return this.blockTexture;
				}
			}
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		CableNet.setNode(world, x, y, z, new CNNBasic(world).positioned(x, y, z));
	}

	@Override
	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		CableNet.setNode(world, x, y, z, null);
	}

	public static int axis = -1;

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return axis != side;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockCable.RENDER_TYPE;
	}

	@Override
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
		if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == ItemInit.screwdriver.shiftedIndex) {
			long nextID = CableNet.nextNetworkID;
			CableNet.Network net = CableNet.getData(world).getOrCreateNetwork(CableNet.getNode(x, y, z));
			player.addChatMessage("Network of Cable: " + net + (nextID != CableNet.nextNetworkID ? " (newly created)" : "") + " with " + net.nodes.size() + " nodes and " + net.receivers.size() + " receivers");
			if (player.isSneaking()) {
				long amount = 10000L;
				long remainder = net.pushEnergy(amount);
				player.addChatMessage((amount - remainder) + "RF out of " + amount + "RF pushed to " + net.receivers.size() + " receivers");
			}
			return true;
		} else return false;
	}

	@Override
	public void addInformation(World world, int x, int y, int z, List<String> information, IntList colors) {
		CableNetNode node = CableNet.getNode(x, y, z);
		if (node != null) {
			information.add("Network ID: " + node.network);
			colors.add(0x00FFFF);
		} else {
			information.add("(no attached CableNet node)");
			colors.add(NPMth.blink() ? 0xFF0000 : 0xFF8888);
		}
	}
}
