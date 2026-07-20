package dev.siepert.nuclearprogram.world.te;

import net.minecraft.client.Minecraft;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftborge.loader.TrackedSound;

public class TileEntityModulator extends TileEntity {
	public TileEntityModulator() {

	}

	private boolean firstTick = true;
	@Override
	public void updateEntity() {
		if (this.firstTick) {
			this.firstTick = false;
			this.onNeighbourBlockChange();
		}
	}

	protected TrackedSound sound = null;
	public void onNeighbourBlockChange() {
		boolean powered = this.isPowered();
		if (powered && this.sound == null) {
			this.sound = Minecraft.getTheMinecraft().sndManager.playTrackedSound("siren.modulator",
					this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F,
					64.0F, 1.0F, true
			);
		}
		if (!powered && this.sound != null) {
			if (this.sound.isValid()) {
				this.sound.getSystem().stop(this.sound.getSoundID());
				this.sound.invalidate();
			}
			this.sound = null;
		}
	}

	protected boolean isPowered() {
		World world = this.worldObj;
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		return world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockGettingPowered(x, y, z);
	}
}
