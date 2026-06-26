package dev.siepert.nuclearprogram.texturefx;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.src.TextureFX;
import net.minecraftborge.MinecraftBorge;
import net.minecraftborge.loader.BorgeMath;
import net.minecraftborge.loader.TerrainIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class TextureYanoizedFX extends TextureFX {
	private static final int BASE_COLOR = 0xFFFDD7E4;
	private static final float BASE_RED = (BASE_COLOR >> 16 & 255) / 255.0F;
	private static final float BASE_GREEN = (BASE_COLOR >> 8 & 255) / 255.0F;
	private static final float BASE_BLUE = (BASE_COLOR & 255) / 255.0F;

	private final float intensity;
	private final byte[] baseTexture = new byte[1024];
	private final boolean[] mask = new boolean[256];

	public TextureYanoizedFX(TerrainIcon texture, String mask, float intensity) {
		super(texture);
		this.intensity = intensity;
		final Minecraft mc = Minecraft.getTheMinecraft();

		try {
			final BufferedImage maskPNG = ImageIO.read(MinecraftBorge.getResource("assets/texturefx/" + mask));
			final int[] data = new int[256];
			maskPNG.getRGB(0, 0, 16, 16, data, 0, 16);
			for (int i = 0; i < 256; i++) {
				this.mask[i] = (data[i] & 0xFFFFFF) > 0;
			}
		} catch (Exception e) {
			System.err.println("Exception loading texture fx bitmask");
			e.printStackTrace(System.err);
		}

		try {
			final BufferedImage map = mc.renderEngine.getTerrainTextureMap().getTexture();
			final int[] data = new int[256];
			map.getRGB(this.textureX, this.textureY, 16, 16, data, 0, 16);
			for (int i = 0; i < 256; i++) {
				this.baseTexture[i * 4] = (byte) (data[i] >> 16 & 255);
				this.baseTexture[i * 4 + 1] = (byte) (data[i] >> 8 & 255);
				this.baseTexture[i * 4 + 2] = (byte) (data[i] & 255);
				this.baseTexture[i * 4 + 3] = (byte) (data[i] >> 24 & 255);
			}
		} catch (Exception e) {
			System.err.println("Could not obtain base texture");
			e.printStackTrace(System.err);
		}
	}

	@Override
	public void onTick() {
		final int ms = Math.toIntExact(System.currentTimeMillis() % 2000);
		final double second = ms * 0.001;
		final float sin = (float) Math.sin(second * Math.PI);
		final float a, r, g, b;
		if (sin < 0.0) {
			float corrected = 1.0F + sin;
			a = corrected * this.intensity;
			r = BASE_RED * corrected;
			g = BASE_GREEN * corrected;
			b = BASE_BLUE * corrected;
		} else {
			a = this.intensity;
			r = BorgeMath.clampedLerp(BASE_RED, 1.0F, sin);
			g = BorgeMath.clampedLerp(BASE_GREEN, 1.0F, sin);
			b = BorgeMath.clampedLerp(BASE_BLUE, 1.0F, sin);
		}

		System.arraycopy(this.baseTexture, 0, this.imageData, 0, 1024);
		final byte ba = (byte) (255);
		final byte br = (byte) (r * 255);
		final byte bg = (byte) (g * 255);
		final byte bb = (byte) (b * 255);
		if (a > 0.95F) {
			for (int i = 0; i < 256; i++) {
				if (this.mask[i]) {
					this.imageData[i * 4] = br;
					this.imageData[i * 4 + 1] = bg;
					this.imageData[i * 4 + 2] = bb;
					this.imageData[i * 4 + 3] = ba;
				}
			}
		} else {
			for (int i = 0; i < 256; i++) {
				if (this.mask[i]) {
					this.imageData[i * 4] = this.larp(this.baseTexture[i * 4] & 255, br & 255, a);
					this.imageData[i * 4 + 1] = this.larp(this.baseTexture[i * 4 + 1] & 255, bg & 255, a);
					this.imageData[i * 4 + 2] = this.larp(this.baseTexture[i * 4 + 2] & 255, bb & 255, a);
					this.imageData[i * 4 + 3] = ba;
				}
			}
		}
	}

	private byte larp(float from, float to, float delta) {
		return (byte) (int) Math.floor(BorgeMath.lerp(from, to, delta));
	}
}
