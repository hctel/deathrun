package be.hctel.renaissance.global.mapmanager;

import org.bukkit.Location;
import org.bukkit.World;
import org.json.JSONObject;

import be.hctel.api.config.Config;

/**
 * 
 * @author <a href="https://hctel.net/">hctel</a>
 *
 */
public class GameMap {
	private World world;
	private Location spawnLocation;
	private Config worldConfig;
	
	/**
	 * This object represents a Game Map world.
	 * @param world the Map's {@link org.bukkit.World} 
	 * @param spawnLocation the Map's {@link org.bukkit.Location}
	 * @param worldConfig the Map's JSON Config (represented by a {@link org.json.JSONObject})
	 */
	public GameMap(World world, Location spawnLocation, Config worldConfig) {
		this.world = world;
		this.spawnLocation = spawnLocation;
		this.worldConfig = worldConfig;
	}
	
	/**
	 * Gets the Map's {@link org.bukkit.World} 
	 * @return the Map's {@link org.bukkit.World} 
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Gets the Map's {@link org.bukkit.Location}
	 * @return the Map's {@link org.bukkit.Location}
	 */
	public Location getSpawn() {
		return this.spawnLocation;
	}
	
	/**
	 * Gets the Map's JSON Config (represented by a {@link org.json.JSONObject})
	 * @return the Map's JSON Config (represented by a {@link org.json.JSONObject})
	 */
	public JSONObject getConfig() {
		return this.worldConfig;
	}
}
