package be.hctel.renaissance.global.stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.hctel.api.Utils;
import be.hctel.renaissance.global.storage.SQLConnector;

/**
 * SuperClass for every Renaissance Project plugin's stats saving system
 * 
 * @author hctel - <a href="https://links.hctel.net/">LinkTree</a>
 */
public class Stats {
	
	private Plugin plugin;
	private SQLConnector connector;
	
	protected HashMap<OfflinePlayer, JSONObject> jsons = new HashMap<>();
	protected HashMap<OfflinePlayer, JSONObject> preferences = new HashMap<>();
	protected HashMap<OfflinePlayer, JSONArray> joinMessages = new HashMap<>();
	protected HashMap<OfflinePlayer, String> currentJoinMessages = new HashMap<>(); 
	protected OfflinePlayer topPlayer;
	
	protected String defaultStatsJson = "{\"points\":0, \"victories\":0, \"gamesPlayed\":0, \"deaths\":0, \"kills\":0, \"achievements\":{}, \"achievementsProgress\":{}}";
	protected String defaultPrefsJson = "{\"viewOthers\":\"visible\"}";
	protected String defaultJoinMessages = "[\"%s has joined.\"]";
	protected String defaultJoinMessage = "%s has joined.";
	
	/**
	 * the super() to call when creating a Stats child class constructor
	 * @param plugin the plugin's main class (extends {@link org.bukkit.plugin.java.JavaPlugin})
	 * @param connector An instance of an {@link be.hctel.renaissance.global.storage.SQLConnector}
	 */
	protected Stats(Plugin plugin, SQLConnector connector) {
		this.plugin = plugin;
		this.connector = connector;
	}
	
	public boolean loadPlayer(OfflinePlayer player) {
		if(player == null) return false;
		if(!jsons.containsKey(player)) {
			try {
				plugin.getLogger().info("Loading stats for " + player.getName() + " (" + Utils.getUUID(player) + ")");
				ResultSet rs = connector.executeQuery(String.format("SELECT * FROM %s WHERE uuid = '%s';", plugin.getName(), Utils.getUUID(player)));
				if(rs.next()) {
					jsons.put(player, new JSONObject(rs.getString("json")));
					preferences.put(player, new JSONObject(rs.getString("preferences")));
					joinMessages.put(player, new JSONArray(rs.getString("ownedJoinMessages")));
					currentJoinMessages.put(player, rs.getString("currentJoinMessage"));
				} else {
					connector.execute(String.format("INSERT INTO %s (uuid, json, preferences, ownedJoinMessages, currentJoinMessage) VALUES ('%s', '%s', '%s', '%s', '%s');", plugin.getName(), Utils.getUUID(player), defaultStatsJson, defaultPrefsJson, defaultJoinMessages, defaultJoinMessage));
					jsons.put(player, new JSONObject(defaultStatsJson));
					preferences.put(player, new JSONObject(defaultPrefsJson));
					joinMessages.put(player, new JSONArray(defaultJoinMessages));
					currentJoinMessages.put(player, defaultJoinMessage);
				}
				rs.close();
				rs = connector.executeQuery(String.format("SELECT uuid FROM %s ORDER BY points DESC LIMIT 1", plugin.getName()));
				if(rs.next()) {

				}
				plugin.getLogger().info("Player stats loading complete!");
			} catch (SQLException | JSONException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void unloadPlayer(OfflinePlayer player) {
		if(jsons.containsKey(player)) {
			try {
				plugin.getLogger().info("Loading stats for " + player.getName() + " (" + Utils.getUUID(player) + ")");
				connector.execute(String.format("UPDATE %s SET json = '%s', preferences='%s', ownedJoinMessages = '%s', currentJoinMessage='%s', points = %d WHERE uuid='%s';", plugin.getName(), jsons.get(player), preferences.get(player), joinMessages.get(player), currentJoinMessages.get(player), getPoints(player), Utils.getUUID(player)));
				jsons.remove(player);
				preferences.remove(player);
				joinMessages.remove(player);
				currentJoinMessages.remove(player);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected int getInt(OfflinePlayer player, String key) {
		if(jsons.containsKey(player)) {
			JSONObject json = jsons.get(player);
			if(json.has(key)) {
				return json.getInt(key);
			} else throw new NoSuchElementException("Key does not exist in json.");
		} else return 0;
	}
	
	public int getPoints(OfflinePlayer player) {
		return getInt(player, "points");
	}
	
	public int getVictories(OfflinePlayer player) {
		return getInt(player, "victories");
	}
	
	public int getGamesPlayed(OfflinePlayer player) {
		return getInt(player, "gamesPlayed");
	}
	
	public int getDeaths(OfflinePlayer player) {
		return getInt(player, "deaths");
	}
	
	public int getKills(OfflinePlayer player) {
		return getInt(player, "kills");
	}
	
	public String getJoinMessage(OfflinePlayer player) {
		return currentJoinMessages.get(player);
	}
	
	public JSONArray getAvailableJoinMessages(OfflinePlayer player) {
		return joinMessages.get(player);
	}
	
	public JSONObject getPreferences(OfflinePlayer player) {
		return preferences.get(player);
	}
	
	
	protected void setKey(OfflinePlayer player, String key, Object value) {
		if(jsons.containsKey(player)) {
			jsons.get(player).put(key, value);
		} else throw new NoSuchElementException("The specified player isn't loaded.");
	}
	
	public void setPoints(OfflinePlayer player, int amount) {
		setKey(player, "points", amount);
	}
	
	public void addPoints(OfflinePlayer player, int amount) {
		setPoints(player, getPoints(player)+amount);
	}
	
	public void setVictories(OfflinePlayer player, int amount) {
		setKey(player, "victories", amount);
	}
	
	public void addVictory(OfflinePlayer player) {
		setVictories(player, getVictories(player)+1);
	}
	
	public void setGamesPlayed(OfflinePlayer player, int amount) {
		setKey(player, "gamesPlayed", amount);
	}
	
	public void addGamePlayed(OfflinePlayer player) {
		setGamesPlayed(player, getGamesPlayed(player)+1);
	}
	
	public void setDeaths(OfflinePlayer player, int amount) {
		setKey(player, "deaths", player);
	}
	
	public void addDeaths(OfflinePlayer player, int amount) {
		setDeaths(player, getDeaths(player) + amount);
	}
	
	public void addDeath(OfflinePlayer player) {
		addDeaths(player, 1);
	}
	
	public void setKills(OfflinePlayer player, int amount) {
		setKey(player, "kills", amount);
	}
	
	public void addKills(OfflinePlayer player, int amount) {
		setKills(player, getKills(player)+amount);
	}
	
	public void addKill(OfflinePlayer player) {
		addKills(player, 1);
	}
	
	public void setJoinMessage(OfflinePlayer player, String joinMessage) {
		if(currentJoinMessages.containsKey(player)) currentJoinMessages.put(player, joinMessage);
	}
	
}
