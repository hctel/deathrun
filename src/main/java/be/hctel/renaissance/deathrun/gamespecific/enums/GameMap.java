package be.hctel.renaissance.deathrun.gamespecific.enums;

import java.util.HashMap;

import org.bukkit.Location;
import be.hctel.renaissance.deathrun.nongame.utils.SpawnLocation;

public enum GameMap {
	
	GARDENS("To Bee Or Not To Bee", "DR_Gardens", new SpawnLocation("DR_Gardens", -2, 64, -35, 0.1f, 0.1f), new SpawnLocation("DR_Gardens", -3, 61, -21, 0.1f, 0.1f));
	
	String mapName;
	String systemName;
	SpawnLocation spawn;
	SpawnLocation deathSpawn;
	private static HashMap<String, GameMap> lookup = new HashMap<String, GameMap>();
	static {
		for(GameMap m : GameMap.values()) {
			lookup.put(m.getSystemName(), m);
		}
		
	}
	
	private GameMap(String mapName, String systemName, SpawnLocation spawn, SpawnLocation deathSpawn) {
		this.mapName = mapName;
		this.systemName = systemName;
		this.deathSpawn = deathSpawn;
		this.spawn = spawn;
	}
	public String getMapName() {
		return mapName;
	}
	public String getSystemName() {
		return systemName;
	}
	public Location getDeathStart() {
		return this.deathSpawn.getLocation();
	}
	public Location getRunnerStart() {
		return this.spawn.getLocation();
	}
	public static GameMap getFromSystemName(String name) {
		return lookup.get(name);
	}
}


