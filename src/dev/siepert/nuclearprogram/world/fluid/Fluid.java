package dev.siepert.nuclearprogram.world.fluid;

import dev.siepert.nuclearprogram.init.ItemInit;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraftborge.loader.Icon;
import net.minecraftborge.loader.IconRegister;

import java.util.Arrays;
import java.util.Objects;

public class Fluid {
	public static final int ID_SIZE = 256;

	public static final Fluid[] fluidsList = new Fluid[ID_SIZE];
	public static final int[] temperatureLookup = new int[fluidsList.length];
	public static final int[] colorLookup = new int[fluidsList.length];
	public static final boolean[] gaseousLookup = new boolean[fluidsList.length];

	public Fluid(int fluidID) {
		if (fluidID == 0) throw new IllegalArgumentException("Fluid ID #0 is reserved for null");
		if (fluidID < 0) throw new IllegalArgumentException("Fluid ID must be positive");
		if (fluidID >= ID_SIZE) throw new IllegalArgumentException("Fluid ID must not exceed " + (ID_SIZE - 1));
		this.fluidID = fluidID;
		if (fluidsList[fluidID] != null) {
			throw new RuntimeException("Duplicate fluid ID: #" + fluidID);
		} else {
			fluidsList[fluidID] = this;
			temperatureLookup[fluidID] = 20;
			colorLookup[fluidID] = 0xFFFFFF;
			gaseousLookup[fluidID] = false;
		}
	}
	public final int fluidID;

	protected String unlocalizedName;
	protected String registryName;
	public Icon fluidTexture;

	public Fluid setName(String name) {
		this.unlocalizedName = "fluid." + name;
		this.registryName = name;
		return this;
	}
	public Fluid setTemperature(int celsius) {
		temperatureLookup[this.fluidID] = celsius;
		return this;
	}
	public Fluid setColor(int color) {
		colorLookup[this.fluidID] = color;
		return this;
	}
	public Fluid setColor(int r, int g, int b) {
		return this.setColor((r << 16) | (g << 8) | (b));
	}
	public Fluid setColor(float r, float g, float b) {
		return this.setColor(MathHelper.floor_float(r * 255), MathHelper.floor_float(g * 255), MathHelper.floor_float(b * 255));
	}
	public Fluid setGaseous() {
		gaseousLookup[this.fluidID] = true;
		return this;
	}

	public String getUnlocalizedName() {
		return this.unlocalizedName;
	}
	public String getRegistryName() {
		return this.registryName;
	}

	public void registerIcons(IconRegister register) {
		this.fluidTexture = register.getTexture("fluid/" + this.getRegistryName(), 16, 16);
	}

	public static ItemStack createItemRepresentation(Fluid fluid, long amount, byte pressure) {
		Objects.requireNonNull(fluid, "fluid");
		ItemStack stack = new ItemStack(ItemInit.fluid, 1, fluid.fluidID);
		stack.itemNBT = new NBTTagCompound(2);
		stack.itemNBT.setLong("fluidAmount", amount);
		stack.itemNBT.setByte("fluidPressure", pressure);
		return stack;
	}
	public static String getUnlocalizedName(Fluid fluid) {
		return fluid != null ? fluid.getUnlocalizedName() : "fluid.none";
	}

	static {
		Arrays.fill(temperatureLookup, 20);
		Arrays.fill(colorLookup, 0x888888);
		Arrays.fill(gaseousLookup, false);
	}
}
