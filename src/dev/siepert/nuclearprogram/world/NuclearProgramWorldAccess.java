package dev.siepert.nuclearprogram.world;

import dev.siepert.nuclearprogram.weapon.BackendExplosionHandler;
import dev.siepert.nuclearprogram.world.entity.EntityHowitzerShell;
import dev.siepert.nuclearprogram.world.particle.EntityGasFX;
import dev.siepert.nuclearprogram.world.te.TileEntityHatch;
import dev.siepert.nuclearprogram.world.te.TileEntitySealedDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.util.Random;

public class NuclearProgramWorldAccess implements IWorldAccess {
	public static final NuclearProgramWorldAccess INSTANCE = new NuclearProgramWorldAccess();

	private final Minecraft mc = Minecraft.getTheMinecraft();
	private final Random rnd = new Random();
	private World worldObj;

	public void setWorld(World world) {
		if (this.worldObj != null) this.worldObj.removeWorldAccess(this);
		if (world != null) world.addWorldAccess(this);
		this.worldObj = world;
		this.rnd.setSeed(world != null ? world.hashCode() : 0L);
	}

	@Override
	public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {

	}

	@Override
	public void markBlockRangeNeedsUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {

	}

	@Override
	public void playSound(String name, double x, double y, double z, float volume, float pitch) {

	}

	@Override
	public void spawnParticle(String name, double x, double y, double z, double dx, double dy, double dz) {
		if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
			double px = this.mc.renderViewEntity.posX - x;
			double py = this.mc.renderViewEntity.posY - y;
			double pz = this.mc.renderViewEntity.posZ - z;
			EntityGasFX gasFX;
			switch (name) {
				case "nuclear_program/gas":
					this.mc.effectRenderer.addEffect(new EntityGasFX(this.worldObj, x, y, z, (float) dx, (float) dy, (float) dz));
					break;
				case "nuclear_program/pollution":
					float f0 = this.rnd.nextFloat() * 0.1F;
					gasFX = new EntityGasFX(this.worldObj, x, y, z, 0.15F + f0, 0.15F + f0, 0.15F + f0);
					if (dx > 0.0) gasFX.scalar = (float) dx;
					this.mc.effectRenderer.addEffect(gasFX);
					break;
				case "nuclear_program/steam":
					float f1 = this.rnd.nextFloat() * 0.05F + 0.95F;
					gasFX = new EntityGasFX(this.worldObj, x, y, z, f1, f1, 1.0F);
					if (dx > 0.0) gasFX.scalar = (float) dx;
					this.mc.effectRenderer.addEffect(gasFX);
					break;
			}
		}
	}

	@Override
	public void obtainEntitySkin(Entity entity) {
		if (entity.getClass() == EntityHowitzerShell.class) {
			System.out.println("Howitzer shell loaded!");
		}
	}

	@Override
	public void releaseEntitySkin(Entity entity) {
		if (entity.getClass() == EntityHowitzerShell.class) {
			System.out.println("Howitzer shell unloaded!");
		}
	}

	@Override
	public void updateAllRenderers() {

	}

	@Override
	public void playRecord(String name, int x, int y, int z) {

	}

	@Override
	public void doNothingWithTileEntity(int x, int y, int z, TileEntity te) {

	}

	@Override
	public void playEvent(EntityPlayer player, int type, int x, int y, int z, int data) {
		if (type == 2137) {
			if (data == 0) {
				player = Minecraft.getTheMinecraft().thePlayer;
				double distance = player != null ? player.getDistance(x, y, z) : 0.0;
				String sound = distance > 128.0 ? "weapon.howitzerDistant" : "weapon.howitzer";
				float pitch;
				if (distance > 600.0) {
					double delta = ((distance - 600) / 1000.0);
					pitch = 1.0F - (float) (delta * delta);
				} else pitch = 1.0F;
				this.worldObj.playSoundEffect(x, y, z, sound, 100.0F, pitch);
			}
			if (data == 1) {
				this.worldObj.playSoundEffect(x + 0.5, y + 1.0, z + 0.5, "block.yanoDoor",
						1.0F, 0.9F + this.worldObj.rand.nextFloat() * 0.2F);
			}
			if (data == 2) {
				this.worldObj.playSoundEffect(x + 0.5, y, z + 0.5, "block.hatchOpen", 1.0F, 1.0F);
				if (this.worldObj.multiplayerWorld) {
					TileEntityHatch te = (TileEntityHatch) this.worldObj.getBlockTileEntity(x, y, z);
					if (te != null) te.setOpen(true);
				}
			}
			if (data == 3) {
				this.worldObj.playSoundEffect(x + 0.5, y, z + 0.5, "block.hatchClose", 1.0F, 1.0F);
				if (this.worldObj.multiplayerWorld) {
					TileEntityHatch te = (TileEntityHatch) this.worldObj.getBlockTileEntity(x, y, z);
					if (te != null) te.setOpen(false);
				}
			}
			if (data == 4) {
				this.worldObj.playSoundEffect(x + 0.5, y + 1.0, z + 0.5, "block.sealedDoorOpen", 1.0F, 1.0F);
				if (this.worldObj.multiplayerWorld) {
					TileEntitySealedDoor te = (TileEntitySealedDoor) this.worldObj.getBlockTileEntity(x, y, z);
					if (te != null) te.setOpen(true);
				}
			}
			if (data == 5) {
				this.worldObj.playSoundEffect(x + 0.5, y + 1.0, z + 0.5, "block.sealedDoorClose", 1.0F, 1.0F);
				if (this.worldObj.multiplayerWorld) {
					TileEntitySealedDoor te = (TileEntitySealedDoor) this.worldObj.getBlockTileEntity(x, y, z);
					if (te != null) te.setOpen(false);
				}
			}
			if (data == 6) {
				this.worldObj.playSoundEffect(x, y, z, "weapon.explosionNuclear", 128.0F, 1.0F);
				BackendExplosionHandler.shockwaveTicks = 20 * 8;
			}
			if (data == 7) {
				this.worldObj.playSoundEffect(x, y, z, "weapon.explosionLarge", 32.0F, 1.0F);
			}
		}
	}
}
