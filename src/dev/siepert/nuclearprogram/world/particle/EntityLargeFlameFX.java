package dev.siepert.nuclearprogram.world.particle;

import net.minecraft.src.EntityFX;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import net.minecraftborge.loader.BorgeMath;
import net.minecraftborge.loader.Icon;

public class EntityLargeFlameFX extends EntityFX {
	private static final float[] colorsStart = {1.0F, 1.0F, 0.0F};
	private static final float[] colorsEnd = {1.0F, 0.0F, 0.0F};

	public EntityLargeFlameFX(World world, double x, double y, double z, double dx, double dy, double dz) {
		super(world, x, y, z, dx, dy, dz);
		this.noClip = true;
		this.particleScale = 2.0F;
		this.particleMaxAge = 20;
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

		float larp = this.particleAge / (float) this.particleMaxAge;
		this.particleRed = BorgeMath.lerp(colorsStart[0], colorsEnd[0], larp);
		this.particleGreen = BorgeMath.lerp(colorsStart[1], colorsEnd[1], larp);
		this.particleBlue = BorgeMath.lerp(colorsStart[2], colorsEnd[2], larp);
	}

	@Override
	public void renderParticle(Tessellator tes, float partialTick, float x, float y, float z, float var6, float var7) {
		int larp = this.particleAge * 8 / this.particleMaxAge;
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
		float brightness = 1.0F;

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
		return 1.0F;
	}
}
