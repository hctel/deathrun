package be.hctel.renaissance.cosmetics;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import be.hctel.renaissance.deathrun.nongame.utils.SpawnLocation;

public enum GameMap {
	
	GARDENS("To bee or not to bee", "DR_Gardens", new SpawnLocation("DR_Gardens", -2, 64, -35, 0.1f, 0.1f), new SpawnLocation("DR_Gardens", -3, 61, -21, 0.1f, 0.1f));
	
	String mapName;
	String systemName;
	SpawnLocation playersLocation;
	SpawnLocation deathLocation;
	private static HashMap<String, GameMap> lookup = new HashMap<String, GameMap>();
	static {
		for(GameMap m : GameMap.values()) {
			lookup.put(m.getSystemName(), m);
		}
		
	}
	
	private GameMap(String mapName, String systemName, SpawnLocation playersLocation, SpawnLocation deathLocation) {
		this.mapName = mapName;
		this.systemName = systemName;
		this.playersLocation = playersLocation;
		this.deathLocation = deathLocation;
	}
	public String getMapName() {
		return mapName;
	}
	public String getSystemName() {
		return systemName;
	}
 	public Location getDeathStart() {
		return this.deathLocation.getLocation();
	}
	public Location getPlayersStart() {
		return this.playersLocation.getLocation();
	}
	public static GameMap getFromSystemName(String name) {
		return lookup.get(name);
	}
}


