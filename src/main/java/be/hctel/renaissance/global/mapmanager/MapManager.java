package be.hctel.renaissance.global.mapmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.JSONArray;
import org.json.JSONObject;

import be.hctel.api.Utils;
import be.hctel.api.config.Config;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.traps.TrapManager;

public class MapManager {
	
	private Config mapConfig;
	private DeathRun plugin;
	
	private ArrayList<GameMap> gameMaps = new ArrayList<>();
	private HashMap<World, GameMap> gameMapsPerWorld = new HashMap<>();
	private HashMap<GameMap, TrapManager> trapManagers = new HashMap<>();
	
	
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
			plugin.getLogger().info("Loading " + W.getName());
			if(!mapConfig.getConfig().has(W.getName())) {
				JSONObject mapJson = new JSONObject();
				mapJson.put("runnerSpawn", Utils.locationToJson(new Location(W, 0,0,0)));
				mapJson.put("deathSpawn", Utils.locationToJson(new Location(W, 0,0,0)));
				mapJson.put("traps", new JSONArray());
				Location spawnLocation = Utils.jsonToLocation(mapJson.getJSONObject("runnerSpawn"));
				GameMap map = new GameMap(W, spawnLocation, mapJson);
				TrapManager trapManager = new TrapManager(this.plugin, map);
				gameMaps.add(map);
				gameMapsPerWorld.put(W, map);
				mapConfig.getConfig().put(W.getName(), mapJson);
				trapManagers.put(map, trapManager);
			} else {
				JSONObject mapJson = mapConfig.getConfig().getJSONObject(W.getName());
				Location spawnLocation = Utils.jsonToLocation(mapJson.getJSONObject("runnerSpawn"));
				GameMap map = new GameMap(W, spawnLocation, mapJson);
				TrapManager trapManager = new TrapManager(this.plugin,map);
				gameMaps.add(map);
				gameMapsPerWorld.put(W, map);
				for(Object O : mapJson.getJSONArray("traps")) {
					if(O instanceof JSONObject) {
						JSONObject trapObject = (JSONObject) O;
						trapManager.addTrap(trapObject);
					}
				}
				trapManagers.put(map, trapManager);
			}
		}
	}
	
	public GameMap getMap(World w) {
		return gameMapsPerWorld.get(w);
	}
	
	public Config getMapConfig() {
		return this.mapConfig;
	}
	
	public JSONObject getMapConfig(World w) {
		return getMap(w).getConfig();
	}
	
	public TrapManager getTrapManager(GameMap map) {
		return trapManagers.get(map);
	}
	
	public void onDisable() {
		try {
			for(TrapManager M : trapManagers.values()) {
				M.stop();
			}
			this.mapConfig.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
