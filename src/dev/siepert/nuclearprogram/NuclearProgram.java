package dev.siepert.nuclearprogram;

import dev.objlib.api.IObjModelFactory;
import dev.siepert.nuclearprogram.gui.LoadingScreenRendererHints;
import dev.siepert.nuclearprogram.gui.NuclearProgramRemoteGUI;
import dev.siepert.nuclearprogram.init.*;
import dev.siepert.nuclearprogram.network.NuclearProgramNetHandler;
import dev.siepert.nuclearprogram.recipe.BloomeryRecipes;
import dev.siepert.nuclearprogram.recipe.BuilderFurnaceRecipes;
import dev.siepert.nuclearprogram.recipe.WorkbenchRecipes;
import dev.siepert.nuclearprogram.texturefx.TextureYanoizedFX;
import dev.siepert.nuclearprogram.world.block.BlockMetal;
import dev.siepert.nuclearprogram.world.entity.EntityHowitzerShell;
import dev.siepert.nuclearprogram.world.entity.render.RenderHowitzerShell;
import dev.siepert.nuclearprogram.world.particle.ParticleTextures;
import net.minecraft.client.Minecraft;
import net.minecraftborge.loader.FurnaceRecipesFix;
import net.minecraftborge.loader.ModList;
import net.minecraftborge.loader.TerrainIcon;
import net.minecraftborge.loader.event.EventBusSubscriber;
import net.minecraftborge.loader.event.EventHandler;
import net.minecraftborge.loader.event.IModLifecycleListener;
import net.minecraftborge.loader.event.lifecycle.ModInitializationEvent;
import net.minecraftborge.loader.event.lifecycle.ModPostInitializationEvent;
import net.minecraftborge.loader.event.lifecycle.ModPreInitializationEvent;
import net.minecraftborge.loader.event.misc.ReplaceSimilarBlocksEvent;
import net.minecraftborge.loader.event.register.*;

@EventBusSubscriber(NuclearProgram.MODID)
public class NuclearProgram implements IModLifecycleListener {
	public static final int EXPLOSION_MS_BUDGET = 20;
	public static final String MODID = "nuclear_program";
	public static String path(String path) {
		return MODID + "/" + path;
	}

	public static IObjModelFactory OBJ_FACTORY;

	@Override
	public void modPreInit(ModPreInitializationEvent event) {
		IObjModelFactory factory = null;
		try {
			if (ModList.get().getLoadedMods().contains("objlib")) {
				factory = (IObjModelFactory) Class.forName("dev.objlib.OBJModelFactoryImpl").newInstance();
			} else {
				System.err.println("OBJ Library not present!");
			}
		} catch (Throwable e) {
			System.err.println("Failed to detect OBJ Library: " + e);
		}
		OBJ_FACTORY = factory;
	}

	@Override
	public void modInit(ModInitializationEvent event) {
		event.registerLanguage();

		event.registerGUIFactory(NuclearProgramRemoteGUI.INSTANCE);
		event.registerNetHandler(NuclearProgramNetHandler.register());

		AchievementInit.register();
		TagInit.registerVanillaTags();
		TagInit.registerBlockTags();
		TagInit.registerItemTags();
		WorldGenInit.register();
		EntityInit.register();
	}

	@Override
	public void modPostInit(ModPostInitializationEvent event) {
		Minecraft mc = Minecraft.getTheMinecraft();
		mc.loadingScreen = new LoadingScreenRendererHints(mc);
	}

	@EventHandler
	public static void registerBlocks(IdAllocationEvent.Blocks event) {
		BlockInit.register(event);
		TileEntityInit.register();
	}

	@EventHandler
	public static void registerItems(IdAllocationEvent.Items event) {
		ItemInit.register(event);
		BlockInit.registerItemBlocks();
	}

	@EventHandler
	public static void registerRecipes(AddRecipesEvent event) {
		RecipeInit.crafting(event);
		RecipeInit.furnace(FurnaceRecipesFix.smelting());
		RecipeInit.workbench(WorkbenchRecipes.crafting());
		RecipeInit.builderFurnace(BuilderFurnaceRecipes.smelting());
		RecipeInit.bloomery(BloomeryRecipes.blooming());
	}

	@EventHandler
	public static void registerTextureFX(RegisterTextureFXEvent event) {
		event.register(new TextureYanoizedFX((TerrainIcon) ItemInit.ingotYanoizedKaupium.itemTexture,
				path("mask_IngotYanoizedKaupium.png"), 1.0F));
		event.register(new TextureYanoizedFX((TerrainIcon) BlockInit.blockMetal.blockTextures[BlockMetal.YANOIZED_KAUPIUM],
				path("mask_BlockYanoizedKaupium.png"), 1.0F));
	}

	@EventHandler
	public static void registerAdditionalIcons(TerrainStitchEvent event) {
		for (int i = 0; i < 8; i++) {
			ParticleTextures.generic[i] = event.registerIcon(path("particle/generic" + i), 16, 16);
		}
		for (int i = 0; i < 8; i++) {
			ParticleTextures.effect[i] = event.registerIcon(path("particle/effect" + i), 16, 16);
		}
	}

	@EventHandler
	public static void registerSounds(ExtractSoundsEvent event) {
		event.extract("assets/sound/nuclear_program/");
	}

	@EventHandler
	public static void registerEntityRenders(RegisterEntityRenderersEvent event) {
		event.register(EntityHowitzerShell.class, new RenderHowitzerShell());
	}

	@EventHandler
	public static void fixStatistics(ReplaceSimilarBlocksEvent event) {
		if (!BlockInit.available) return;
		event.replace(BlockInit.furnaceBuilderLit.blockID, BlockInit.furnaceBuilderIdle.blockID);
	}
}
