package dev.siepert.nuclearprogram.world.entity;

import net.minecraft.src.*;

public class EntityHowitzerShell extends Entity {
	public static final double RANGE_MULTIPLIER = 25.0;
	private double deltaX, deltaY, deltaZ;
	public EntityHowitzerShell(World world) {
		super(world);
	}

	public static Vec3D getLookVec(float yaw, float pitch) {
		float var2;
		float var3;
		float var4;
		float var5;
		var2 = MathHelper.cos(-yaw * ((float)Math.PI / 180.0F) - (float)Math.PI);
		var3 = MathHelper.sin(-yaw * ((float)Math.PI / 180.0F) - (float)Math.PI);
		var4 = -MathHelper.cos(-pitch * ((float)Math.PI / 180.0F));
		var5 = MathHelper.sin(-pitch * ((float)Math.PI / 180.0F));
		return Vec3D.createVector(var3 * var4, var5, var2 * var4);
	}

	@Override
	public Vec3D getLookVec() {
		float var2;
		float var3;
		float var4;
		float var5;
		var2 = MathHelper.cos(-this.rotationYaw * ((float)Math.PI / 180.0F) - (float)Math.PI);
		var3 = MathHelper.sin(-this.rotationYaw * ((float)Math.PI / 180.0F) - (float)Math.PI);
		var4 = -MathHelper.cos(-this.rotationPitch * ((float)Math.PI / 180.0F));
		var5 = MathHelper.sin(-this.rotationPitch * ((float)Math.PI / 180.0F));
		return Vec3D.createVector(var3 * var4, var5, var2 * var4);
	}

	@Override
	protected void entityInit() {
	}

	public void calculations() {
		Vec3D vec = this.getLookVec();
		this.deltaX = vec.xCoord * 10.0;
		this.deltaY = vec.yCoord * 10.0;
		this.deltaZ = vec.zCoord * 10.0;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.deltaX = nbt.getDouble("dX");
		this.deltaY = nbt.getDouble("dY");
		this.deltaZ = nbt.getDouble("dZ");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setDouble("dX", this.deltaX);
		nbt.setDouble("dY", this.deltaY);
		nbt.setDouble("dZ", this.deltaZ);
	}

	@Override
	public void onUpdate() {
		this.loadChunks();
		this.moveEntity(this.deltaX, this.deltaY, this.deltaZ);
		if (this.deltaY >= -10) this.deltaY = Math.max(-10, this.deltaY - (9.81 * 0.05) / RANGE_MULTIPLIER);
		if (this.isCollided) {
			this.explode();
		}
		super.onUpdate();
	}

	public void loadChunks() {
		IChunkProvider provider = this.worldObj.getIChunkProvider();
		int chunkX = MathHelper.floor_double(this.posX) >> 4;
		int chunkZ = MathHelper.floor_double(this.posZ) >> 4;
		provider.provideChunk(chunkX, chunkZ);
		int prospectChunkX = MathHelper.floor_double(this.posX + this.motionX) >> 4;
		int prospectChunkZ = MathHelper.floor_double(this.posZ + this.motionZ) >> 4;
		if (prospectChunkX != chunkX || prospectChunkZ != chunkZ) {
			provider.provideChunk(prospectChunkX, prospectChunkZ);
		}
	}
	private void explode() {
		System.out.println("Exploding at X:" + this.posX + ", Y:" + this.posY + ", Z:" + this.posZ);
		this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 10.0F, true);
		this.setEntityDead();
	}

	@Override
	public void setRotation(float var1, float var2) {
		super.setRotation(var1, var2);
	}
}
