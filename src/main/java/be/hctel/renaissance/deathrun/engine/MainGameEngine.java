package be.hctel.renaissance.deathrun.engine;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import be.hctel.renaissance.deathrun.DeathRun;

/**
 * <p>Main Game Engine class for DeathRun game.
 * <p>Manages timing, roles, countdowns, etc...
 * 
 * @author <a href="https://hctel.net/">hctel</a>: <a href="https://links.hctel.net/">LinkTree</a>
 *
 */
public class MainGameEngine {
	
	private DeathRun plugin;
	
	/**
	 * Game Engine constructor
	 * @param plugin This plugin's main class
	 */
	public MainGameEngine(DeathRun plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * <p>Registers a player death (doesn't actually kill the player). 
	 * <p>Teleports to spawn and displays the death message.
	 * @param player The player to kill
	 */
	public void killPlayer(Player player) {
		if(player.getGameMode() == GameMode.CREATIVE) return;
		Location toTeleport = player.getRespawnLocation();
		if(toTeleport == null) toTeleport = plugin.mapManager.getMap(player.getWorld()).getSpawn();
		player.teleport(toTeleport);
		player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 10f, 1f);
		player.sendTitle("§4§l✖", "You died!", 0, 50, 20);
	}
	
	/**
	 * Gets a player's role
	 * @param player
	 * @return the {@link Role} of the player
	 */
	public Role getRole(Player player) {
		return Role.TEST;
	}
	
}
