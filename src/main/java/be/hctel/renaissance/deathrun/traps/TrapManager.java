package be.hctel.renaissance.deathrun.traps;

import java.util.ArrayList;

import org.json.JSONObject;

import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.global.mapmanager.GameMap;

public class TrapManager {
	
	private DeathRun plugin;
	
	private GameMap map;
	
	private ArrayList<TrapControls> traps = new ArrayList<>();
	
	public TrapManager(DeathRun plugin, GameMap map) {
		this.plugin = plugin;
		this.map = map;
	}
	
	public void addTrap(TrapControls controls) {
		traps.add(controls);
		JSONObject controlsJson = controls.getJsonObject();
		map.getConfig().getJSONArray("traps").put(controlsJson);
	}
	
	public void addTrap(JSONObject trapJson) {
		traps.add(TrapControls.getFromJsonObject(plugin, trapJson));
	}
	
	public void stop() {
		for(TrapControls T : traps) {
			T.serverStop();
		}
	}
	
}
