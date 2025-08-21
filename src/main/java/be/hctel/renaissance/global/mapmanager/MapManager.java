package be.hctel.renaissance.global.mapmanager;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import be.hctel.api.config.Config;
import be.hctel.renaissance.deathrun.DeathRun;

public class MapManager {
	
	@SuppressWarnings("unused")
	private Config mapConfig;
	@SuppressWarnings("unused")
	private DeathRun plugin;
	
	
	public MapManager(DeathRun plugin) {
		this.plugin = plugin;
		try {
			this.mapConfig = new Config(plugin, "maps.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(World W : Bukkit.getWorlds()) {
			W.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
			W.setGameRule(GameRule.DO_FIRE_TICK, false);
			W.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			W.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		}
	}
	
}
