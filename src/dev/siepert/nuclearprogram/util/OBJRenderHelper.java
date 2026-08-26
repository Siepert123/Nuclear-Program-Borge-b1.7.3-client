package dev.siepert.nuclearprogram.util;

import net.minecraft.src.GLAllocation;
import net.minecraft.src.Vec3D;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

@Deprecated
public class OBJRenderHelper {
	@Deprecated
	private static final FloatBuffer buf = GLAllocation.createDirectFloatBuffer(16);
	@Deprecated
	private static final Vec3D lightSource0;
	@Deprecated
	private static final Vec3D lightSource1;

	@Deprecated
	public static void enableMachineLight() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);

		@Deprecated
		float ambient = 0.4F;
		@Deprecated
		float diffuse = 0.6F;
		@Deprecated
		float specular = 0.0F;

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, storeVec4(lightSource0.xCoord, lightSource0.yCoord, lightSource0.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, storeVec4(diffuse, diffuse, diffuse, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, storeVec4(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, storeVec4(specular, specular, specular, 1.0F));

		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, storeVec4(lightSource1.xCoord, lightSource1.yCoord, lightSource1.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, storeVec4(diffuse, diffuse, diffuse, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, storeVec4(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, storeVec4(specular, specular, specular, 1.0F));

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, storeVec4(ambient, ambient, ambient, 1.0F));
	}
	@Deprecated
	public static void disableMachineLight() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		GL11.glDisable(GL11.GL_LIGHT1);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
	}

	@Deprecated
	private static FloatBuffer storeVec4(@Deprecated double x, @Deprecated double y, @Deprecated double z, @Deprecated double w) {
		return storeVec4((float) x, (float) y, (float) z, (float) w);
	}
	@Deprecated
	private static FloatBuffer storeVec4(@Deprecated float x, @Deprecated float y, @Deprecated float z, @Deprecated float w) {
		buf.clear();
		buf.put(x).put(y).put(z).put(w);
		buf.flip();
		return buf;
	}

	static {
		@Deprecated
		Vec3D ls0 = Vec3D.createVector(0.2, 1.0, -0.7).normalize();
		lightSource0 = Vec3D.createVectorHelper(ls0.xCoord, ls0.yCoord, ls0.zCoord);
		@Deprecated
		Vec3D ls1 = Vec3D.createVector(-0.2, 1.0, 0.7).normalize();
		lightSource1 = Vec3D.createVectorHelper(ls1.xCoord, ls1.yCoord, ls1.zCoord);
	}
}
