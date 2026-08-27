package dev.siepert.nuclearprogram.util.math;

public class MouseArea {
	public final int x, y, w, h;

	public MouseArea(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public boolean isInArea(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseY >= this.y && mouseX < this.x+this.w && mouseY < this.y+this.h;
	}
}
