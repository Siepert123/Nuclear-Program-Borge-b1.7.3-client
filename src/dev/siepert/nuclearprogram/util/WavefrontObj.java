package dev.siepert.nuclearprogram.util;

import dev.objlib.api.IObjModel;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WavefrontObj {
	private static final boolean EXTENDED_LOGGING = true;

	public final IObjModel model;
	public final List<String> groups;
	public final Map<String, Integer> groupLists;
	public boolean prerendered = false;

	public WavefrontObj(IObjModel model) {
		this.model = model.disableFormatCheck();
		this.groups = new ArrayList<>();
		this.groupLists = new HashMap<>();
	}

	public void callList(String group) {
		if (!this.prerendered) this.rerender();
		Integer list = this.groupLists.get(group);
		if (list == null) return;
		GL11.glCallList(list);
	}
	public void callAllLists() {
		if (!this.prerendered) this.rerender();
		for (Integer list : this.groupLists.values()) {
			if (list != null) GL11.glCallList(list);
		}
	}

	public void rerender() {
		this.groups.clear();
		this.groups.addAll(this.model.collectGroupNames());
		if (!this.groupLists.isEmpty()) {
			for (Integer list : this.groupLists.values()) {
				if (GL11.glIsList(list)) GL11.glDeleteLists(list, 1);
			}
			this.groupLists.clear();
		}
		for (String group : this.groups) {
			Tessellator.instance.setColorOpaque(255, 255, 255);
			int list = GL11.glGenLists(1);
			if (EXTENDED_LOGGING) System.out.println("Rendering " + this.model.getFilename() + ": " + group + " (#" + list + ")");
			GL11.glNewList(list, GL11.GL_COMPILE);
			this.model.renderGroup(group);
			GL11.glEndList();
			this.groupLists.put(group, list);
		}
		this.prerendered = true;
	}
}
