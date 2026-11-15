package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.world.te.TileEntityBellows;
import net.minecraft.src.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderBellows extends RenderMachineBase<TileEntityBellows> {
	public static final RenderBellows INSTANCE = new RenderBellows();
	private RenderBellows() {
		super(TileEntityBellows.class);
	}

	@Override
	public String getRenderTexture(TileEntityBellows te) {
		return OBJInit.bellows_tex;
	}

	@Override
	protected void renderMachine(TileEntityBellows te, double x, double y, double z, float partialTick) {
		if (te.cooldown == 0) {
			OBJInit.bellows.callAllLists();
		} else {
			OBJInit.bellows.callList("Base");
			float animation = te.cooldown - partialTick;
			float scale = 1.0F-MathHelper.sin(animation * 0.05F * (float) Math.PI);
			GL11.glTranslatef(0.0F, 0.25F, 0.0F);
			GL11.glScalef(1.0F, scale*0.75F + 0.25F, 1.0F);
			GL11.glTranslatef(0.0F, -0.25F, 0.0F);
			OBJInit.bellows.callList("Sack");
			OBJInit.bellows.callList("Top");
		}
	}
}
