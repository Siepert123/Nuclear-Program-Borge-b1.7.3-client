package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.Nothing;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.util.SaveHandlerHook;
import dev.siepert.nuclearprogram.weapon.BackendExplosionHandler;
import dev.siepert.nuclearprogram.weapon.WorldActiveExplosions;
import dev.siepert.nuclearprogram.world.NuclearProgramWorldAccess;
import dev.siepert.nuclearprogram.world.block.BlockRBMKColumn;
import dev.siepert.nuclearprogram.world.entity.EntityHowitzerShell;
import dev.siepert.nuclearprogram.world.mapdata.WorldFalloutClouds;
import dev.siepert.nuclearprogram.world.te.TileEntityRBMKColumn;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraftborge.loader.BorgeMath;
import net.minecraftborge.loader.event.Event;
import net.minecraftborge.loader.event.EventBusSubscriber;
import net.minecraftborge.loader.event.EventHandler;
import net.minecraftborge.loader.event.entity.EntityDropLootEvent;
import net.minecraftborge.loader.event.entity.player.PlayerCreateItemEvent;
import net.minecraftborge.loader.event.entity.player.PlayerDestroyBlockEvent;
import net.minecraftborge.loader.event.gui.RenderOverlayGuiEvent;
import net.minecraftborge.loader.event.misc.ChatCommandEvent;
import net.minecraftborge.loader.event.misc.FurnaceBurnTimeEvent;
import net.minecraftborge.loader.event.render.GetFOVModifierEvent;
import net.minecraftborge.loader.event.render.GetFogColorEvent;
import net.minecraftborge.loader.event.render.RenderRainSnowEvent;
import net.minecraftborge.loader.event.world.ChangeWorldEvent;
import net.minecraftborge.loader.event.world.WorldTickEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EventBusSubscriber(NuclearProgram.MODID)
public class EventHandlers {
	private static final Random rnd = new Random();

	@EventHandler
	public static void worldChanged(ChangeWorldEvent event) {
		NuclearProgramWorldAccess.INSTANCE.setWorld(event.getWorld());
		SaveHandlerHook.set(event.getWorld());
		if (event.getWorld() == null) {
			WorldActiveExplosions.cache = null;
			WorldFalloutClouds.cache = null;
		} else {
			WorldActiveExplosions.cache = WorldActiveExplosions.get(event.getWorld());
			WorldActiveExplosions.cache.setWorld(event.getWorld());

			WorldFalloutClouds.cache = WorldFalloutClouds.get(event.getWorld());
		}
	}

	@EventHandler
	public static void getItemBurnTime(FurnaceBurnTimeEvent event) {
		if (event.isCanceled()) return;
		int itemID = event.getStack().itemID;
		if (itemID == ItemInit.cokeCoal.shiftedIndex) event.setBurnTime(3200);
		if (itemID == ItemInit.cokePetroleum.shiftedIndex) event.setBurnTime(3200);
	}

	private static double parsePosition(String string, double offset) {
		double o = 0.0;
		if (string.startsWith("~")) {
			string = string.substring(1);
			o = offset;
		}
		if (string.isEmpty()) return o;
		double pos = Double.parseDouble(string);
		return pos + o;
	}
	@EventHandler
	public static void processCommand(ChatCommandEvent event) {
		if (event.isCanceled()) return;
		try {
			String cmd = event.getCommand();
			EntityPlayer sender = event.getSender();
			World world = sender.worldObj;
			if (cmd.startsWith("/playEvent")) {
				event.setCanceled(true);
				String[] params = cmd.substring("/playEvent".length()).trim().split(" ");
				if (params.length == 0) {
					event.sendStatus("Missing 'event' argument");
					return;
				}
				int eventID = Integer.parseInt(params[0]);
				if (params.length == 1) {
					world.playEvent(sender, eventID, (int) sender.posX, (int) sender.posY, (int) sender.posZ, 0);
					return;
				}
				if (params.length < 4) {
					event.sendStatus("Invalid 'xyz' arguments");
					return;
				}
				int x = Integer.parseInt(params[1]);
				int y = Integer.parseInt(params[2]);
				int z = Integer.parseInt(params[3]);
				int data = params.length > 4 ? Integer.parseInt(params[4]) : 0;
				world.playEvent(sender, eventID, x, y, z, data);
				return;
			}
			if (cmd.startsWith("/goon1")) {
				event.setCanceled(true);
				String[] params = cmd.substring("/goon1".length()).trim().split(" ");
				if (params.length < 3) {
					event.sendStatus("Invalid 'xyz' arguments");
					return;
				}
				final double x, y, z;
				try {
					x = parsePosition(params[0], sender.posX);
					y = parsePosition(params[1], sender.posY);
					z = parsePosition(params[2], sender.posZ);
				} catch (Exception e) {
					event.sendStatus("Malformed 'xyz' arguments: " + e);
					return;
				}
				final float yaw, pitch;
				if (params.length > 3) {
					if (params.length < 5) {
						event.sendStatus("Invalid 'yaw pitch' arguments");
						return;
					}
					try {
						yaw = Float.parseFloat(params[3]);
						pitch = Float.parseFloat(params[4]);
					} catch (Exception e) {
						event.sendStatus("Malformed 'yaw pitch' arguments: " + e);
						return;
					}
				} else {
					yaw = 0.0F;
					pitch = -45.0F;
				}

				EntityHowitzerShell entity = new EntityHowitzerShell(world);
				entity.setPosition(x, y, z);
				entity.setRotation(yaw, pitch);
				entity.calculations();
				entity.loadChunks();
				world.entityJoinedWorld(entity);
				world.playEvent(2137, (int)x, (int)y, (int)z, 0);

				return;
			}
			if (cmd.startsWith("/goon2")) {
				event.setCanceled(true);
				BackendExplosionHandler.shockwaveTicks = 80;
				return;
			}
			if (cmd.startsWith("/goon3")) {
				event.setCanceled(true);
				world.spawnParticle("nuclear_program/pollution", sender.posX, sender.posY, sender.posZ, 0, 0, 0);
				return;
			}
			Nothing.none();
		} catch (Exception e) {
			event.sendStatus("Exception handling command " + event.getCommand() + " (handler: " + NuclearProgram.MODID + "):");
			event.sendStatus(e.toString());
		}
	}

