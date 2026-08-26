package dev.siepert.nuclearprogram.world.particle;

import dev.siepert.nuclearprogram.util.Easing;
import net.minecraft.src.EntityFX;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import net.minecraftborge.loader.Icon;

public class EntityDrainageFX extends EntityFX {
	public EntityDrainageFX(World world, double x, double y, double z, double dx, double dy, double dz) {
		super(world, x, y, z, dx, dy, dz);
		this.noClip = true;
		this.particleScale = 1.0F;
		this.particleMaxAge = 20 + world.rand.nextInt(10);

		this.particleRed = (float) dx;
		this.particleGreen = (float) dy;
		this.particleBlue = (float) dz;

		this.motionX = 0.0;
		this.motionY = -(9.81 / 20.0);
		this.motionZ = 0.0;
	}

	private boolean glowing = false;
	public EntityDrainageFX setGlowing(boolean glowing) {
		this.glowing = glowing;
		return this;
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

	@Override
	public void renderParticle(Tessellator tes, float partialTick, float x, float y, float z, float var6, float var7) {
		int larp = MathHelper.floor_float(Easing.IN_QUAD.ease(this.particleAge / (float)this.particleMaxAge) * 8);
		if (larp == 8) return;
		Icon texture = ParticleTextures.generic[7-larp];

		double minU = texture.getU(0.0);
		double maxU = texture.getU(1.0);
		double minV = texture.getV(0.0);
		double maxV = texture.getV(1.0);
		float scale = this.particleScale * 0.5F;
		float px = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTick - interpPosX);
		float py = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTick - interpPosY);
		float pz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTick - interpPosZ);
		float brightness = this.getEntityBrightness(partialTick);

		tes.setColorOpaque_F(this.particleRed * brightness, this.particleGreen * brightness, this.particleBlue * brightness);
		tes.addVertexWithUV(px - x * scale - var6 * scale, py - y * scale, pz - z * scale - var7 * scale, maxU, minV);
		tes.addVertexWithUV(px - x * scale + var6 * scale, py + y * scale, pz - z * scale + var7 * scale, maxU, maxV);
		tes.addVertexWithUV(px + x * scale + var6 * scale, py + y * scale, pz + z * scale + var7 * scale, minU, maxV);
		tes.addVertexWithUV(px + x * scale - var6 * scale, py - y * scale, pz + z * scale - var7 * scale, minU, minV);
	}

	@Override
	public int getFXLayer() {
		return 2;
	}

	@Override
	public float getEntityBrightness(float var1) {
		return this.glowing ? 1.0F : super.getEntityBrightness(var1);
	}
}
