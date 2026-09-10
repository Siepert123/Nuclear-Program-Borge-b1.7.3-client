package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.world.te.TileEntityMachineBase;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public abstract class RenderMachineBase<T extends TileEntityMachineBase> extends TileEntitySpecialRenderer<T> {
	private final Class<T> type;
	protected final Random rnd = new Random();

	public RenderMachineBase(Class<T> type) {
		super();
		this.type = type;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y, z + 0.5);
		T machine = this.type.cast(te);
		if (machine.collapsed) {
			this.rnd.setSeed(machine.hashCode());
			GL11.glTranslatef(0.0F, -1.0F, 0.0F);
			GL11.glRotatef(10.0F, this.rnd.nextFloat() - 0.5F, this.rnd.nextFloat() - 0.5F, this.rnd.nextFloat() - 0.5F);
		}
		this.renderMachine(machine, x, y, z, partialTick);
		GL11.glPopMatrix();
	}

	protected abstract void renderMachine(T te, double x, double y, double z, float partialTick);
}
