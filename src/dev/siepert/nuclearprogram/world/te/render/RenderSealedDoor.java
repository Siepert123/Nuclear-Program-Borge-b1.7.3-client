package dev.siepert.nuclearprogram.world.te.render;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.util.Easing;
import dev.siepert.nuclearprogram.util.Vec2f;
import dev.siepert.nuclearprogram.world.block.render.RenderBlockSealedDoor;
import dev.siepert.nuclearprogram.world.te.TileEntitySealedDoor;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class RenderSealedDoor extends TileEntitySpecialRenderer<TileEntitySealedDoor> {
	public static final RenderSealedDoor INSTANCE = new RenderSealedDoor();

	private RenderSealedDoor() {}

	private final RenderBlocks renderer = new RenderBlocks();
	private final RenderBlockSealedDoor instance = RenderBlockSealedDoor.INSTANCE;

	private static float pos(int px32) {
		return px32 / 32.0F;
	}

	private static final Vec2f[] SCREW_OFFSETS = {
			new Vec2f(pos(-2), pos(-2)),
			new Vec2f(pos(16), pos(-2)),
			new Vec2f(pos(34), pos(-2)),
			new Vec2f(pos(-2), pos(15)),
			new Vec2f(pos(-2), pos(32)),
			new Vec2f(pos(-2), pos(49)),
			new Vec2f(pos(-2), pos(66)),
			new Vec2f(pos(34), pos(15)),
			new Vec2f(pos(34), pos(32)),
			new Vec2f(pos(34), pos(49)),
			new Vec2f(pos(34), pos(66)),
			new Vec2f(pos(16), pos(66)),
	};

	private World worldObj;

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		this.worldObj = world;
	}

	@Override
	public String getRenderTexture(TileEntitySealedDoor te) {
		return "terrain.png";
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		this.renderDo((TileEntitySealedDoor) te, x, y, z, partialTick);
	}

	private void renderDo(TileEntitySealedDoor te, double x, double y, double z, float pt) {
		int meta = te.getBlockMetadata();
		if ((meta & 0b1000) != 0) return;
		RenderHelper.enableStandardItemLighting();
		Tessellator tes = Tessellator.instance;
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5, y, z + 0.5);
		GL11.glRotatef((te.getBlockMetadata() & 0b0011) * -90.0F + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, 0.0F, -0.5F);

		GL11.glPushMatrix();
		GL11.glTranslatef(Easing.OUT_QUAD.ease(te.getSlide(pt)) * 0.99F, 0.0F, 0.0F);
		tes.startDrawingQuads();
		BlockInit.sealedDoor.pass = 1;
		BlockInit.sealedDoor.setBlockBounds(0.0F, 0.0F, 0.5F+(0.0625F*0.5F), 1.0F, 2.0F, 1.0F-0.0625F);
		this.instance.renderFaces(this.renderer, tes, BlockInit.sealedDoor, 0, 0, 0);
		BlockInit.sealedDoor.pass = 0;
		tes.draw();
		GL11.glPopMatrix();

		GL11.glTranslatef(-0.5F, -0.5F, 0.875F + (0.0625F*0.5F) + te.getScrewExpansion(pt));
		for (Vec2f vec : SCREW_OFFSETS) {
			GL11.glPushMatrix();
			GL11.glTranslatef(vec.x() + 0.5F, vec.y() + 0.5F, 0.0F);
			GL11.glRotatef(te.getScrewRot(pt) * 360.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
			tes.startDrawingQuads();
			this.instance.renderScrew(Block.stone, tes, this.renderer, Block.blockIron.blockTexture);
			tes.draw();
			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();

		BlockInit.sealedDoor.setBlockBoundsBasedOnState(te.worldObj, te.xCoord, te.yCoord, te.zCoord);
		Block.stone.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}
