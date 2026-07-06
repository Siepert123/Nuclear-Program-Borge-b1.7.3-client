package dev.siepert.nuclearprogram.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import net.minecraftborge.MinecraftBorge;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadingScreenRendererHints extends LoadingScreenRenderer {
	private final List<String> hints = new ArrayList<>();

	private String phaseText = "";
	private final Minecraft mc;
	private String titleText = "";
	private long lastUpdate = System.currentTimeMillis();
	private boolean hasTitle = false;

	private void initializeHints() {
		try (InputStream in = MinecraftBorge.getResourceAsStream("assets/text/nuclear_program/hints.txt");
		     BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			reader.lines().filter(s -> !s.isEmpty()).forEach(this.hints::add);
		} catch (Throwable e) {
			this.hints.clear();
			this.hints.add(e.toString());
		}
	}

	private final Random hintRnd = new Random(2137L);
	private String getHint() {
		long index = System.currentTimeMillis() / 2000;
		this.hintRnd.setSeed(index);
		this.hintRnd.setSeed(this.hintRnd.nextLong());
		this.hintRnd.setSeed(this.hintRnd.nextLong());
		return this.hints.get(this.hintRnd.nextInt(this.hints.size()));
	}

	public LoadingScreenRendererHints(Minecraft mc) {
		super(mc);
		this.mc = mc;

		this.initializeHints();
	}

	public void printText(String var1) {
		this.hasTitle = false;
		this.func_597_c(var1);
	}

	public void displayTitle(String string) {
		this.hasTitle = true;
		this.func_597_c(this.titleText);
	}

	public void displayLoadingString(String string) {
		if(!this.mc.running) {
			if(!this.hasTitle) {
				throw new MinecraftError();
			}
		} else {
			this.lastUpdate = 0L;
			this.phaseText = string;
			this.setLoadingProgress(-1);
			this.lastUpdate = 0L;
		}
	}

	public void func_597_c(String var1) {
		if(!this.mc.running) {
			if(!this.hasTitle) {
				throw new MinecraftError();
			}
		} else {
			this.titleText = var1;
			ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, var2.field_25121_a, var2.field_25120_b, 0.0D, 100.0D, 300.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	@Override
	public void setLoadingProgress(int progress) {
		if(!this.mc.running) {
			if(!this.hasTitle) {
				throw new MinecraftError();
			}
		} else {
			long var2 = System.currentTimeMillis();
			if(var2 - this.lastUpdate >= 20L) {
				this.lastUpdate = var2;
				ScaledResolution var4 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int var5 = var4.getScaledWidth();
				int var6 = var4.getScaledHeight();
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, var4.field_25121_a, var4.field_25120_b, 0.0D, 100.0D, 300.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -200.0F);
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
				Tessellator var7 = Tessellator.instance;
				int var8 = this.mc.renderEngine.getTexture("/gui/background.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var8);
				float var9 = 32.0F;
				var7.startDrawingQuads();
				var7.setColorOpaque_I(4210752);
				var7.addVertexWithUV(0.0D, var6, 0.0D, 0.0D, (float)var6 / var9);
				var7.addVertexWithUV(var5, var6, 0.0D, (float)var5 / var9, (float)var6 / var9);
				var7.addVertexWithUV(var5, 0.0D, 0.0D, (float)var5 / var9, 0.0D);
				var7.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
				var7.draw();
				if(progress >= 0) {
					byte var10 = 100;
					byte var11 = 2;
					int var12 = var5 / 2 - var10 / 2;
					int var13 = var6 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var7.startDrawingQuads();
					var7.setColorOpaque_I(8421504);
					var7.addVertex(var12, var13, 0.0D);
					var7.addVertex(var12, var13 + var11, 0.0D);
					var7.addVertex(var12 + var10, var13 + var11, 0.0D);
					var7.addVertex(var12 + var10, var13, 0.0D);
					var7.setColorOpaque_I(8454016);
					var7.addVertex(var12, var13, 0.0D);
					var7.addVertex(var12, var13 + var11, 0.0D);
					var7.addVertex(var12 + progress, var13 + var11, 0.0D);
					var7.addVertex(var12 + progress, var13, 0.0D);
					var7.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.mc.fontRenderer.drawStringWithShadow(this.titleText, (var5 - this.mc.fontRenderer.getStringWidth(this.titleText)) / 2, var6 / 2 - 4 - 16, 16777215);
				this.mc.fontRenderer.drawStringWithShadow(this.phaseText, (var5 - this.mc.fontRenderer.getStringWidth(this.phaseText)) / 2, var6 / 2 - 4 + 8, 16777215);

				String[] hint = this.getHint().split("[$]");
				for (int i = 0; i < hint.length; i++) {
					this.mc.fontRenderer.drawStringWithShadow(hint[i], (var5 - this.mc.fontRenderer.getStringWidth(hint[i])) / 2, 4 + i * 9, 0x00FFFF);
				}

				Display.update();

				try {
					Thread.yield();
				} catch (Exception var14) {
				}

			}
		}
	}
}
