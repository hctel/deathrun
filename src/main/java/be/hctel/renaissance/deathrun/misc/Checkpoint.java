package be.hctel.renaissance.deathrun.misc;

import org.bukkit.Location;
import org.json.JSONObject;

import be.hctel.api.Utils;

public class Checkpoint {
	private int tokensAmount;
	private String name;
	private Location checkpointLocation;
	private Location respawnLocation;
	
	public Checkpoint(Location checkpointLocation, Location respawnLocation, String name, int tokens) {
		this.checkpointLocation = checkpointLocation.clone();
		this.respawnLocation = respawnLocation.clone();
		this.name = name;
		this.tokensAmount = tokens;
	}
	
	public Location getLocation() {
		return this.checkpointLocation;
	}
	
	public Location getRespawnLocation() {
		return this.respawnLocation.clone().add(0,0.5,0);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getTokens() {
		return this.tokensAmount;
	}
	
	public JSONObject getSaveJson() {
		JSONObject out = new JSONObject();
		out.put("name", getName());
		out.put("tokens", getTokens());
		out.put("checkpointLocation", Utils.locationToJson(getLocation()));
		out.put("respawnLocation", Utils.locationToJson(getRespawnLocation()));
		return out;
	}
	
	public static Checkpoint getFromJson(JSONObject j) {
		try {
			String name = j.getString("name");
			int tokens = j.getInt("tokens");
			Location checkpointLocation = Utils.jsonToLocation(j.getJSONObject("checkpointLocation"));
			Location respawnLocation = Utils.jsonToLocation(j.getJSONObject("respawnLocation"));
			return new Checkpoint(checkpointLocation, respawnLocation, name, tokens);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("The checkpoint JSON is either damaged or missing");
		}
	}
}
