package be.hctel.renaissance.global.mapmanager;

import org.bukkit.plugin.Plugin;
import be.hctel.api.config.Config;

public class MapManager {
	
	private Config mapConfig;
	private Plugin plugin;
	
	
	public MapManager(Plugin plugin) {
		this.plugin = plugin;
		
		try {
			this.mapConfig = new Config(plugin, "maps.json");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
