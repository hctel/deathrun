package be.hctel.renaissance.global.mapmanager;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.json.JSONObject;
import org.mvplugins.multiverse.external.minidev.json.JSONArray;

import be.hctel.renaissance.deathrun.misc.Checkpoint;

/**
 * 
 * @author <a href="https://hctel.net/">hctel</a>
 *
 */
public class GameMap {
	private World world;
	private Location spawnLocation;
	private JSONObject worldConfig;
	
	private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
	
	/**
	 * This object represents a Game Map world.
	 * @param world the Map's {@link org.bukkit.World} 
	 * @param spawnLocation the Map's {@link org.bukkit.Location}
	 * @param worldConfig the Map's JSON Config (represented by a {@link org.json.JSONObject})
	 */
	public GameMap(World world, Location spawnLocation, JSONObject worldConfig) {
		this.world = world;
		this.spawnLocation = spawnLocation;
		this.worldConfig = worldConfig;
		if(worldConfig.has("checkpoints")) {
			for(Object O : worldConfig.getJSONArray("checkpoints")) {
				if(O instanceof JSONObject) {
					checkpoints.add(Checkpoint.getFromJson((JSONObject) O));
				}
			}
		} else worldConfig.put("checkpoints", new JSONArray());
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
	
	public String getName() {
		return getConfig().getString("name");
	}
	
	public String getAuthor() {
		return getConfig().getString("author");
	}
	
	/**
	 * Returns the ArrayList containing the checkpoints of a Game Map
	 * @return
	 */
	public ArrayList<Checkpoint> getCheckpoints() {
		return this.checkpoints;
	}
	
	/**
	 * Adds a checkpoint to the Game Map
	 * @param cp
	 */
	public void addCheckpoint(Checkpoint cp) {
		checkpoints.add(cp);
		worldConfig.getJSONArray("checkpoints").put(cp.getSaveJson());
	}
}
