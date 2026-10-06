package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityManufactory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityItem;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderManager;
import net.minecraft.src.World;
import net.minecraftborge.loader.BorgeMath;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderManufactory extends RenderMachineBase<TileEntityManufactory> {
	private final RenderItem itemRenderer = new RenderItem();
	private final EntityItem itemEntity = new EntityItem(null);
	private final Random rnd = new Random();

	public RenderManufactory() {
		super(TileEntityManufactory.class);
		this.itemRenderer.setRenderManager(RenderManager.instance);
	}

	@Override
	public String getRenderTexture(TileEntityManufactory te) {
		return OBJInit.dynamic_tex;
	}

	@Override
	public void setWorld(World world) {
		this.itemEntity.setWorld(world);
	}

	@Override
	protected void renderMachine(TileEntityManufactory te, double x, double y, double z, float partialTick) {
		this.bindTextureByName(OBJInit.manufactory_tex);

		GL11.glPushMatrix();
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);
		OBJInit.manufactory.callList("FlatShading");
		GL11.glShadeModel(GL11.GL_SMOOTH);
		OBJInit.manufactory.callList("SmoothShading");
		OBJInit.manufactory.callList("Press");

		this.rnd.setSeed(te.hashCode() + (te.recipe != null ? te.recipe.hashCode() : 2137L));
		float rot = BorgeMath.clampedLerp(te.animationOld, te.animation, partialTick) * (this.rnd.nextFloat() * 9.0F + 1.0F) * (this.rnd.nextBoolean() ? -1 : 1);
		GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
		OBJInit.manufactory.callList("Cogwheel1");
		GL11.glRotatef(-2*rot, 0.0F, 1.0F, 0.0F);
		OBJInit.manufactory.callList("Cogwheel2");

		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();

		if (te.recipe != null && te.recipe.icon != null) {
			this.itemEntity.item = te.recipe.icon;
			this.itemEntity.age = Minecraft.getTicksRan();
			this.itemEntity.setPositionAndRotation(te.xCoord + 0.5, te.yCoord + 1.5, te.zCoord + 0.5, 0.0F, 0.0F);
			this.itemRenderer.doRenderItem(this.itemEntity, 0.0, 1.5, 0.0, 0.0F, partialTick);
		}
	}
}
