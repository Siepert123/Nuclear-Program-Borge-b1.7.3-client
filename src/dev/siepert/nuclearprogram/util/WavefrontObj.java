package dev.siepert.nuclearprogram.util;

import dev.objlib.api.IObjModel;
import dev.objlib.api.IVBOBufferData;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WavefrontObj {
	public static final boolean USE_VBO = true;
	private static final boolean EXTENDED_LOGGING = true;

	public final IObjModel model;
	public final List<String> groups;
	public final Map<String, Integer> groupLists;
	private final List<IVBOBufferData> vbo = new ArrayList<>();
	public boolean prerendered = false;

	public WavefrontObj(IObjModel model) {
		this.model = model.disableFormatCheck();
		this.groups = new ArrayList<>();
		this.groupLists = new HashMap<>();
	}

	public void callList(String group) {
		if (!this.prerendered) this.rerender();
		if (USE_VBO) {
			for (IVBOBufferData data : this.vbo) {
				if (group.equals(data.name())) {
					this.render(data);
				}
			}
		} else {
			Integer list = this.groupLists.get(group);
			if (list == null) return;
			GL11.glCallList(list);
		}
	}
	public void callAllLists() {
		if (!this.prerendered) this.rerender();
		if (USE_VBO) {
			for (IVBOBufferData data : this.vbo) {
				this.render(data);
			}
		} else {
			for (Integer list : this.groupLists.values()) {
				if (list != null) GL11.glCallList(list);
			}
		}
	}
	private void render(IVBOBufferData data) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, data.vertexID());
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, data.uvID());
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0L);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, data.normalID());
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0L);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, data.vertices());

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public void rerender() {
		this.groups.clear();
		this.groups.addAll(this.model.collectGroupNames());
		if (USE_VBO) {
			if (!this.vbo.isEmpty()) {
				for (IVBOBufferData data : this.vbo) {
					GL15.glDeleteBuffers(data.vertexID());
					GL15.glDeleteBuffers(data.uvID());
					GL15.glDeleteBuffers(data.normalID());
				}
				this.vbo.clear();
			}
			this.vbo.addAll(this.model.compileVBO());
		} else {
			if (!this.groupLists.isEmpty()) {
				for (Integer list : this.groupLists.values()) {
					if (GL11.glIsList(list)) GL11.glDeleteLists(list, 1);
				}
				this.groupLists.clear();
			}
			for (String group : this.groups) {
				Tessellator.instance.setColorOpaque(255, 255, 255);
				int list = GL11.glGenLists(1);
				if (EXTENDED_LOGGING)
					System.out.println("Rendering " + this.model.getFilename() + ": " + group + " (#" + list + ")");
				GL11.glNewList(list, GL11.GL_COMPILE);
				this.model.renderGroup(group);
				GL11.glEndList();
				this.groupLists.put(group, list);
			}
		}
		this.prerendered = true;
	}
}
