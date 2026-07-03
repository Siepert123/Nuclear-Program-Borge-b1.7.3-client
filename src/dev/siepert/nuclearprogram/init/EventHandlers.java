package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.Nothing;
import dev.siepert.nuclearprogram.NuclearProgram;
import dev.siepert.nuclearprogram.weapon.BackendExplosionHandler;
import dev.siepert.nuclearprogram.weapon.NukeTypes;
import dev.siepert.nuclearprogram.weapon.WorldActiveExplosions;
import dev.siepert.nuclearprogram.world.NuclearProgramWorldAccess;
import dev.siepert.nuclearprogram.world.entity.EntityExplosionHelper;
import dev.siepert.nuclearprogram.world.entity.EntityHowitzerShell;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.World;
import net.minecraftborge.loader.event.Event;
import net.minecraftborge.loader.event.EventBusSubscriber;
import net.minecraftborge.loader.event.EventHandler;
import net.minecraftborge.loader.event.entity.EntityDropLootEvent;
import net.minecraftborge.loader.event.gui.RenderOverlayGuiEvent;
import net.minecraftborge.loader.event.misc.ChatCommandEvent;
import net.minecraftborge.loader.event.world.ChangeWorldEvent;
import net.minecraftborge.loader.event.world.WorldTickEvent;
import org.lwjgl.opengl.GL11;

@EventBusSubscriber(NuclearProgram.MODID)
public class EventHandlers {
	@EventHandler
	public static void worldChanged(ChangeWorldEvent event) {
		NuclearProgramWorldAccess.INSTANCE.setWorld(event.getWorld());
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
			if (cmd.startsWith("/goon ")) {
				event.setCanceled(true);
				String[] params = cmd.substring("/goon ".length()).trim().split(" ");
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
				EntityExplosionHelper entity = new EntityExplosionHelper(world, sender.posX, sender.posY, sender.posZ, NukeTypes.CHARGE);
				world.entityJoinedWorld(entity);
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
	public static void changeWorld(ChangeWorldEvent event) {
		if (event.getWorld() == null) {
			WorldActiveExplosions.cache = null;
		} else {
			WorldActiveExplosions.cache = WorldActiveExplosions.get(event.getWorld());
			WorldActiveExplosions.cache.setWorld(event.getWorld());
		}
	}

	@EventHandler
	public static void tickWorld(WorldTickEvent event) {
		if (event.getPhase() == Event.Phase.POST) {
			WorldActiveExplosions.tick();
			if (BackendExplosionHandler.shockwaveTicks > 0) BackendExplosionHandler.shockwaveTicks--;
		}
	}

	@EventHandler
	public static void renderGUI(RenderOverlayGuiEvent event) {
		if (event.getLayer() == RenderOverlayGuiEvent.Layer.PRE) {
			int ticks = BackendExplosionHandler.shockwaveTicks;
			if (ticks > 0) {
				double mul = ticks * 0.05;
				double time = Math.toIntExact(System.currentTimeMillis() % 2000) * 0.003 * Math.PI;
				GL11.glTranslated(Math.sin(time * 2) * mul, Math.sin(time) * mul, 0.0);
			}
		}
	}
}