	@EventHandler
	public static void entityDropLoot(EntityDropLootEvent event) {
		Entity entity = event.getEntity();
		if (entity.getClass() == EntityZombie.class) {
			entity.dropItem(ItemInit.potato.shiftedIndex, 1);
		}
	}

	@EventHandler
	public static void playerCraftItem(PlayerCreateItemEvent event) {
		EntityPlayer player = event.getEntity();
		ItemStack stack = event.getStack();
		if (player == null || stack == null) return;

		int itemID = stack.itemID;
		int metadata = stack.getItemDamage();
		int count = stack.stackSize;

		if (itemID == BlockInit.concrete.blockID) {
			player.triggerAchievement(AchievementInit.instance.concrete);
		}
		if (itemID == BlockInit.bloomeryIdle.blockID) {
			player.triggerAchievement(AchievementInit.instance.bloomery);
		}
	}

	@EventHandler
	public static void tickWorld(WorldTickEvent event) {
		if (event.getPhase() == Event.Phase.POST) {
			World world = event.getWorld();
			WorldActiveExplosions.tick();
			WorldFalloutClouds.tick(world);
			if (BackendExplosionHandler.shockwaveTicks > 0) BackendExplosionHandler.shockwaveTicks--;

			if (WorldFalloutClouds.cache != null && (world.getWorldTime() % 100) == 0) {
				for (WorldFalloutClouds.CloudData cloud : WorldFalloutClouds.cache.clouds) {
					int chunkX = cloud.x * 2;
					int chunkZ = cloud.z * 2;
					int ox = cloud.x / 8;
					int oz = cloud.z / 8;
					int range = (int)(cloud.radius * 2);
					float r2 = cloud.radius * cloud.radius * 64;
					for (int x = chunkX - range; x <= chunkX + range; x++) {
						for (int z = chunkZ - range; z <= chunkZ; z++) {
							if (world.blockExists(x << 4, 64, z << 4)) {
								int cx = (x << 4) + world.rand.nextInt(16);
								int cz = (z << 4) + world.rand.nextInt(16);
								int dx = cx - ox;
								int dz = cz - oz;
								if ((dx * dx) + (dz * dz) < r2) {
									int cy = world.getHeightValue(cx, cz);
									if (world.getBlockMaterial(cx, cy-1, cz).isSolid()) {
										world.setBlockWithNotify(cx, cy, cz, BlockInit.fallout.blockID);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static final ArrayList<String> overlayTooltip = new ArrayList<>(16);
	@EventHandler
	public static void renderGUI(RenderOverlayGuiEvent event) {
		switch (event.getLayer()) {
			case PRE: {
				int ticks = BackendExplosionHandler.shockwaveTicks;
				if (ticks > 0) {
					double mul = ticks * 0.05;
					double time = Math.toIntExact(System.currentTimeMillis() % 2000) * 0.003 * Math.PI;
					GL11.glTranslated(Math.sin(time * 2) * mul, Math.sin(time) * mul, 0.0);
				}
				break;
			}
			case DEBUG_SCREEN: {
				World world = event.getMc().theWorld;
				MovingObjectPosition select = event.getMc().objectMouseOver;
				if (world != null && select != null) {
					int x = select.blockX;
					int y = select.blockY;
					int z = select.blockZ;
					Block block = Block.blocksList[world.getBlockId(x, y, z)];
					if (block instanceof BlockRBMKColumn) {
						TileEntity te = world.getBlockTileEntity(x, y - world.getBlockMetadata(x, y, z), z);
						if (te instanceof TileEntityRBMKColumn) {
							((TileEntityRBMKColumn) te).debug(overlayTooltip);
						} else overlayTooltip.add("[MALFORMED RBMK COLUMN]");

						GuiIngame gui = event.getIngameGUI();
						FontRenderer font = Minecraft.getTheMinecraft().fontRenderer;
						StringTranslate translate = StringTranslate.getInstance();
						gui.drawString(font, translate.translateNamedKey(block.getBlockName()) + " debug data", 2, 100, 0xFFFFFF);
						int pos = 110;
						for (String s : overlayTooltip) {
							gui.drawString(font, s, 2, pos, 0xE0E0E0);
							pos += 8;
						}

						overlayTooltip.clear();
					}
				}
				break;
			}
		}
	}

	@EventHandler
	public static void getFOV(GetFOVModifierEvent event) {
		int ticks = BackendExplosionHandler.shockwaveTicks;
		if (ticks > 0) {
			if ((ticks & 1) == 0) {
				double larp = ticks / 80.0;
				event.setFOV(BorgeMath.clampedLerp(event.getFOV(), event.getFOV() - 15.0F, larp));
			}
		}
	}

	@EventHandler
	public static void renderRainSnow(RenderRainSnowEvent event) {
		if (WorldFalloutClouds.cache != null) {
			List<WorldFalloutClouds.CloudData> clouds = WorldFalloutClouds.cache.clouds;
			int playerX = MathHelper.floor_double(event.getCamera().posX * 0.125);
			int playerZ = MathHelper.floor_double(event.getCamera().posZ * 0.125);
			if (WorldFalloutClouds.CloudData.anyInRange(clouds, playerX, playerZ)) {
				Minecraft mc = event.getMC();
				float pt = event.getPartialTick();
				float ticksRan = Minecraft.getTicksRan() + pt;
				Entity cam = event.getCamera();
				World world = mc.theWorld;

				event.setCanceled(true);
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/environment/snow.png"));
				int px = MathHelper.floor_double(cam.posX);
				int py = MathHelper.floor_double(cam.posY);
				int pz = MathHelper.floor_double(cam.posZ);
				double x = cam.lastTickPosX + (cam.posX - cam.lastTickPosX) * pt;
				double y = cam.lastTickPosY + (cam.posY - cam.lastTickPosY) * pt;
				double z = cam.lastTickPosZ + (cam.posZ - cam.lastTickPosZ) * pt;

				byte range = (byte) (mc.gameSettings.fancyGraphics ? 10 : 5);

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				Tessellator tes = Tessellator.instance;
				tes.startDrawingQuads();
				tes.setColorOpaque_F(0.4F, 0.4F, 0.4F);
				tes.setTranslationD(-x, -y, -z);
				int max = py + 64;
				for (int bx = px - range; bx <= px + range; bx++) {
					for (int bz = pz - range; bz <= pz + range; bz++) {
						int by = world.findTopSolidBlock(bx, bz);
						int height = max - by;
						if (height > 0) {
							rnd.setSeed(bx * bx * 2137L + bz * bz * 42069L + 12345L);
							double offY = (ticksRan * 0.001) + (rnd.nextGaussian());
							double offX = rnd.nextInt(64) * 0.0625 * 0.25;
							tes.addVertexWithUV(bx, by, bz + 0.5, 0.0F + offX, offY);
							tes.addVertexWithUV(bx + 1, by, bz + 0.5, 1.0F + offX, offY);
							tes.addVertexWithUV(bx + 1, max, bz + 0.5, 1.0F + offX, height * 0.25 + offY);
							tes.addVertexWithUV(bx, max, bz + 0.5, 0.0F + offX, height * 0.25 + offY);
							tes.addVertexWithUV(bx + 0.5, by, bz, 0.0F + offX, offY);
							tes.addVertexWithUV(bx + 0.5, by, bz + 1, 1.0F + offX, offY);
							tes.addVertexWithUV(bx + 0.5, max, bz + 1, 1.0F + offX, height * 0.25 + offY);
							tes.addVertexWithUV(bx + 0.5, max, bz, 0.0F + offX, height * 0.25 + offY);
						}
					}
				}
				tes.setTranslationD(0.0, 0.0, 0.0);
				tes.draw();

				GL11.glEnable(GL11.GL_CULL_FACE);
			}
		}
	}

	@EventHandler
	public static void getFogColor(GetFogColorEvent event) {
		if (WorldFalloutClouds.cache != null) {
			List<WorldFalloutClouds.CloudData> clouds = WorldFalloutClouds.cache.clouds;
			int playerX = MathHelper.floor_double(event.getCamera().posX * 0.125);
			int playerZ = MathHelper.floor_double(event.getCamera().posZ * 0.125);
			if (WorldFalloutClouds.CloudData.anyInRange(clouds, playerX, playerZ)) {
				event.getColor().set(0.1F, 0.1F, 0.1F);
			}
		}
	}
}
