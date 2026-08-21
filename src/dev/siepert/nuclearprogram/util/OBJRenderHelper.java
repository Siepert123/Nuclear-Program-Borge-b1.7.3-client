package dev.siepert.nuclearprogram.util;

import net.minecraft.src.GLAllocation;
import net.minecraft.src.Vec3D;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class OBJRenderHelper {
	private static final FloatBuffer buf = GLAllocation.createDirectFloatBuffer(16);

	public static void enableMachineLight() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		float ambient = 0.4F;
		float diffuse = 0.6F;
		float specular = 0.0F;
		Vec3D vec = Vec3D.createVector(3, 10, -7);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, storeVec4(vec.xCoord, vec.yCoord, vec.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, storeVec4(diffuse, diffuse, diffuse, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, storeVec4(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, storeVec4(specular, specular, specular, 1.0F));
		vec = Vec3D.createVector(-3, 10, 7);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, storeVec4(vec.xCoord, vec.yCoord, vec.zCoord, 0.0D));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, storeVec4(diffuse, diffuse, diffuse, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, storeVec4(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, storeVec4(specular, specular, specular, 1.0F));

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, storeVec4(ambient, ambient, ambient, 1.0F));
	}

	private static FloatBuffer storeVec4(double x, double y, double z, double w) {
		return storeVec4((float) x, (float) y, (float) z, (float) w);
	}
	private static FloatBuffer storeVec4(float x, float y, float z, float w) {
		buf.clear();
		buf.put(x).put(y).put(z).put(w);
		buf.flip();
		return buf;
	}
}
