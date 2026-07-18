package dev.siepert.nuclearprogram.world.block;

import dev.siepert.nuclearprogram.weapon.NukeTypes;
import dev.siepert.nuclearprogram.world.entity.EntityExplosionHelper;
import dev.siepert.nuclearprogram.world.te.TileEntityNuke;
import net.minecraft.src.World;

public class BlockNuclearCharge extends BlockNuke {
	public BlockNuclearCharge(int blockID) {
		super(blockID);
		isBlockContainerMetaMask[blockID] = 0;
	}

	@Override
	protected TileEntityNuke getBlockEntity(int meta) {
		return null;
	}

	@Override
	public Callback detonate(World world, int x, int y, int z) {
		if (!world.multiplayerWorld) {
			world.setBlockWithNotify(x, y, z, 0);
			world.entityJoinedWorld(new EntityExplosionHelper(world, x+0.5, y+0.5, z+0.5, NukeTypes.CHARGE));
		}
		return Callback.SUCCESS;
	}
}
