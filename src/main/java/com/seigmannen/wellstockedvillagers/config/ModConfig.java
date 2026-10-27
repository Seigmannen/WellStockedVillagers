package com.seigmannen.wellstockedvillagers.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.seigmannen.wellstockedvillagers.WellStockedVillagers;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loaded from config/wellstockedvillagers.json. Missing fields fall back to the defaults below.
 */
public final class ModConfig {
	public static final int MIN_RESTOCKS_PER_DAY = 2;
	public static final int MAX_RESTOCKS_PER_DAY = 10;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(WellStockedVillagers.MOD_ID + ".json");

	// Vanilla villagers work from tick 2000 to 9000 of the day (data/minecraft/timeline/villager_schedule.json).
	private static final int WORK_TICKS_PER_DAY = 7000;
	// WorkAtPoi only re-checks the workstation every 300+ ticks, so each restock can lag by about that much.
	private static final int WORK_CHECK_COOLDOWN = 300;
	// Time for the villager to walk to its workstation when work starts, so the last restock isn't cut off at 9000.
	private static final int WORK_START_MARGIN = 500;
	// Villager.allowedToRestock() uses 2400; never make the gap stricter than vanilla.
	private static final long VANILLA_TICKS_BETWEEN_RESTOCKS = 2400;

	private static ModConfig instance = new ModConfig();

	public int maxUses = 12;
	public int restocksPerDay = 2;

	public static ModConfig get() {
		return instance;
	}

	/**
	 * Spreads the restocks over the work day: the first one can happen shortly after work starts, and the
	 * remaining (restocksPerDay - 1) each need the gap plus up to one workstation check delay. 10/day gives 422 ticks.
	 */
	public long minTicksBetweenRestocks() {
		long gap = (WORK_TICKS_PER_DAY - WORK_START_MARGIN) / (restocksPerDay - 1) - WORK_CHECK_COOLDOWN;
		return Math.min(VANILLA_TICKS_BETWEEN_RESTOCKS, gap);
	}

	public static void setRestocksPerDay(int restocksPerDay) {
		instance.restocksPerDay = restocksPerDay;
		instance.validate();
		save();
	}

	public static void load() {
		ModConfig config = null;
		if (Files.exists(PATH)) {
			try (Reader reader = Files.newBufferedReader(PATH)) {
				config = GSON.fromJson(reader, ModConfig.class);
			} catch (IOException | JsonParseException e) {
				WellStockedVillagers.LOGGER.error("Could not read {}, using defaults", PATH, e);
			}
		}
		if (config == null) {
			config = new ModConfig();
		}
		config.validate();
		instance = config;
		// Rewrites the file, which also drops fields from older versions such as minTicksBetweenRestocks.
		save();
	}

	private static void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(PATH)) {
				GSON.toJson(instance, writer);
			}
		} catch (IOException e) {
			WellStockedVillagers.LOGGER.error("Could not write {}", PATH, e);
		}
	}

	private void validate() {
		maxUses = Math.max(1, maxUses);
		restocksPerDay = Math.clamp(restocksPerDay, MIN_RESTOCKS_PER_DAY, MAX_RESTOCKS_PER_DAY);
	}
}
