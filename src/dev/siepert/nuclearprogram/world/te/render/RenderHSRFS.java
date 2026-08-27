package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.OBJInit;
import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.world.block.BlockMulti;
import dev.siepert.nuclearprogram.world.te.TileEntityHSRFS;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderHSRFS extends TileEntitySpecialRenderer<TileEntityHSRFS> {
	public static final int[] OPENING_ANIMATION_TICKS = TileEntityHSRFS.OPENING_ANIMATION_TICKS;
	public static final int[] CLOSING_ANIMATION_TICKS = TileEntityHSRFS.CLOSING_ANIMATION_TICKS;
	public static final float ROTATION_MULTIPLIER = TileEntityHSRFS.ROTATION_MULTIPLIER;

	public static final RenderHSRFS INSTANCE = new RenderHSRFS();
	private RenderHSRFS() {}

	@Override
	public String getRenderTexture(TileEntityHSRFS te) {
		return OBJInit.hsrfs_tex;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
		GL11.glRotatef(BlockMulti.getRotation(te.getBlockMetadata()), 0.0F, 1.0F, 0.0F);

		this.renderAt((TileEntityHSRFS) te, partialTick);

		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
	}

	private void renderAt(TileEntityHSRFS te, float pt) {
		OBJInit.hsrfs.callList("Base");
		//RenderHelper.disableStandardItemLighting();
		if (te.open) {
			if (te.animationTicks == 0) {
				GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
				OBJInit.hsrfs.callList("InnerCover");
				OBJInit.hsrfs.callList("OuterCover");
			} else {
				if (te.animRotation > 0) {
					float animRotation = te.animRotation < OPENING_ANIMATION_TICKS[2] ? te.animRotation + pt : te.animRotation;
					float rotation = Easing.IN_EXPO.ease(animRotation / OPENING_ANIMATION_TICKS[2]) * 180.0F;
					GL11.glRotatef(rotation, 1.0F, 0.0F, 0.0F);
					if (te.animRotation < OPENING_ANIMATION_TICKS[2]) {
						GL11.glPushMatrix();
						GL11.glRotatef(te.rotation * ROTATION_MULTIPLIER, 0.0F, 1.0F, 0.0F);
						OBJInit.hsrfs.callList("Hull");
						GL11.glPopMatrix();
					}
					OBJInit.hsrfs.callList("OuterCover");
					GL11.glRotatef(-rotation * 2, 1.0F, 0.0F, 0.0F);
					OBJInit.hsrfs.callList("InnerCover");
				} else {
					float animAcceleration = te.animAcceleration < OPENING_ANIMATION_TICKS[0] ? te.animAcceleration + pt : te.animAcceleration;
					float rotation = Easing.OUT_QUART.ease(animAcceleration / OPENING_ANIMATION_TICKS[0]) * 360.0F + te.rotation * ROTATION_MULTIPLIER;
					GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
					OBJInit.hsrfs.callList("Hull");
				}
			}
		} else {
			if (te.animationTicks == 0) {
				GL11.glRotatef((te.rotation + pt) * TileEntityHSRFS.ROTATION_MULTIPLIER, 0.0F, 1.0F, 0.0F);
				OBJInit.hsrfs.callList("Hull");
			} else {
				if (te.animAcceleration > 0) {
					float animAcceleration = te.animAcceleration < CLOSING_ANIMATION_TICKS[2] ? te.animAcceleration + pt : te.animAcceleration;
					float extra = te.animAcceleration < CLOSING_ANIMATION_TICKS[2] ? te.rotation : te.rotation + pt;
					float rotation = Easing.IN_QUART.ease(animAcceleration / CLOSING_ANIMATION_TICKS[2]) * 360.0F + extra * ROTATION_MULTIPLIER;
					GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
					OBJInit.hsrfs.callList("Hull");
				} else if (te.animRotation > 0) {
					float animRotation = te.animRotation < CLOSING_ANIMATION_TICKS[1] ? te.animRotation + pt : te.animRotation;
					float rotation = 180.0F - Easing.IN_EXPO.ease(animRotation / CLOSING_ANIMATION_TICKS[1]) * 180.0F;
					GL11.glRotatef(rotation, 1.0F, 0.0F, 0.0F);

					GL11.glPushMatrix();
					GL11.glRotatef(te.rotation * ROTATION_MULTIPLIER, 0.0F, 1.0F, 0.0F);
					OBJInit.hsrfs.callList("Hull");
					GL11.glPopMatrix();

					OBJInit.hsrfs.callList("OuterCover");
					GL11.glRotatef(-rotation * 2, 1.0F, 0.0F, 0.0F);
					OBJInit.hsrfs.callList("InnerCover");
				} else {
					GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
					OBJInit.hsrfs.callList("InnerCover");
					OBJInit.hsrfs.callList("OuterCover");
				}
			}
		}
		//RenderHelper.enableStandardItemLighting();
	}
}
