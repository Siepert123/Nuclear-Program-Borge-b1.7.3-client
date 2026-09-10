package dev.siepert.nuclearprogram.world.particle;

import dev.siepert.nuclearprogram.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityFX;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import net.minecraftborge.loader.BorgeMath;
import net.minecraftborge.loader.Icon;
import org.lwjgl.opengl.GL11;

public class EntityGasFX extends EntityFX {
	public static final boolean RENDER_FAST = true;

	public static final Minecraft mc = Minecraft.getTheMinecraft();
	public static Icon texture = null;

	public float scalar = 1.0F;
	public float particleAlpha = 1.0F;
	public EntityGasFX(World world, double x, double y, double z, float r, float g, float b, float scalar) {
		this(world, x, y, z, r, g, b);
		this.scalar = scalar;
	}
	public EntityGasFX(World world, double x, double y, double z, float r, float g, float b) {
		super(world, x, y, z, 0, 0, 0);
		texture = ParticleTextures.generic[7];
		this.setMaxAge(195 + world.rand.nextInt(10));
		this.particleRed = r;
		this.particleGreen = g;
		this.particleBlue = b;
		this.particleGravity = 0.0F;
		this.noClip = true;
	}
	public void setMaxAge(int age) {
		this.particleMaxAge = age;
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}
		float larp = this.particleAge / (float) this.particleMaxAge;
		this.particleAlpha = larp < 0.5F ? 1.0F : 1.0F - ((larp - 0.5F) * 2.0F);
		if ((this.particleAge & 3) == 0) {
			this.motionX = (this.rand.nextGaussian() * 0.05F + 0.05F + (1.0F - this.particleAlpha) * 0.1F) * this.scalar * this.particleAlpha;
			this.motionY = (0.25F * this.scalar) * this.particleAlpha;
			this.motionZ = (this.rand.nextGaussian() * 0.05F + 0.05F + (1.0F - this.particleAlpha) * 0.1F) * this.scalar * this.particleAlpha;
		}
		this.particleScale = BorgeMath.lerp(this.scalar, this.scalar * 20.0F, Easing.IN_QUAD.ease(larp));
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

	@Override
	public void renderParticle(Tessellator tes, float partialTick, float x, float y, float z, float var6, float var7) {
		if (RENDER_FAST) {
			this.renderFast(tes, partialTick, x, y, z, var6, var7);
		} else {
			this.renderFancy(tes, partialTick, x, y, z, var6, var7);
		}
	}

	private void renderFast(Tessellator tes, float partialTick, float x, float y, float z, float var6, float var7) {
		if (texture == null) return;
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
	private void renderFancy(Tessellator tes, float partialTick, float x, float y, float z, float var6, float var7) {
		if (texture == null) return;
		double minU = texture.getU(0.0);
		double maxU = texture.getU(1.0);
		double minV = texture.getV(0.0);
		double maxV = texture.getV(1.0);
		float scale = this.particleScale * 0.5F;
		float px = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTick - interpPosX);
		float py = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTick - interpPosY);
		float pz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTick - interpPosZ);
		float brightness = this.getEntityBrightness(partialTick);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tes.startDrawingQuads();
		tes.setColorRGBA_F(this.particleRed * brightness, this.particleGreen * brightness, this.particleBlue * brightness, 0.2F);
		tes.addVertexWithUV(px - x * scale - var6 * scale, py - y * scale, pz - z * scale - var7 * scale, maxU, minV);
		tes.addVertexWithUV(px - x * scale + var6 * scale, py + y * scale, pz - z * scale + var7 * scale, maxU, maxV);
		tes.addVertexWithUV(px + x * scale + var6 * scale, py + y * scale, pz + z * scale + var7 * scale, minU, maxV);
		tes.addVertexWithUV(px + x * scale - var6 * scale, py - y * scale, pz + z * scale - var7 * scale, minU, minV);
		tes.draw();
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public int getFXLayer() {
		return RENDER_FAST ? 2 : 3;
	}

	@Override
	public float getEntityBrightness(float var1) {
		if (this.posY < 0.0) return this.entityBrightness;
		if (this.posY > 127.0) return this.worldObj.getLightBrightness((int)this.posX, (int)this.posY, (int)this.posZ);
		return super.getEntityBrightness(var1);
	}
}
