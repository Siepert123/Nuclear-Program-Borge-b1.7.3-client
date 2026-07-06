package dev.siepert.nuclearprogram.init;

import dev.siepert.nuclearprogram.NuclearProgram;
import net.minecraft.src.*;
import net.minecraftborge.loader.AchievementListBase;

public final class AchievementInit extends AchievementListBase {
	public static AchievementInit instance;
	public static void register() {
		if (instance != null) throw new RuntimeException("Already registered!");
		instance = new AchievementInit();
	}
	private AchievementInit() {
		super();
	}

	public final Achievement concrete = this.register("concrete", 0, 0, BlockInit.concrete);
	public final Achievement furnaceBuilder = this.register("furnaceBuilder", -2, 5, BlockInit.furnaceBuilderLit);
	public final Achievement fallout = this.register("fallout", 3, -1, BlockInit.fallout, this.concrete).setSpecial();
	public final Achievement bloomery = this.register("bloomery", 2, 2, BlockInit.bloomeryLit, AchievementList.mineWood);

	@Override
	public String getName() {
		return "Nuclear Program";
	}

	private Achievement register(String name, int x, int y, ItemStack icon, Achievement parent) {
		return new Achievement(this, NuclearProgram.path(name), x, y, icon, parent).registerAchievement();
	}
	private Achievement register(String name, int x, int y, Block icon, Achievement parent) {
		return this.register(name, x, y, new ItemStack(icon), parent);
	}
	private Achievement register(String name, int x, int y, Item icon, Achievement parent) {
		return this.register(name, x, y, new ItemStack(icon), parent);
	}

	private Achievement register(String name, int x, int y, ItemStack icon) {
		return this.register(name, x, y, icon, null);
	}
	private Achievement register(String name, int x, int y, Block icon) {
		return this.register(name, x, y, icon, null);
	}
	private Achievement register(String name, int x, int y, Item icon) {
		return this.register(name, x, y, icon, null);
	}
}
